package dev.hybridlabs.fishnships

import dev.hybridlabs.fishnships.client.gui.screen.FSMenuScreens
import dev.hybridlabs.fishnships.client.render.entity.FSEntityRenderers
import net.fabricmc.api.ClientModInitializer

@Suppress("UnusedExpression", "DEPRECATION")
object FishNShipsClient : ClientModInitializer {
	override fun onInitializeClient() {
        FSMenuScreens
        FSEntityRenderers
	}
}
