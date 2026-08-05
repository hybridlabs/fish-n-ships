package dev.hybridlabs.fishnships

import dev.hybridlabs.fishnships.client.gui.screen.FSMenuScreens
import dev.hybridlabs.fishnships.client.render.entity.FSEntityRenderers
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.item.FSItemGroups
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.network.FSNetworking
import dev.hybridlabs.fishnships.tag.FSItemTags
import dev.hybridlabs.fishnships.world.inventory.FSMenuTypes
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Suppress("UnusedExpression")
@Mod(Constants.MOD_ID)
object FishNShipsForge {
    private val LOGGER = Constants.LOG

    init {
        CommonClass.init()

        FSEntityTypes
        FSMenuTypes

        FSItems
        FSItemGroups

        FSItemTags

        FSNetworking.registerPackets()

        runForDist(
            clientTarget = {
                FSEntityRenderers
                MOD_BUS.addListener(FishNShipsForge::onClientSetup)
            },
            serverTarget = {
                MOD_BUS.addListener(FishNShipsForge::onServerSetup)
            }
        )
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.info("Initializing client...")

        FSMenuScreens
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.info("Server starting...")
    }
}
