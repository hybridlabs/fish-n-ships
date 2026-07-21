package dev.hybridlabs.fishnships.entity.ship

import com.mojang.serialization.Codec
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.world.inventory.ShipMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.util.ByIdMap
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.ContainerEntity
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.IceBlock
import net.minecraft.world.level.block.WaterlilyBlock
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.Shapes
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.constant.DefaultAnimations
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.IntFunction
import kotlin.math.max

open class ShipEntity(
    type: EntityType<out ShipEntity>,
    world: Level,
) :
    Entity(type, world), PlayerRideable, HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private val animCache = GeckoLibUtil.createInstanceCache(this)
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(30, ItemStack.EMPTY)
    private var shipLootTable: ResourceLocation? = null
    private var shipLootTableSeed: Long = 0
    private var inputLeft = false
    private var inputRight = false
    private var inputUp = false
    private var inputDown = false
    private var inputJumping = false
    private var inputSprint = false
    private var lastJumpInput = false
    private var deltaRotation = 0f
    private var lerpSteps = 0
    private var lerpX = 0.0
    private var lerpY = 0.0
    private var lerpZ = 0.0
    private var lerpYRot = 0.0
    private var lerpXRot = 0.0
    private var invFriction = 0f
    private var outOfControlTicks = 0f
    private var waterLevel = 0.0
    private var landFriction = 0f
    private var status: Status? = null
    private var oldStatus: Status? = null
    private var lastYd = 0.0

    private var litTime = 0
    private var litDuration = 0
    private val dataAccess = object : ContainerData {
        override fun get(index: Int): Int {
            return when (index) {
                0 -> litTime
                1 -> litDuration
                else -> 0
            }
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> litTime = value
                1 -> litDuration = value
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    //#region Data
    override fun defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0)
        this.entityData.define(DATA_ID_HURTDIR, 1)
        this.entityData.define(DATA_ID_DAMAGE, 0.0f)
        this.entityData.define(DATA_ID_RIGHT_PROPELLER, false)
        this.entityData.define(DATA_ID_LEFT_PROPELLER, false)
        this.entityData.define(SAIL_COLOR, FlagColor.NONE.id)
        this.entityData.define(IS_BURNING, false)
        this.entityData.define(HAS_TRAWLING_NET, false)
        this.entityData.define(IS_TRAWLING, false)
        this.entityData.define(HAS_ICEBREAKER, false)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        tag.putFloat("Damage", getDamage())
        tag.putString("FlagColor", this.getFlagColor().serializedName)
        tag.putInt("BurnTime", this.litTime)
        this.addChestVehicleSaveData(tag)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        setDamage(tag.getFloat("Damage"))

        if (tag.contains("FlagColor", 8)) {
            val colorName = tag.getString("FlagColor")
            val color = FlagColor.entries.firstOrNull { it.serializedName == colorName } ?: FlagColor.NONE
            setFlagColor(color)
        }

        this.litTime = tag.getInt("BurnTime")
        setLit(litTime > 0)
        this.readChestVehicleSaveData(tag)
    }
    //#endregion

    open fun getFlagColor(): FlagColor {
        return FlagColor.byId(entityData.get(SAIL_COLOR))
    }

    open fun setFlagColor(flagColor: FlagColor) {
        entityData.set(SAIL_COLOR, flagColor.id)
    }

    override fun getEyeHeight(pose: Pose, size: EntityDimensions): Float {
        return size.height * 0.5f
    }

    override fun lerpTo(
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
        posRotationIncrements: Int,
        teleport: Boolean,
    ) {
        this.lerpX = x
        this.lerpY = y
        this.lerpZ = z
        this.lerpYRot = yaw.toDouble()
        this.lerpXRot = pitch.toDouble()
        this.lerpSteps = 10
    }

    open fun getBurnDuration(fuel: ItemStack): Int {
        return if (fuel.isEmpty) 0
        else AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuel.item, 0) * 2
    }

    fun isLit(): Boolean {
        return entityData.get(IS_BURNING)
    }

    fun setLit(value: Boolean) {
        this.entityData.set(IS_BURNING, value)
    }

    fun burnTick() {
        val fuelItemStack = itemStacks[0]

        if (litTime > 0) {
            litTime--
            if (litTime == 0) setLit(false)
            return
        }
        if (fuelItemStack.isEmpty) return

        litTime = getBurnDuration(fuelItemStack)
        if (litTime <= 0) return

        litDuration = litTime

        val itemRemainder = fuelItemStack.item.craftingRemainingItem
        fuelItemStack.shrink(1)
        if (fuelItemStack.isEmpty && itemRemainder != null) itemStacks[0] = itemRemainder.defaultInstance

        setLit(true)
    }

    fun hasTrawlingNet(): Boolean {
        return entityData.get(HAS_TRAWLING_NET)
    }

    fun setTrawling(value: Boolean) {
        this.entityData.set(IS_TRAWLING, value)
    }

    fun isTrawling(): Boolean {
        return entityData.get(IS_TRAWLING)
    }

    fun hasIceBreaker(): Boolean {
        return entityData.get(HAS_ICEBREAKER)
    }

    private fun breakIce() {
        val box = boundingBox.expandTowards(deltaMovement).inflate(1.0)

        val minX = Mth.floor(box.minX)
        val maxX = Mth.floor(box.maxX)
        val minY = Mth.floor(box.minY)
        val maxY = Mth.floor(box.maxY)
        val minZ = Mth.floor(box.minZ)
        val maxZ = Mth.floor(box.maxZ)

        val pos = BlockPos.MutableBlockPos()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    pos.set(x, y, z)

                    val state = level().getBlockState(pos)
                    if (state.`is`(Blocks.ICE) || state.`is`(Blocks.FROSTED_ICE)) {
                        if (level().dimensionType().ultraWarm()) {
                            level().removeBlock(pos, false)
                        } else {
                            level().setBlockAndUpdate(pos, IceBlock.meltsInto())
                            level().neighborChanged(pos, IceBlock.meltsInto().block, pos)
                        }
                    }
                }
            }
        }
    }

    override fun tick() {
        super.tick()

        burnTick()

        this.oldStatus = this.status
        this.status = this.getStatus()
        if (this.status != Status.UNDER_WATER && this.status != Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0f
        } else {
            ++this.outOfControlTicks
        }

        if (!this.level().isClientSide && this.outOfControlTicks >= 60.0f) {
            this.ejectPassengers()
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1)
        }

        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f)
        }

        super.tick()
        this.tickLerp()
        if (this.isControlledByLocalInstance) {
            if (this.firstPassenger !is Player) {
                setPropellerState(left = false, right = false)
            }

            floatShip()

            if (this.level().isClientSide) {
                if (isLit()) controlShip()

                this.level().sendPacketToServer(
                    ServerboundPaddleBoatPacket(
                        getPropellerState(0),
                        getPropellerState(1)
                    )
                )
            }

            this.move(MoverType.SELF, this.deltaMovement)
        } else {
            this.deltaMovement = Vec3.ZERO
        }

        this.checkInsideBlocks()

        if (!level().isClientSide && hasIceBreaker()) {
            breakIce()
        }
    }

    private fun tickLerp() {
        if (this.isControlledByLocalInstance) {
            this.lerpSteps = 0
            this.syncPacketPositionCodec(this.x, this.y, this.z)
        }

        if (this.lerpSteps > 0) {
            val d = this.x + (this.lerpX - this.x) / this.lerpSteps
            val e = this.y + (this.lerpY - this.y) / this.lerpSteps
            val f = this.z + (this.lerpZ - this.z) / this.lerpSteps
            val g = Mth.wrapDegrees(this.lerpYRot - this.yRot)
            this.yRot += g.toFloat() / this.lerpSteps
            this.xRot += (this.lerpXRot - this.xRot).toFloat() / this.lerpSteps
            this.lerpSteps--
            this.setPos(d, e, f)
            this.setRot(this.yRot, this.xRot)
        }
    }

    private fun getStatus(): Status {
        val shipStatus = this.isUnderwater()
        if (shipStatus != null) {
            this.waterLevel = this.boundingBox.maxY
            return shipStatus
        } else if (this.checkInWater()) {
            return Status.IN_WATER
        } else {
            val f = this.getGroundFriction()
            if (f > 0.0f) {
                this.landFriction = f
                return Status.ON_LAND
            } else {
                return Status.IN_AIR
            }
        }
    }

    fun getWaterLevelAbove(): Float {
        val aabb = this.boundingBox
        val i = Mth.floor(aabb.minX)
        val j = Mth.ceil(aabb.maxX)
        val k = Mth.floor(aabb.maxY)
        val l = Mth.ceil(aabb.maxY - this.lastYd)
        val i1 = Mth.floor(aabb.minZ)
        val j1 = Mth.ceil(aabb.maxZ)
        val mutableBlockPos = BlockPos.MutableBlockPos()

        label39@ for (k1 in k..<l) {
            var f = 0.0f

            for (l1 in i..<j) {
                for (i2 in i1..<j1) {
                    mutableBlockPos.set(l1, k1, i2)
                    val fluidstate = this.level().getFluidState(mutableBlockPos)
                    if (fluidstate.`is`(FluidTags.WATER)) {
                        f = max(f, fluidstate.getHeight(this.level(), mutableBlockPos))
                    }

                    if (f >= 1.0f) {
                        continue@label39
                    }
                }
            }

            if (f < 1.0f) {
                return mutableBlockPos.y.toFloat() + f
            }
        }

        return (l + 1).toFloat()
    }

    fun getGroundFriction(): Float {
        val aabb = this.boundingBox
        val aabb1 = AABB(aabb.minX, aabb.minY - 0.001, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ)
        val i = Mth.floor(aabb1.minX) - 1
        val j = Mth.ceil(aabb1.maxX) + 1
        val k = Mth.floor(aabb1.minY) - 1
        val l = Mth.ceil(aabb1.maxY) + 1
        val i1 = Mth.floor(aabb1.minZ) - 1
        val j1 = Mth.ceil(aabb1.maxZ) + 1
        val voxelshape = Shapes.create(aabb1)
        var f = 0.0f
        var k1 = 0
        val mutableBlockPos = BlockPos.MutableBlockPos()

        for (l1 in i..<j) {
            for (i2 in i1..<j1) {
                val j2 = (if (l1 != i && l1 != j - 1) 0 else 1) + (if (i2 != i1 && i2 != j1 - 1) 0 else 1)
                if (j2 != 2) {
                    for (k2 in k..<l) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            mutableBlockPos.set(l1, k2, i2)
                            val blockstate = this.level().getBlockState(mutableBlockPos)
                            if (blockstate.block !is WaterlilyBlock && Shapes.joinIsNotEmpty(
                                    blockstate.getCollisionShape(
                                        this.level(),
                                        mutableBlockPos
                                    ).move(l1.toDouble(), k2.toDouble(), i2.toDouble()), voxelshape, BooleanOp.AND
                                )
                            ) {
                                f += blockstate.block.getFriction()
                                ++k1
                            }
                        }
                    }
                }
            }
        }

        return f / k1.toFloat()
    }

    private fun checkInWater(): Boolean {
        val aabb = this.boundingBox
        val i = Mth.floor(aabb.minX)
        val j = Mth.ceil(aabb.maxX)
        val k = Mth.floor(aabb.minY)
        val l = Mth.ceil(aabb.minY + 0.001)
        val i1 = Mth.floor(aabb.minZ)
        val j1 = Mth.ceil(aabb.maxZ)
        var flag = false
        this.waterLevel = -Double.MAX_VALUE
        val mutableBlockPos = BlockPos.MutableBlockPos()

        for (k1 in i..<j) {
            for (l1 in k..<l) {
                for (i2 in i1..<j1) {
                    mutableBlockPos.set(k1, l1, i2)
                    val fluidstate = this.level().getFluidState(mutableBlockPos)
                    if (fluidstate.`is`(FluidTags.WATER)) {
                        val f = l1.toFloat() + fluidstate.getHeight(this.level(), mutableBlockPos)
                        this.waterLevel = max(f.toDouble(), this.waterLevel)
                        flag = flag or (aabb.minY < f.toDouble())
                    }
                }
            }
        }

        return flag
    }

    private fun isUnderwater(): Status? {
        val aabb = this.boundingBox
        val d0 = aabb.maxY + 0.001
        val i = Mth.floor(aabb.minX)
        val j = Mth.ceil(aabb.maxX)
        val k = Mth.floor(aabb.maxY)
        val l = Mth.ceil(d0)
        val i1 = Mth.floor(aabb.minZ)
        val j1 = Mth.ceil(aabb.maxZ)
        var flag = false
        val mutableBlockPos = BlockPos.MutableBlockPos()

        for (k1 in i..<j) {
            for (l1 in k..<l) {
                for (i2 in i1..<j1) {
                    mutableBlockPos.set(k1, l1, i2)
                    val fluidstate = this.level().getFluidState(mutableBlockPos)
                    if (fluidstate.`is`(FluidTags.WATER) && d0 < (mutableBlockPos.y
                            .toFloat() + fluidstate.getHeight(this.level(), mutableBlockPos)).toDouble()
                    ) {
                        if (!fluidstate.isSource) {
                            return Status.UNDER_FLOWING_WATER
                        }

                        flag = true
                    }
                }
            }
        }

        return if (flag) Status.UNDER_WATER else null
    }

    private fun floatShip() {
        val d0 = -0.04
        var d1 = if (this.isNoGravity) 0.0 else -0.04
        var d2 = 0.0
        this.invFriction = 0.05f
        if (this.oldStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
            this.waterLevel = this.getY(1.0)
            this.setPos(this.x, (this.getWaterLevelAbove() - this.bbHeight).toDouble() + 0.101, this.z)
            this.deltaMovement = this.deltaMovement.multiply(1.0, 0.0, 1.0)
            this.lastYd = 0.0
            this.status = Status.IN_WATER
        } else {
            if (this.status == Status.IN_WATER) {
                d2 = (this.waterLevel - this.y) / this.bbHeight.toDouble()
                this.invFriction = 0.9f
            } else if (this.status == Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4
                this.invFriction = 0.9f
            } else if (this.status == Status.UNDER_WATER) {
                d2 = 0.01
                this.invFriction = 0.45f
            } else if (this.status == Status.IN_AIR) {
                this.invFriction = 0.9f
            } else if (this.status == Status.ON_LAND) {
                this.invFriction = this.landFriction
                if (this.getControllingPassenger() is Player) {
                    this.landFriction /= 2.0f
                }
            }

            val vec3 = this.deltaMovement
            this.setDeltaMovement(
                vec3.x * this.invFriction.toDouble(),
                vec3.y + d1,
                vec3.z * this.invFriction.toDouble()
            )
            this.deltaRotation *= this.invFriction
            if (d2 > 0.0) {
                val vec31 = this.deltaMovement
                this.setDeltaMovement(vec31.x, (vec31.y + d2 * 0.06153846016296973) * 0.75, vec31.z)
            }
        }
    }

    private fun controlShip() {
        if (this.isVehicle) {
            var f = 0.0f
            if (this.inputLeft) {
                --this.deltaRotation
            }

            if (this.inputRight) {
                ++this.deltaRotation
            }

            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                f += 0.005f
            }

            this.yRot += this.deltaRotation
            if (this.inputUp) {
                f += 0.04f
            }

            if (this.inputDown) {
                f -= 0.005f
            }

            if (inputJumping && !lastJumpInput) {
                setTrawling(!isTrawling())
            }

            lastJumpInput = inputJumping

            this.deltaMovement = this.deltaMovement.add(
                (Mth.sin(-this.yRot * (Math.PI.toFloat() / 180f)) * f).toDouble(),
                0.0,
                (Mth.cos(this.yRot * (Math.PI.toFloat() / 180f)) * f).toDouble()
            )
            this.setPropellerState(
                this.inputRight && !this.inputLeft || this.inputUp,
                this.inputLeft && !this.inputRight || this.inputUp
            )
        }
    }

    fun setInput(
        inputLeft: Boolean,
        inputRight: Boolean,
        inputUp: Boolean,
        inputDown: Boolean,
        inputJumping: Boolean,
        inputSprint: Boolean,
    ) {
        this.inputLeft = inputLeft
        this.inputRight = inputRight
        this.inputUp = inputUp
        this.inputDown = inputDown
        this.inputJumping = inputJumping
        this.inputSprint = inputSprint
    }

    fun setPropellerState(left: Boolean, right: Boolean) {
        this.entityData.set(DATA_ID_LEFT_PROPELLER, left)
        this.entityData.set(DATA_ID_RIGHT_PROPELLER, right)
    }

    fun getPropellerState(side: Int): Boolean {
        return this.entityData.get(if (side == 0) DATA_ID_LEFT_PROPELLER else DATA_ID_RIGHT_PROPELLER) && this.getControllingPassenger() != null
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (source.entity != null && this.hasPassenger(source.entity)) {
            return false
        }

        if (this.isInvulnerableTo(source)) {
            return false
        } else if (!this.level().isClientSide && !this.isRemoved) {
            this.setHurtTime(10)
            this.setDamage(this.getDamage() + amount * 10.0f)
            this.markHurt()
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.entity)
            val flag = source.entity is Player && (source.entity as Player).abilities.instabuild
            if (flag || this.getDamage() > 90.0f) {
                if (!flag && this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(source)
                }

                this.discard()
            }

            return true
        } else {
            return true
        }
    }

    fun getShipItem(ship: ShipEntity): ItemStack {
        val stack = ItemStack(FSItems.SHIP.get())
        val tag = stack.orCreateTag

        tag.putInt("FlagColor", ship.getFlagColor().id)

        return stack
    }

    fun setDamage(damageTaken: Float) {
        this.entityData.set(DATA_ID_DAMAGE, damageTaken)
    }

    fun getDamage(): Float {
        return this.entityData.get(DATA_ID_DAMAGE)
    }

    fun setHurtTime(hurtTime: Int) {
        this.entityData.set(DATA_ID_HURT, hurtTime)
    }

    fun getHurtTime(): Int {
        return this.entityData.get(DATA_ID_HURT) as Int
    }

    override fun getMotionDirection(): Direction {
        return this.direction.clockWise
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController(this, "Ship Controller", 4) { state ->
                val moving = this.deltaMovement.horizontalDistanceSqr() > 0.01

                when {
                    isInWater && moving -> {
                        state.setAndContinue(DefaultAnimations.SWIM)
                    }

                    else -> {
                        state.setAndContinue(DefaultAnimations.IDLE)
                    }
                }
            }
        )
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
        return animCache
    }

    override fun getMovementEmission(): MovementEmission {
        return MovementEmission.EVENTS
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return canVehicleCollide(this, entity)
    }

    fun canVehicleCollide(vehicle: Entity, entity: Entity): Boolean {
        return (entity.canBeCollidedWith() || entity.isPushable) && !vehicle.isPassengerOfSameVehicle(entity)
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun isPushable(): Boolean {
        return true
    }

    override fun getPassengersRidingOffset(): Double {
        return 1.5
    }

    protected fun clampRotation(entityToUpdate: Entity) {
        entityToUpdate.setYBodyRot(this.yRot)
        val f = Mth.wrapDegrees(entityToUpdate.yRot - this.yRot)
        val f1 = Mth.clamp(f, -105.0f, 105.0f)
        entityToUpdate.yRotO += f1 - f
        entityToUpdate.yRot = entityToUpdate.yRot + f1 - f
        entityToUpdate.yHeadRot = entityToUpdate.yRot
    }

    override fun onPassengerTurned(entityToUpdate: Entity) {
        this.clampRotation(entityToUpdate)
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {

        return if (player.isSecondaryUseActive) {
            InteractionResult.PASS
        } else if (this.isVehicle) {
            InteractionResult.PASS
        } else if (!this.level().isClientSide) {
            if (player.startRiding(this)) InteractionResult.CONSUME else InteractionResult.PASS
        } else {
            InteractionResult.SUCCESS
        }
    }

    override fun isPickable(): Boolean {
        return !this.isRemoved
    }

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        if (this.hasPassenger(passenger)) {
            callback.accept(
                passenger,
                this.x,
                this.y + this.passengersRidingOffset + passenger.myRidingOffset,
                this.z
            )
        }
    }

    override fun canAddPassenger(passenger: Entity): Boolean {
        return this.passengers.isEmpty()
    }

    override fun getControllingPassenger(): LivingEntity? {
        val entity = this.firstPassenger
        val livingentity1: LivingEntity? = entity as? LivingEntity

        return livingentity1
    }
    //#endregion

    //#region Container
    protected open fun destroy(damageSource: DamageSource) {
        val stack = getShipItem(this)
        this.spawnAtLocation(stack)
        this.chestVehicleDestroyed(damageSource, this.level(), this)
    }

    override fun remove(reason: RemovalReason) {
        if (!this.level().isClientSide && reason.shouldDestroy()) {
            Containers.dropContents(this.level(), this, this)
        }
        super.remove(reason)
    }

    override fun openCustomInventoryScreen(player: Player) {
        player.openMenu(this)
        if (!player.level().isClientSide) {
            this.gameEvent(GameEvent.CONTAINER_OPEN, player)
        }
    }

    override fun getLootTable(): ResourceLocation? {
        return shipLootTable
    }

    override fun setLootTable(id: ResourceLocation?) {
        shipLootTable = id
    }

    override fun getLootTableSeed(): Long {
        return shipLootTableSeed
    }

    override fun setLootTableSeed(seed: Long) {
        shipLootTableSeed = seed
    }

    override fun getItemStacks(): NonNullList<ItemStack> {
        return this.itemStacks
    }

    override fun clearItemStacks() {
        this.itemStacks = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)
    }

    override fun getContainerSize(): Int {
        return 30
    }

    override fun getItem(slot: Int): ItemStack {
        return this.getChestVehicleItem(slot)
    }

    override fun removeItem(slot: Int, amount: Int): ItemStack {
        val result = removeChestVehicleItem(slot, amount)
        updateIceBreaker()
        updateTrawlingNet()
        return result
    }

    override fun removeItemNoUpdate(slot: Int): ItemStack {
        val result = removeChestVehicleItemNoUpdate(slot)
        updateIceBreaker()
        updateTrawlingNet()
        return result
    }

    override fun setItem(slot: Int, stack: ItemStack) {
        this.setChestVehicleItem(slot, stack)
        updateIceBreaker()
        updateTrawlingNet()
    }

    private fun updateIceBreaker() {
        entityData.set(HAS_ICEBREAKER, itemStacks[1].`is`(FSItems.ICEBREAKER.get()))
    }

    private fun updateTrawlingNet() {
        entityData.set(HAS_TRAWLING_NET, itemStacks[2].`is`(FSItems.TRAWLING_NET.get()))
    }

    override fun setChanged() {
        updateTrawlingNet()
        updateIceBreaker()
    }

    override fun stillValid(player: Player): Boolean {
        return this.isChestVehicleStillValid(player)
    }

    override fun clearContent() {
        this.clearChestVehicleContent()
    }

    override fun createMenu(
        containerId: Int,
        playerInventory: Inventory,
        player: Player,
    ): AbstractContainerMenu? {
        if (this.lootTable != null && player.isSpectator) {
            return null
        } else {
            this.unpackLootTable(playerInventory.player)
            return ShipMenu.threeRows(containerId, playerInventory, this, this.dataAccess)
        }
    }

    fun unpackLootTable(player: Player?) {
        this.unpackChestVehicleLootTable(player)
    }

    override fun stopOpen(player: Player) {
        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(player))
    }
    //#endregion

    companion object {
        private val DATA_ID_HURT: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.INT)

        private val DATA_ID_HURTDIR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.INT)

        private val DATA_ID_DAMAGE: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.FLOAT)

        private val DATA_ID_RIGHT_PROPELLER: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.BOOLEAN)

        private val DATA_ID_LEFT_PROPELLER: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.BOOLEAN)

        private val SAIL_COLOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.INT)

        private val IS_BURNING: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.BOOLEAN)

        private val HAS_TRAWLING_NET: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.BOOLEAN)

        private val IS_TRAWLING: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.BOOLEAN)

        private val HAS_ICEBREAKER: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ShipEntity::class.java, EntityDataSerializers.BOOLEAN)
    }
    
    enum class Status {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR
    }
    
    enum class FlagColor(val id: Int, val key: String) : StringRepresentable {
        NONE(0, ""),
        WHITE(1, "white"),
        ORANGE(2, "orange"),
        MAGENTA(3, "magenta"),
        LIGHT_BLUE(4, "light_blue"),
        YELLOW(5, "yellow"),
        LIME(6, "lime"),
        PINK(7, "pink"),
        GRAY(8, "gray"),
        LIGHT_GRAY(9, "light_gray"),
        CYAN(10, "cyan"),
        PURPLE(11, "purple"),
        BLUE(12, "blue"),
        BROWN(13, "brown"),
        GREEN(14, "green"),
        RED(15, "red"),
        BLACK(16, "black");

        override fun getSerializedName(): String {
            return this.key
        }

        companion object {
            val CODEC: Codec<FlagColor> =
                StringRepresentable.fromEnum { entries.toTypedArray() }

            val BY_ID: IntFunction<FlagColor> = ByIdMap.continuous(
                { color -> color.id },
                entries.toTypedArray(),
                ByIdMap.OutOfBoundsStrategy.WRAP
            )

            fun byId(id: Int): FlagColor {
                return BY_ID.apply(id)
            }

            fun fromDye(dye: DyeColor): FlagColor {
                return when (dye) {
                    DyeColor.WHITE -> WHITE
                    DyeColor.ORANGE -> ORANGE
                    DyeColor.MAGENTA -> MAGENTA
                    DyeColor.LIGHT_BLUE -> LIGHT_BLUE
                    DyeColor.YELLOW -> YELLOW
                    DyeColor.LIME -> LIME
                    DyeColor.PINK -> PINK
                    DyeColor.GRAY -> GRAY
                    DyeColor.LIGHT_GRAY -> LIGHT_GRAY
                    DyeColor.CYAN -> CYAN
                    DyeColor.PURPLE -> PURPLE
                    DyeColor.BLUE -> BLUE
                    DyeColor.BROWN -> BROWN
                    DyeColor.GREEN -> GREEN
                    DyeColor.RED -> RED
                    DyeColor.BLACK -> BLACK
                }
            }
        }
    }
}