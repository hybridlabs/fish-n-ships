package dev.hybridlabs.fishnships.client.gui.screen

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.world.inventory.SupplyRaftMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory

class SupplyRaftScreen(
    menu: SupplyRaftMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<SupplyRaftMenu>(menu, playerInventory, title, 212, 222) {

    init {
        inventoryLabelY = imageHeight - 94
    }

    override fun extractBackground(
        extractor: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        super.extractBackground(extractor, mouseX, mouseY, partialTick)

        val left = (width - imageWidth) / 2
        val top = (height - imageHeight) / 2

        extractor.blit(
            RenderPipelines.GUI_TEXTURED,
            SUPPLY_RAFT_BACKGROUND,
            left,
            top,
            0f,
            0f,
            imageWidth,
            imageHeight,
            256,
            256
        )
    }

    companion object {
        val SUPPLY_RAFT_BACKGROUND: Identifier = CommonClass.locate("textures/gui/container/supply_raft_6row.png")
    }
}
