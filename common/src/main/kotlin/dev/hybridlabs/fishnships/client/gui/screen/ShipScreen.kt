package dev.hybridlabs.fishnships.client.gui.screen

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.world.inventory.ShipMenu
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class ShipScreen(menu: ShipMenu, playerInventory: Inventory, title: Component) :
    AbstractContainerScreen<ShipMenu>(
        menu, playerInventory,
        title.copy().withStyle(ChatFormatting.WHITE)
    ) {
    private val shipRows: Int = menu.rowCount

    init {
        // size of screen in pixels
        imageHeight = 226
        imageWidth = 182

        inventoryLabelY = imageHeight - 93
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
        val leftDrawPos = (this.width - this.imageWidth) / 2
        val topDrawPos = (this.height - this.imageHeight) / 2

        val topHeight = shipRows * 18 + 71
        val bottomHeight = imageHeight - topHeight

        guiGraphics.blit(
            SHIP_BACKGROUND,
            leftDrawPos,
            topDrawPos,
            16,
            0,
            imageWidth,
            topHeight
        )

        guiGraphics.blit(
            SHIP_BACKGROUND,
            leftDrawPos,
            topDrawPos + topHeight,
            16,
            126,
            imageWidth,
            bottomHeight
        )

        if (this.menu.isLit()) {
            val litProgress = this.menu.getLitProgress()

            guiGraphics.blit(
                SHIP_BACKGROUND,
                leftDrawPos + 30,
                topDrawPos + 60 - litProgress,
                198,
                12 - litProgress,
                14,
                litProgress + 1
            )
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(guiGraphics)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    companion object {
        val SHIP_BACKGROUND: ResourceLocation = CommonClass.locate("textures/gui/container/ship_3row.png")
    }
}