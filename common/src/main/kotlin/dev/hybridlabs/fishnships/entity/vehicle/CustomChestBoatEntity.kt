package dev.hybridlabs.fishnships.entity.vehicle

import dev.hybridlabs.fishnships.item.FSItems
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Containers
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.HasCustomInventoryScreen
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.ContainerEntity
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import software.bernie.geckolib.animatable.GeoEntity

open class CustomChestBoatEntity(
    entityType: EntityType<out CustomChestBoatEntity?>, level: Level,
) :
    CustomBoatEntity(entityType, level),
    HasCustomInventoryScreen,
    ContainerEntity,
    GeoEntity {
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(66, ItemStack.EMPTY)
    private var chestBoatLootTable: ResourceLocation? = null
    private var chestBoatLootTableSeed: Long = 0

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

    override val maxPassengers: Int
        get() = 1

    override fun getPassengersRidingOffset(): Double {
        return -0.1
    }

    override fun getBoatItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.WARPED_BOAT_WITH_CHEST.get()
            else -> item = FSItems.CRIMSON_BOAT_WITH_CHEST.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getBoatItem())
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
        val stack = getBoatItem()
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
        return chestBoatLootTable
    }

    override fun setLootTable(id: ResourceLocation?) {
        chestBoatLootTable = id
    }

    override fun getLootTableSeed(): Long {
        return chestBoatLootTableSeed
    }

    override fun setLootTableSeed(seed: Long) {
        chestBoatLootTableSeed = seed
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
            return ChestMenu.threeRows(containerId, playerInventory, this)
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