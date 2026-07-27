package dev.hybridlabs.fishnships.entity.ship

import com.google.common.collect.Lists
import com.google.common.collect.UnmodifiableIterator
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.world.inventory.ShipMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.WaterAnimal
import net.minecraft.world.entity.decoration.HangingEntity
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.ContainerEntity
import net.minecraft.world.entity.vehicle.DismountHelper
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.WaterlilyBlock
import net.minecraft.world.level.block.state.BlockState
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
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.withSign

open class RaftEntity(
    type: EntityType<out RaftEntity>,
    world: Level,
) :
    Entity(type, world), HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private val animCache = GeckoLibUtil.createInstanceCache(this)
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(42, ItemStack.EMPTY)
    private var shipLootTable: ResourceLocation? = null
    private var shipLootTableSeed: Long = 0
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
    private var isAboveBubbleColumn = false
    private var bubbleColumnDirectionIsDown = false
    private var bubbleMultiplier = 0f
    private var bubbleAngle = 0f
    private var bubbleAngleO = 0f
    private var leashHolder: Entity? = null
    private var delayedLeashHolderId = 0
    private var leashInfoTag: CompoundTag? = null

    init {
        noCulling = true
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        if (!isAlive) {
            return InteractionResult.PASS
        }

        if (this.getLeashHolder() === player) {
            this.dropLeash(true, !player.abilities.instabuild)
            this.gameEvent(GameEvent.ENTITY_INTERACT, player)
            return InteractionResult.sidedSuccess(level().isClientSide)
        }

        val result = checkAndHandleImportantInteractions(player, hand)
        if (result.consumesAction()) {
            this.gameEvent(GameEvent.ENTITY_INTERACT, player)
            return result
        }

        return if (player.isSecondaryUseActive) {
            InteractionResult.PASS
        } else if (outOfControlTicks < 60.0f) {
            if (!level().isClientSide) {
                if (player.startRiding(this)) {
                    InteractionResult.CONSUME
                } else {
                    InteractionResult.PASS
                }
            } else {
                InteractionResult.SUCCESS
            }
        } else {
            InteractionResult.PASS
        }
    }

    private fun checkAndHandleImportantInteractions(player: Player, hand: InteractionHand): InteractionResult {
        val itemstack = player.getItemInHand(hand)
        if (itemstack.`is`(Items.LEAD) && this.canBeLeashed(player)) {
            this.setLeashedTo(player, true)
            itemstack.shrink(1)
            return InteractionResult.sidedSuccess(this.level().isClientSide)
        }
        return InteractionResult.PASS
    }

    private fun restoreLeashFromSave() {
        if (this.leashInfoTag != null && this.level() is ServerLevel) {
            if (this.leashInfoTag!!.hasUUID("UUID")) {
                val uuid = this.leashInfoTag!!.getUUID("UUID")
                val entity = (this.level() as ServerLevel).getEntity(uuid)
                if (entity != null) {
                    this.setLeashedTo(entity, true)
                    return
                }
            } else if (this.leashInfoTag!!.contains("X", 99) && this.leashInfoTag!!.contains(
                    "Y",
                    99
                ) && this.leashInfoTag!!.contains("Z", 99)
            ) {
                val blockpos = NbtUtils.readBlockPos(this.leashInfoTag)
                this.setLeashedTo(LeashFenceKnotEntity.getOrCreateKnot(this.level(), blockpos), true)
                return
            }

            if (this.tickCount > 100) {
                this.spawnAtLocation(Items.LEAD)
                this.leashInfoTag = null
            }
        }
    }

    protected fun tickLeash() {
        if (this.leashInfoTag != null) {
            this.restoreLeashFromSave()
        }

        if (this.leashHolder != null) {
            if (!this.isAlive || !this.leashHolder!!.isAlive) {
                this.dropLeash(broadcastPacket = true, dropLeash = true)
            }
        }

        val entity = this.getLeashHolder()
        if (entity != null && entity.level() === this.level()) {
            val f = this.distanceTo(entity)

            if (f > 10.0f) {
                this.dropLeash(true, dropLeash = true)
            } else if (f > 6.0f) {
                val d0 = (entity.x - this.x) / f.toDouble()
                val d1 = (entity.y - this.y) / f.toDouble()
                val d2 = (entity.z - this.z) / f.toDouble()

                this.deltaMovement = this.deltaMovement.add(
                    (d0 * d0 * 0.4).withSign(d0),
                    (d1 * d1 * 0.4).withSign(d1),
                    (d2 * d2 * 0.4).withSign(d2)
                )

                this.checkSlowFallDistance()
            }
        }
    }

    protected open fun shouldStayCloseToLeashHolder(): Boolean {
        return true
    }

    protected open fun followLeashSpeed(): Double {
        return 1.0
    }

    protected open fun onLeashDistance(distance: Float) {
    }

    fun dropLeash(broadcastPacket: Boolean, dropLeash: Boolean) {
        if (this.leashHolder != null) {
            this.leashHolder = null
            this.leashInfoTag = null
            if (!this.level().isClientSide && dropLeash) {
                this.spawnAtLocation(Items.LEAD)
            }

            if (!this.level().isClientSide && broadcastPacket && this.level() is ServerLevel) {
                (this.level() as ServerLevel).chunkSource
                    .broadcast(this, ClientboundSetEntityLinkPacket(this, null as Entity?))
            }
        }
    }

    open fun canBeLeashed(player: Player?): Boolean {
        return !this.isLeashed() && this !is Enemy
    }

    fun isLeashed(): Boolean {
        return this.leashHolder != null
    }

    fun getLeashHolder(): Entity? {
        if (this.leashHolder == null && this.delayedLeashHolderId != 0 && this.level().isClientSide) {
            this.leashHolder = this.level().getEntity(this.delayedLeashHolderId)
        }

        return this.leashHolder
    }

    /**
     * Sets the entity to be leashed to.
     */
    fun setLeashedTo(leashHolder: Entity?, broadcastPacket: Boolean) {
        this.leashHolder = leashHolder
        this.leashInfoTag = null
        if (!this.level().isClientSide && broadcastPacket && this.level() is ServerLevel) {
            (this.level() as ServerLevel).chunkSource
                .broadcast(this, ClientboundSetEntityLinkPacket(this, this.leashHolder))
        }

        if (this.isPassenger) {
            this.stopRiding()
        }
    }

    fun setDelayedLeashHolderId(leashHolderID: Int) {
        this.delayedLeashHolderId = leashHolderID
        this.dropLeash(broadcastPacket = false, dropLeash = false)
    }

    override fun defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0)
        this.entityData.define(DATA_ID_HURTDIR, 1)
        this.entityData.define(DATA_ID_DAMAGE, 0.0f)
        this.entityData.define(DATA_ID_BUBBLE_TIME, 0)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        tag.putFloat("Damage", getDamage())

        if (this.leashHolder != null) {
            val compoundtag2 = CompoundTag()
            if (this.leashHolder is LivingEntity) {
                val uuid = this.leashHolder!!.getUUID()
                compoundtag2.putUUID("UUID", uuid)
            } else if (this.leashHolder is HangingEntity) {
                val blockpos = (this.leashHolder as HangingEntity).getPos()
                compoundtag2.putInt("X", blockpos.x)
                compoundtag2.putInt("Y", blockpos.y)
                compoundtag2.putInt("Z", blockpos.z)
            }

            tag.put("Leash", compoundtag2)
        } else if (this.leashInfoTag != null) {
            tag.put("Leash", this.leashInfoTag!!.copy())
        }
        this.addChestVehicleSaveData(tag)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        setDamage(tag.getFloat("Damage"))

        if (tag.contains("Leash", 10)) {
            this.leashInfoTag = tag.getCompound("Leash")
        }
        this.readChestVehicleSaveData(tag)
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return canVehicleCollide(this, entity)
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun isPushable(): Boolean {
        return true
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

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController(this, "Raft Controller", 4) { state ->
                when {
                    isInWater -> {
                        state.setAndContinue(DefaultAnimations.IDLE)
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

    override fun onAboveBubbleCol(downwards: Boolean) {
        if (!this.level().isClientSide) {
            this.isAboveBubbleColumn = true
            this.bubbleColumnDirectionIsDown = downwards
            if (this.bubbleTime == 0) {
                this.bubbleTime = 60
            }
        }

        this.level().addParticle(
            ParticleTypes.SPLASH,
            this.x + this.random.nextFloat().toDouble(),
            this.y + 0.7,
            this.z + this.random.nextFloat().toDouble(),
            0.0,
            0.0,
            0.0
        )
        if (this.random.nextInt(20) == 0) {
            this.level().playLocalSound(
                this.x,
                this.y,
                this.z,
                this.swimSplashSound,
                this.soundSource,
                1.0f,
                0.8f + 0.4f * this.random.nextFloat(),
                false
            )
            this.gameEvent(GameEvent.SPLASH, this.controllingPassenger)
        }
    }

    override fun push(entity: Entity) {
        if (entity is CanoeEntity) {
            if (entity.boundingBox.minY < this.boundingBox.maxY) {
                super.push(entity)
            }
        } else if (entity.boundingBox.minY <= this.boundingBox.minY) {
            super.push(entity)
        }
    }

    override fun isPickable(): Boolean {
        return !this.isRemoved
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

    override fun getMotionDirection(): Direction {
        return this.direction.clockWise
    }

    override fun tick() {
        super.tick()

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

        this.tickLerp()
        this.tickLeash()

        if (this.isControlledByLocalInstance) {

            floatRaft()

            this.move(MoverType.SELF, this.deltaMovement)
        } else {
            this.deltaMovement = Vec3.ZERO
        }

        this.checkInsideBlocks()
        val list = this.level()
            .getEntities(this, this.boundingBox.inflate(0.2, -0.01, 0.2), EntitySelector.pushableBy(this))
        if (!list.isEmpty()) {
            val flag = !this.level().isClientSide

            for (j in list.indices) {
                val entity = list[j] as Entity
                if (!entity.hasPassenger(this)) {
                    if (flag && this.passengers.size < this.maxPassengers && !entity.isPassenger && this.hasEnoughSpaceFor(
                            entity
                        ) && entity is LivingEntity && (entity !is WaterAnimal) && (entity !is Player)
                    ) {
                        entity.startRiding(this)
                    } else {
                        this.push(entity)
                    }
                }
            }
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

    private fun tickBubbleColumn() {
        if (this.level().isClientSide) {
            val i = this.bubbleTime
            if (i > 0) {
                this.bubbleMultiplier += 0.05f
            } else {
                this.bubbleMultiplier -= 0.1f
            }

            this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0f, 1.0f)
            this.bubbleAngleO = this.bubbleAngle
            this.bubbleAngle =
                10.0f * sin((0.5f * this.level().gameTime.toFloat()).toDouble()).toFloat() * this.bubbleMultiplier
        } else {
            if (!this.isAboveBubbleColumn) {
                this.bubbleTime = 0
            }

            var k = this.bubbleTime
            if (k > 0) {
                --k
                this.bubbleTime = k
                val j = 60 - k - 1
                if (j > 0 && k == 0) {
                    this.bubbleTime = 0
                    val vec3 = this.deltaMovement
                    if (this.bubbleColumnDirectionIsDown) {
                        this.deltaMovement = vec3.add(0.0, -0.7, 0.0)
                        this.ejectPassengers()
                    } else {
                        this.setDeltaMovement(
                            vec3.x,
                            if (this.hasPassenger { entity: Entity? -> entity is Player }) 2.7 else 0.6,
                            vec3.z
                        )
                    }
                }

                this.isAboveBubbleColumn = false
            }
        }
    }

    protected open val singlePassengerXOffset: Float
        get() = 0.0f

    fun hasEnoughSpaceFor(entity: Entity): Boolean {
        return entity.bbWidth < this.bbWidth
    }

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        if (this.hasPassenger(passenger)) {
            var f = this.singlePassengerXOffset
            val f1 =
                ((if (this.isRemoved) 0.01 else this.passengersRidingOffset) + passenger.myRidingOffset).toFloat()
            if (this.passengers.size > 1) {
                val i = this.passengers.indexOf(passenger)
                f = if (i == 0) {
                    0.2f
                } else {
                    -0.6f
                }

                if (passenger is Animal) {
                    f += 0.2f
                }
            }

            val vec3 = (Vec3(
                f.toDouble(),
                0.0,
                0.0
            )).yRot(-this.yRot * (Math.PI.toFloat() / 180f) - (Math.PI.toFloat() / 2f))
            callback.accept(passenger, this.x + vec3.x, this.y + f1.toDouble(), this.z + vec3.z)
            passenger.yRot += this.deltaRotation
            passenger.yHeadRot += this.deltaRotation
            this.clampRotation(passenger)
            if (passenger is Animal && this.passengers.size == this.maxPassengers) {
                val j = if (passenger.id % 2 == 0) 90 else 270
                passenger.setYBodyRot(passenger.yBodyRot + j.toFloat())
                passenger.setYHeadRot(passenger.getYHeadRot() + j.toFloat())
            }
        }
    }

    override fun getDismountLocationForPassenger(livingEntity: LivingEntity): Vec3 {
        val vec3 = getCollisionHorizontalEscapeVector(
            (this.bbWidth * Mth.SQRT_OF_TWO).toDouble(),
            livingEntity.bbWidth.toDouble(),
            livingEntity.yRot
        )
        val d0 = this.x + vec3.x
        val d1 = this.z + vec3.z
        val blockpos = BlockPos.containing(d0, this.boundingBox.maxY, d1)
        val blockpos1 = blockpos.below()
        if (!this.level().isWaterAt(blockpos1)) {
            val list: MutableList<Vec3> = Lists.newArrayList<Vec3>()
            val d2 = this.level().getBlockFloorHeight(blockpos)
            if (DismountHelper.isBlockFloorValid(d2)) {
                list.add(Vec3(d0, blockpos.y.toDouble() + d2, d1))
            }

            val d3 = this.level().getBlockFloorHeight(blockpos1)
            if (DismountHelper.isBlockFloorValid(d3)) {
                list.add(Vec3(d0, blockpos1.y.toDouble() + d3, d1))
            }

            val var14: UnmodifiableIterator<*> = livingEntity.dismountPoses.iterator()

            while (var14.hasNext()) {
                val pose = var14.next() as Pose

                for (vec31 in list) {
                    if (DismountHelper.canDismountTo(this.level(), vec31, livingEntity, pose)) {
                        livingEntity.pose = pose
                        return vec31
                    }
                }
            }
        }

        return super.getDismountLocationForPassenger(livingEntity)
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

    override fun checkFallDamage(y: Double, onGround: Boolean, state: BlockState, pos: BlockPos) {
        this.lastYd = this.deltaMovement.y
        if (!this.isPassenger) {
            if (onGround) {
                if (this.fallDistance > 3.0f) {
                    if (this.status != Status.ON_LAND) {
                        this.resetFallDistance()
                        return
                    }

                    this.causeFallDamage(this.fallDistance, 1.0f, this.damageSources().fall())
                    if (!this.level().isClientSide && !this.isRemoved) {
                        this.kill()
                        if (this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (j in 0..1) {
                                this.spawnAtLocation(Items.STICK)
                            }
                        }
                    }
                }

                this.resetFallDistance()
            } else if (!this.level().getFluidState(this.blockPosition().below()).`is`(FluidTags.WATER) && y < 0.0) {
                this.fallDistance -= y.toFloat()
            }
        }
    }

    private var bubbleTime: Int
        get() = this.entityData.get(DATA_ID_BUBBLE_TIME) as Int
        set(bubbleTime) {
            this.entityData.set(DATA_ID_BUBBLE_TIME, bubbleTime)
        }

    fun getBubbleAngle(partialTicks: Float): Float {
        return Mth.lerp(partialTicks, this.bubbleAngleO, this.bubbleAngle)
    }

    var hurtDir: Int
        get() = this.entityData.get(DATA_ID_HURTDIR) as Int
        set(hurtDirection) {
            this.entityData.set(DATA_ID_HURTDIR, hurtDirection)
        }

    override fun canAddPassenger(passenger: Entity): Boolean {
        return this.passengers.size < this.maxPassengers && !this.isEyeInFluid(FluidTags.WATER)
    }

    protected open val maxPassengers: Int
        get() = 4
    
    private fun getStatus(): Status {
        val raftStatus = this.isUnderwater()
        if (raftStatus != null) {
            this.waterLevel = this.boundingBox.maxY
            return raftStatus
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

    private fun floatRaft() {
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
                if (this.controllingPassenger is Player) {
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

    fun getShipItem(): ItemStack {
        val stack = ItemStack(FSItems.SHIP.get())

        return stack
    }

    //#region Container

    private val dataAccess = object : ContainerData {
        override fun get(index: Int): Int {
            return 0
        }

        override fun set(index: Int, value: Int) {
        }

        override fun getCount(): Int {
            return 2
        }
    }

    protected open fun destroy(damageSource: DamageSource) {
        val stack = getShipItem()
        this.spawnAtLocation(stack)
        this.chestVehicleDestroyed(damageSource, this.level(), this)
    }

    override fun remove(reason: RemovalReason) {
        if (!this.level().isClientSide && reason.shouldDestroy()) {
            Containers.dropContents(this.level(), this, this)
        }
        super.remove(reason)
    }

    override fun setChanged() {
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
        return 54
    }

    override fun getItem(slot: Int): ItemStack {
        return this.getChestVehicleItem(slot)
    }

    override fun removeItem(slot: Int, amount: Int): ItemStack {
        val result = removeChestVehicleItem(slot, amount)
        return result
    }

    override fun removeItemNoUpdate(slot: Int): ItemStack {
        val result = removeChestVehicleItemNoUpdate(slot)
        return result
    }

    override fun setItem(slot: Int, stack: ItemStack) {
        this.setChestVehicleItem(slot, stack)
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
            SynchedEntityData.defineId(CanoeEntity::class.java, EntityDataSerializers.INT)
        private val DATA_ID_HURTDIR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(CanoeEntity::class.java, EntityDataSerializers.INT)
        private val DATA_ID_DAMAGE: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(CanoeEntity::class.java, EntityDataSerializers.FLOAT)
        private val DATA_ID_TYPE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(CanoeEntity::class.java, EntityDataSerializers.INT)
        private val DATA_ID_BUBBLE_TIME: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(CanoeEntity::class.java, EntityDataSerializers.INT)

        private const val TIME_TO_EJECT = 60
        const val BUBBLE_TIME: Int = 60

        fun canVehicleCollide(vehicle: Entity, entity: Entity): Boolean {
            return (entity.canBeCollidedWith() || entity.isPushable) && !vehicle.isPassengerOfSameVehicle(entity)
        }
    }

    enum class Status {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR
    }
}