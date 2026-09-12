package dev.hybridlabs.fishnships.entity.vehicle

import dev.hybridlabs.fishnships.item.FSItems
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
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
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity

open class DinghyWithChestEntity(entityType: EntityType<out DinghyWithChestEntity>, level: Level) :
    DinghyEntity(entityType, level), HasCustomInventoryScreen, ContainerEntity,
    GeoEntity {
    private var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(54, ItemStack.EMPTY)
    private var dinghyLootTable: ResourceLocation? = null
    private var dinghyLootTableSeed: Long = 0

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        if (!isAlive) {
            return InteractionResult.PASS
        }

        val result = super.interact(player, hand)
        if (result != InteractionResult.PASS) {
            return result
        }

        if (player.isSecondaryUseActive) {
            val containerResult = interactWithContainerVehicle(player)
            if (containerResult.consumesAction()) {
                gameEvent(GameEvent.CONTAINER_OPEN, player)
            }
            return containerResult
        }

        val stack = player.getItemInHand(hand)

        if (stack.item is AxeItem && !player.isSecondaryUseActive) {
            if (!level().isClientSide) {
                this.alternate = !this.alternate

                if (!player.abilities.instabuild) {
                    stack.hurtAndBreak(1, player) {
                        it.broadcastBreakEvent(hand)
                    }
                }

                playSound(SoundEvents.AXE_STRIP)
            }

            return InteractionResult.sidedSuccess(level().isClientSide)
        }

        return if (outOfControlTicks < 60.0f) {
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
        get() = 3

    override fun getPassengersRidingOffset(): Double {
        return -0.1
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

    override fun getDinghyItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_DINGHY_WITH_CHEST.get()
            2 -> item = FSItems.BIRCH_DINGHY_WITH_CHEST.get()
            3 -> item = FSItems.JUNGLE_DINGHY_WITH_CHEST.get()
            4 -> item = FSItems.ACACIA_DINGHY_WITH_CHEST.get()
            5 -> item = FSItems.CHERRY_DINGHY_WITH_CHEST.get()
            6 -> item = FSItems.DARK_OAK_DINGHY_WITH_CHEST.get()
            7 -> item = FSItems.MANGROVE_DINGHY_WITH_CHEST.get()
            8 -> item = FSItems.CRIMSON_DINGHY_WITH_CHEST.get()
            9 -> item = FSItems.WARPED_DINGHY_WITH_CHEST.get()
            else -> item = FSItems.OAK_DINGHY_WITH_CHEST.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getDinghyItem())
    }

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        if (!hasPassenger(passenger)) {
            return
        }

        val yOffset =
            ((if (isRemoved) 0.01 else passengersRidingOffset) + passenger.myRidingOffset).toFloat()

        val (xOffset, zOffset) = when (passengers.size) {
            0 -> 0.0 to 0.0

            1 -> 0.75 to 0.5
            2 -> 0.75 to -0.5

            else -> 0.0 to 0.0
        }

        val offset = Vec3(xOffset, 0.0, zOffset)
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

    override fun destroy(damageSource: DamageSource) {
        val stack = getDinghyItem()
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
        return dinghyLootTable
    }

    override fun setLootTable(id: ResourceLocation?) {
        if (id != null) dinghyLootTable = id
    }

    override fun getLootTableSeed(): Long {
        return dinghyLootTableSeed
    }

    override fun setLootTableSeed(seed: Long) {
        dinghyLootTableSeed = seed
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