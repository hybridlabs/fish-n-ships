package dev.hybridlabs.fishnships.entity.ship

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
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import software.bernie.geckolib.animatable.GeoEntity

open class SailboatWithChestEntity(
    entityType: EntityType<out SailboatWithChestEntity?>, level: Level,
) :
    SailboatEntity(entityType, level), HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(27, ItemStack.EMPTY)
    private var sailboatLootTable: ResourceLocation? = null
    private var sailboatLootTableSeed: Long = 0

    override val maxPassengers: Int
        get() = 1

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

    override fun getSailboatItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_SAILBOAT_WITH_CHEST.get()
            2 -> item = FSItems.BIRCH_SAILBOAT_WITH_CHEST.get()
            3 -> item = FSItems.JUNGLE_SAILBOAT_WITH_CHEST.get()
            4 -> item = FSItems.ACACIA_SAILBOAT_WITH_CHEST.get()
            5 -> item = FSItems.CHERRY_SAILBOAT_WITH_CHEST.get()
            6 -> item = FSItems.DARK_OAK_SAILBOAT_WITH_CHEST.get()
            7 -> item = FSItems.MANGROVE_SAILBOAT_WITH_CHEST.get()
            8 -> item = FSItems.CRIMSON_SAILBOAT_WITH_CHEST.get()
            9 -> item = FSItems.WARPED_SAILBOAT_WITH_CHEST.get()
            else -> item = FSItems.OAK_SAILBOAT_WITH_CHEST.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getSailboatItem())
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
        val stack = getSailboatItem()
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
        return sailboatLootTable
    }

    override fun setLootTable(id: ResourceLocation?) {
        sailboatLootTable = id
    }

    override fun getLootTableSeed(): Long {
        return sailboatLootTableSeed
    }

    override fun setLootTableSeed(seed: Long) {
        sailboatLootTableSeed = seed
    }

    override fun getItemStacks(): NonNullList<ItemStack> {
        return this.itemStacks
    }

    override fun clearItemStacks() {
        this.itemStacks = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)
    }

    override fun getContainerSize(): Int {
        return 27
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