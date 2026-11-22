package dev.hybridlabs.fishnships.client.gui.screen

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.world.inventory.ShipMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory

class ShipScreen(menu: ShipMenu, playerInventory: Inventory, title: Component) :
    AbstractContainerScreen<ShipMenu>(menu, playerInventory, title, 182, 226) {
    private val shipRows: Int = menu.rowCount

    init {
        inventoryLabelY = imageHeight - 93
    }

    override fun extractBackground(
        extractor: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        super.extractBackground(extractor, mouseX, mouseY, partialTick)

        val leftDrawPos = (this.width - this.imageWidth) / 2
        val topDrawPos = (this.height - this.imageHeight) / 2
        extractor.blit(
            RenderPipelines.GUI_TEXTURED,
            SHIP_BACKGROUND,
            leftDrawPos,
            topDrawPos,
            16f,
            0f,
            imageWidth,
            shipRows * 18 + 71,
            256,
            256
        )

        extractor.blit(
            RenderPipelines.GUI_TEXTURED,
            SHIP_BACKGROUND,
            leftDrawPos,
            topDrawPos + shipRows * 18 + 71,
            16f,
            126f,
            imageWidth,
            226 - shipRows * 18 + 71,
            256,
            256
        )

        if (this.menu.isLit()) {
            val litProgress = this.menu.getLitProgress()
            extractor.blit(
                RenderPipelines.GUI_TEXTURED,
                SHIP_BACKGROUND,
                leftDrawPos + 30,
                topDrawPos + 60 - litProgress,
                198f,
                (12 - litProgress).toFloat(),
                14,
                litProgress + 1,
                256,
                256
            )
        }
    }

    companion object {
        val SHIP_BACKGROUND: Identifier = CommonClass.locate("textures/gui/container/ship_3row.png")
    }
}
