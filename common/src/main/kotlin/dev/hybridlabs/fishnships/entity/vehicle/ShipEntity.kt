package dev.hybridlabs.fishnships.entity.vehicle

import com.mojang.serialization.Codec
import dev.hybridlabs.fishnships.Constants
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.platform.Services
import dev.hybridlabs.fishnships.world.inventory.ShipMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
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
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationController.AnimationStateHandler
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import java.util.function.IntFunction
import kotlin.math.min

open class ShipEntity(
    type: EntityType<out ShipEntity>,
    world: Level,
) :
    BaseBoatEntity(type, world), PlayerRideable, HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(42, ItemStack.EMPTY)
    private var shipLootTable: ResourceLocation? = null
    private var shipLootTableSeed: Long = 0
    private var inputLeft = false
    private var inputRight = false
    private var inputUp = false
    private var inputDown = false
    private var inputJumping = false
    private var inputSprint = false
    private var lastJumpInput = false
    private var litTime = 0
    private var litDuration = 0
    private val body: ShipCabinPart = ShipCabinPart(this, "body", 5.0f, 2.0f)
    private val cabin: ShipCabinPart = ShipCabinPart(this, "cabin", 2.0f, 3.0f)
    private val subEntities: Array<ShipCabinPart> = arrayOf(body, cabin)
    private val trawlingInterval = 100
    private val trawlingChance = 0.5
    private val trawlingSlots = 3..14
    private var moving = false

    init {
        noCulling = true
    }

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
        super.defineSynchedData()
        this.entityData.define(DATA_ID_RIGHT_PROPELLER, false)
        this.entityData.define(DATA_ID_LEFT_PROPELLER, false)
        this.entityData.define(SAIL_COLOR, FlagColor.NONE.id)
        this.entityData.define(IS_BURNING, false)
        this.entityData.define(HAS_TRAWLING_NET, false)
        this.entityData.define(IS_TRAWLING, false)
        this.entityData.define(HAS_ICEBREAKER, false)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putString("FlagColor", this.getFlagColor().serializedName)
        tag.putInt("BurnTime", this.litTime)
        this.addChestVehicleSaveData(tag)
        tag.putBoolean("HasIceBreaker", this.hasIceBreaker())
        tag.putBoolean("HasTrawlingNet", this.hasTrawlingNet())
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        setHasTrawlingNet(tag.getBoolean("HasTrawlingNet"))
        setHasIceBreaker(tag.getBoolean("HasIceBreaker"))

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

    fun setHasTrawlingNet(value: Boolean) {
        this.entityData.set(HAS_TRAWLING_NET, value)
    }

    fun hasTrawlingNet(): Boolean {
        return entityData.get(HAS_TRAWLING_NET)
    }

    fun setTrawling(value: Boolean) {
        Constants.LOG.debug("Set trawling: {}", value)
        this.entityData.set(IS_TRAWLING, value)
    }

    fun isTrawling(): Boolean {
        return entityData.get(IS_TRAWLING)
    }

    fun didMove(): Boolean {
        return this.deltaMovement.horizontalDistanceSqr() > 0.01
    }

    fun setMoving(moving:Boolean){
        this.moving= moving
    }

    fun canTrawl(): Boolean {
        return hasTrawlingNet() &&
                isTrawling() &&
                isLit() &&
                !getAvailableTrawlSlots().isEmpty() &&
                moving &&
                controllingPassenger is Player
    }

    fun hasIceBreaker(): Boolean {
        return entityData.get(HAS_ICEBREAKER)
    }

    fun setHasIceBreaker(value: Boolean) {
        this.entityData.set(HAS_ICEBREAKER, value)
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

                        if (!level().isClientSide) {
                            (level() as ServerLevel).sendParticles(
                                BlockParticleOption(ParticleTypes.BLOCK, state),
                                pos.x + 0.5,
                                pos.y + 0.5,
                                pos.z + 0.5,
                                20,          // particle count
                                0.3, 0.3, 0.3,
                                0.05
                            )

                            level().playSound(
                                null,
                                pos,
                                state.soundType.breakSound,
                                SoundSource.BLOCKS,
                                1.0f,
                                0.9f + random.nextFloat() * 0.2f
                            )
                        }

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

    private fun tickPart(part: ShipCabinPart, offsetX: Double, offsetY: Double, offsetZ: Double) {
        part.setPos(this.x + offsetX, this.y + offsetY, this.z + offsetZ)
    }

    fun getSubEntities(): Array<ShipCabinPart> {
        return this.subEntities
    }

    override fun recreateFromPacket(packet: ClientboundAddEntityPacket) {
        super.recreateFromPacket(packet)
        val shippart: Array<ShipCabinPart> = this.getSubEntities()

        for (i in shippart.indices) {
            shippart[i].id = i + packet.id
        }
    }

    override fun tick() {
        super.tick()

        tickPart(
            body,
            0.0,
            0.0,
            0.0
        )

        tickPart(
            cabin,
            0.0,
            2.0,
            0.0
        )

        burnTick()

        if (!this.level().isClientSide && this.outOfControlTicks >= 60.0f) {
            this.ejectPassengers()
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1)
        }

        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f)
        }

        if (this.isControlledByLocalInstance) {
            if (this.firstPassenger !is Player) {
                setPropellerState(left = false, right = false)
            }

            if (this.level().isClientSide) {
                if (isLit()) controlShip()

                this.level().sendPacketToServer(
                    ServerboundPaddleBoatPacket(
                        getPropellerState(0),
                        getPropellerState(1)
                    )
                )
            }

        } else {
            this.deltaMovement = Vec3.ZERO
        }
        this.move(MoverType.SELF, this.deltaMovement)
        if (level().isClientSide){
            Services.PLATFORM.sendMovementToServer(didMove())
        }

        tickTrawling()

        this.checkInsideBlocks()

        if (!level().isClientSide && hasIceBreaker()) {
            breakIce()
        }
    }

    private fun tickTrawling() {
        if (level().isClientSide
            || (level().gameTime.toInt() % trawlingInterval) != 0
            || !canTrawl()
            || random.nextFloat() > trawlingChance
        ) return

        val lootTable = server?.lootData?.getLootTable(BuiltInLootTables.FISHING) ?: return

        val lootParamsBuilder = LootParams.Builder(level() as ServerLevel)
            .withParameter(LootContextParams.ORIGIN, position())

        val loot = lootTable.getRandomItems(
            lootParamsBuilder.create(LootContextParamSets.CHEST),
            lootTableSeed
        )

        val openSlots = getAvailableTrawlSlots()
        val occupiedSlots = getOccupiedTrawlSlots()

        var trawlSuccess = false

        loot.forEach {
            for (slot in occupiedSlots) {
                val existingStack = itemStacks[slot]
                if (existingStack.item == it.item && existingStack.count < existingStack.maxStackSize) {
                    val capacity = existingStack.maxStackSize - existingStack.count
                    val moveCount = min(it.count, capacity)
                    it.shrink(moveCount)
                    existingStack.grow(moveCount)

                    if (moveCount > 0) {
                        trawlSuccess = true
                    }

                    if (it.isEmpty) return@forEach
                }
            }

            if (openSlots.isEmpty()) return@forEach

            val slot = openSlots.removeFirst()
            itemStacks[slot] = it
            occupiedSlots.add(slot)
            trawlSuccess = true
        }

        if (trawlSuccess) {
            level().playSound(
                null,
                blockPosition(),
                SoundEvents.FISHING_BOBBER_SPLASH,
                SoundSource.AMBIENT,
                0.8f,
                0.9f + random.nextFloat() * 0.2f
            )
        }
    }

    private fun getAvailableTrawlSlots(): MutableList<Int> {
        val availableSlots = ArrayList<Int>()
        for (slot in trawlingSlots) {
            if (itemStacks[slot].isEmpty)
                availableSlots.add(slot)
        }
        return availableSlots
    }

    private fun getOccupiedTrawlSlots(): MutableList<Int> {
        val occupiedSlots = ArrayList<Int>()
        for (slot in trawlingSlots) {
            if (!itemStacks[slot].isEmpty)
                occupiedSlots.add(slot)
        }
        return occupiedSlots
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
                f += 0.0025f
            }

            this.yRot += this.deltaRotation
            if (this.inputUp) {
                f += 0.025f
            }

            if (this.inputDown) {
                f -= 0.0025f
            }

            if (inputJumping && !lastJumpInput) {
                Services.PLATFORM.sendTrawlingToServer(!isTrawling())
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
        if (source.entity != null && this.hasPassenger(source.entity!!)) {
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
            if (flag || this.getDamage() > 100.0f) {
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

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        super.registerControllers(controllers)
        controllers.add(
            AnimationController(
                this, "Trawling",
                AnimationStateHandler { state: AnimationState<ShipEntity> ->
                    if (this.isTrawling())
                        return@AnimationStateHandler state.setAndContinue(TRAWL_ON_ANIMATION)
                    else return@AnimationStateHandler state.setAndContinue(TRAWL_OFF_ANIMATION)
                }
            )
        )
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
        return 42
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
        entityData.set(HAS_ICEBREAKER, itemStacks[2].`is`(FSItems.ICEBREAKER.get()))
    }

    private fun updateTrawlingNet() {
        entityData.set(HAS_TRAWLING_NET, itemStacks[1].`is`(FSItems.TRAWLING_NET.get()))
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

        val TRAWL_ON_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("misc.trawl_on")
        val TRAWL_OFF_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("misc.trawl_off")
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(FSItems.SHIP.get())
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