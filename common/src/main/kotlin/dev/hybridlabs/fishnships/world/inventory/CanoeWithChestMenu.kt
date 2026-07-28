package dev.hybridlabs.fishnships.world.inventory

import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack

class CanoeWithChestMenu(type: MenuType<*>, containerId: Int, playerInventory: Inventory, container: Container, rows: Int) :
    AbstractContainerMenu(type, containerId) {
    val container: Container
    val rowCount: Int

    init {
        checkContainerSize(container, rows * SLOTS_PER_ROW)
        this.container = container
        this.rowCount = rows
        container.startOpen(playerInventory.player)

        for (row in 0 until rowCount) {
            for (column in 0 until SLOTS_PER_ROW) {
                addSlot(
                    Slot(
                        container,
                        column + row * SLOTS_PER_ROW,
                        8 + column * 18,
                        18 + row * 18
                    )
                )
            }
        }

        val playerY = 18 + rowCount * 18 + 14

        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(
                    Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        playerY + row * 18
                    )
                )
            }
        }

        for (column in 0 until 9) {
            addSlot(
                Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    playerY + 58
                )
            )
        }
    }

    override fun stillValid(player: Player): Boolean {
        return this.container.stillValid(player)
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = slots[index]

        if (slot.hasItem()) {
            val stack = slot.item
            itemStack = stack.copy()

            val chestSlots = rowCount * SLOTS_PER_ROW

            if (index < chestSlots) {
                if (!moveItemStackTo(stack, chestSlots, slots.size, true))
                    return ItemStack.EMPTY
            } else {
                if (!moveItemStackTo(stack, 0, chestSlots, false))
                    return ItemStack.EMPTY
            }

            if (stack.isEmpty) {
                slot.setByPlayer(ItemStack.EMPTY)
            } else {
                slot.setChanged()
            }
        }

        return itemStack
    }

    override fun removed(player: Player) {
        super.removed(player)
        this.container.stopOpen(player)
    }

    companion object {
        private const val SLOTS_PER_ROW = 9

        fun threeRows(containerId: Int, playerInventory: Inventory): CanoeWithChestMenu {
            return CanoeWithChestMenu(
                FSMenuTypes.CANOE_MENU_3ROW.get(),
                containerId,
                playerInventory,
                SimpleContainer(SLOTS_PER_ROW * 3),
                3
            )
        }

        fun threeRows(
            containerId: Int,
            playerInventory: Inventory,
            container: Container
        ): CanoeWithChestMenu {
            return CanoeWithChestMenu(
                FSMenuTypes.CANOE_MENU_3ROW.get(),
                containerId,
                playerInventory,
                container,
                3
            )
        }
    }
}