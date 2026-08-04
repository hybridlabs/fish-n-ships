package dev.hybridlabs.fishnships.entity.vehicle

import dev.hybridlabs.fishnships.item.FSItems
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Containers
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
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
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity

open class CanoeWithDoubleChestEntity(entityType: EntityType<out CanoeWithDoubleChestEntity>, level: Level) :
    CanoeEntity(entityType, level), HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(54, ItemStack.EMPTY)
    private var canoeLootTable: ResourceLocation? = null
    private var canoeLootTableSeed: Long = 0

    override val maxPassengers: Int
        get() = 1

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        if (!hasPassenger(passenger)) {
            return
        }

        val yOffset =
            ((if (isRemoved) 0.01 else passengersRidingOffset) + passenger.myRidingOffset).toFloat()

        val offset = Vec3(0.5, 0.0, 0.0)
            .yRot(-yRot * (Math.PI.toFloat() / 180f) - (Math.PI.toFloat() / 2f))

        callback.accept(
            passenger,
            x + offset.x,
            y + yOffset,
            z + offset.z
        )

        passenger.yRot += deltaRotation
        passenger.yHeadRot += deltaRotation
        clampRotation(passenger)
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

    override fun getCanoeItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST.get()
            2 -> item = FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST.get()
            3 -> item = FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST.get()
            4 -> item = FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST.get()
            5 -> item = FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST.get()
            6 -> item = FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST.get()
            7 -> item = FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST.get()
            8 -> item = FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST.get()
            9 -> item = FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST.get()
            else -> item = FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getCanoeItem())
    }

    override fun destroy(damageSource: DamageSource) {
        val stack = getCanoeItem()
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
        return canoeLootTable
    }

    override fun setLootTable(id: ResourceLocation?) {
        canoeLootTable = id
    }

    override fun getLootTableSeed(): Long {
        return canoeLootTableSeed
    }

    override fun setLootTableSeed(seed: Long) {
        canoeLootTableSeed = seed
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
            return ChestMenu.sixRows(containerId, playerInventory, this)
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