package dev.hybridlabs.fishnships.world.inventory

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ShipTrawlerOutputSlot(
    container: Container,
    index: Int,
    x: Int,
    y: Int
) : Slot(container, index, x, y) {

    override fun mayPlace(stack: ItemStack): Boolean {
        return false
    }

    override fun mayPickup(player: Player): Boolean {
        return true
    }
}