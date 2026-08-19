package dev.hybridlabs.fishnships.entity.vehicle

import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.world.inventory.SupplyRaftMenu
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.HasCustomInventoryScreen
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.ContainerEntity
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.storage.loot.LootTable
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

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        if (!isAlive) {
            return InteractionResult.PASS
        }

        val result = super.interact(player, hand)
        if (result != InteractionResult.PASS) {
            return result
        }

        val containerResult = interactWithContainerVehicle(player)
        if (containerResult.consumesAction()) {
            gameEvent(GameEvent.CONTAINER_OPEN, player)
        }

        return containerResult
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

    override fun isPickable(): Boolean {
        return !this.isRemoved
    }

    override fun canAddPassenger(passenger: Entity): Boolean {
        return false
    }

    override val maxPassengers: Int
        get() = 0

    override fun getRaftItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_SUPPLY_RAFT.get()
            2 -> item = FSItems.BIRCH_SUPPLY_RAFT.get()
            3 -> item = FSItems.JUNGLE_SUPPLY_RAFT.get()
            4 -> item = FSItems.ACACIA_SUPPLY_RAFT.get()
            5 -> item = FSItems.CHERRY_SUPPLY_RAFT.get()
            6 -> item = FSItems.DARK_OAK_SUPPLY_RAFT.get()
            7 -> item = FSItems.MANGROVE_SUPPLY_RAFT.get()
            8 -> item = FSItems.CRIMSON_SUPPLY_RAFT.get()
            9 -> item = FSItems.WARPED_SUPPLY_RAFT.get()
            else -> item = FSItems.OAK_SUPPLY_RAFT.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getRaftItem())
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
        val stack = getRaftItem()
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
        if (id != null) raftLootTable = id
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
}