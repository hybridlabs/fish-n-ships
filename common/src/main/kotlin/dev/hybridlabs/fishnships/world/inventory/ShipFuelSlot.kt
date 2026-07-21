package dev.hybridlabs.fishnships.world.inventory

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

data class ShipFuelSlot(
    val shipMenu: ShipMenu,
    val shipContainer: Container,
    val slot: Int,
    val xPos: Int,
    val yPos: Int
): Slot(shipContainer, slot, xPos, yPos) {

    override fun mayPlace(stack: ItemStack): Boolean {
        return this.shipMenu.isFuel(stack) || isBucket(stack)
    }

    override fun getMaxStackSize(stack: ItemStack): Int {
        return if (isBucket(stack)) 1 else super.getMaxStackSize(stack)
    }

    fun isBucket(stack: ItemStack): Boolean {
        return stack.`is`(Items.BUCKET)
    }
}
