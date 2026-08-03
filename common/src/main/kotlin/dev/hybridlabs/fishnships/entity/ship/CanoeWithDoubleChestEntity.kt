package dev.hybridlabs.fishnships.entity.ship

import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.world.inventory.CanoeWithDoubleChestMenu
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
import net.minecraft.world.inventory.ContainerData
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

    private fun getCanoeWithDoubleChestItem(): ItemStack {
        val stack = ItemStack(FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get())

        return stack
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

    override fun destroy(damageSource: DamageSource) {
        val stack = getCanoeWithDoubleChestItem()
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
            return CanoeWithDoubleChestMenu.sixRows(containerId, playerInventory, this)
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
        return ItemStack(FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get())
    }
}