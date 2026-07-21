package dev.hybridlabs.fishnships.world.inventory

import dev.hybridlabs.fishnships.item.FSItems
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

data class ShipTrawlingNetSlot(
    val shipMenu: ShipMenu,
    val shipContainer: Container,
    val slot: Int,
    val xPos: Int,
    val yPos: Int
): Slot(shipContainer, slot, xPos, yPos) {

    override fun mayPlace(stack: ItemStack): Boolean {
        return stack.`is`(FSItems.TRAWLING_NET.get())
    }

    override fun getMaxStackSize(stack: ItemStack): Int {
        return 1
    }
}
