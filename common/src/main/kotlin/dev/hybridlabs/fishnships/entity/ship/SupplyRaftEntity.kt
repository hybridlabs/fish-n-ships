package dev.hybridlabs.fishnships.entity.ship

import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.world.inventory.SupplyRaftMenu
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import software.bernie.geckolib.animatable.GeoEntity

open class SupplyRaftEntity(
    type: EntityType<out SupplyRaftEntity>,
    world: Level,
) :
    RaftEntity(type, world), HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(66, ItemStack.EMPTY)
    private var raftLootTable: ResourceLocation? = null
    private var raftLootTableSeed: Long = 0

    init {
        noCulling = true
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        if (!isAlive) {
            return InteractionResult.PASS
        }

        if (getLeashHolder() === player) {
            dropLeash(true, !player.abilities.instabuild)
            gameEvent(GameEvent.ENTITY_INTERACT, player)
            return InteractionResult.sidedSuccess(level().isClientSide)
        }

        val result = checkAndHandleImportantInteractions(player, hand)
        if (result.consumesAction()) {
            gameEvent(GameEvent.ENTITY_INTERACT, player)
            return result
        }

        if (player.isSecondaryUseActive) {
            val containerResult = interactWithContainerVehicle(player)
            if (containerResult.consumesAction()) {
                gameEvent(GameEvent.CONTAINER_OPEN, player)
            }
            return containerResult
        }

        return InteractionResult.PASS
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

    override fun defineSynchedData() {
        super.defineSynchedData()
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        this.addChestVehicleSaveData(tag)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        setDamage(tag.getFloat("Damage"))
        this.readChestVehicleSaveData(tag)
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

    override fun isPickable(): Boolean {
        return !this.isRemoved
    }

    override fun canAddPassenger(passenger: Entity): Boolean {
        return false
    }

    override val maxPassengers: Int
        get() = 0

    fun getSupplyRaftItem(): ItemStack {
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

    override fun destroy(damageSource: DamageSource) {
        val stack = getSupplyRaftItem()
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
        return raftLootTable
    }

    override fun setLootTable(id: ResourceLocation?) {
        raftLootTable = id
    }

    override fun getLootTableSeed(): Long {
        return raftLootTableSeed
    }

    override fun setLootTableSeed(seed: Long) {
        raftLootTableSeed = seed
    }

    override fun getItemStacks(): NonNullList<ItemStack> {
        return this.itemStacks
    }

    override fun clearItemStacks() {
        this.itemStacks = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)
    }

    override fun getContainerSize(): Int {
        return 66
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
            return SupplyRaftMenu.sixRows(containerId, playerInventory, this)
        }
    }

    fun unpackLootTable(player: Player?) {
        this.unpackChestVehicleLootTable(player)
    }

    override fun stopOpen(player: Player) {
        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(player))
    }
    //#endregion

    override fun getPickResult(): ItemStack? {
        return ItemStack(FSItems.SUPPLY_RAFT.get())
    }
}