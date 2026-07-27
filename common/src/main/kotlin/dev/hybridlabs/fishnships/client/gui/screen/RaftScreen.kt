package dev.hybridlabs.fishnships.client.gui.screen

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.world.inventory.RaftMenu
import dev.hybridlabs.fishnships.world.inventory.ShipMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class RaftScreen(
    menu: RaftMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<RaftMenu>(menu, playerInventory, title) {
    private val raftRows: Int = menu.rowCount

    init {
        // size of screen in pixels
        imageHeight = 222
        imageWidth = 176
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
            RAFT_BACKGROUND,
            left,
            top,
            0,
            0,
            imageWidth,
            imageHeight
        )
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(guiGraphics)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    companion object {
        val RAFT_BACKGROUND: ResourceLocation = CommonClass.locate("textures/gui/container/raft_6row.png")
    }
}