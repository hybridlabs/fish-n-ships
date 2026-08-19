package dev.hybridlabs.fishnships.client.gui.screen

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.world.inventory.SupplyRaftMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class SupplyRaftScreen(
    menu: SupplyRaftMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<SupplyRaftMenu>(menu, playerInventory, title) {

    init {
        // size of screen in pixels
        imageHeight = 222
        imageWidth = 212
        inventoryLabelY = imageHeight - 94
    }


    override fun init() {
        super.init()
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        val left = (width - imageWidth) / 2
        val top = (height - imageHeight) / 2

        guiGraphics.blit(
            SUPPLY_RAFT_BACKGROUND,
            left,
            top,
            0,
            0,
            imageWidth,
            imageHeight
        )
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    companion object {
        val SUPPLY_RAFT_BACKGROUND: ResourceLocation = CommonClass.locate("textures/gui/container/supply_raft_6row.png")
    }
}