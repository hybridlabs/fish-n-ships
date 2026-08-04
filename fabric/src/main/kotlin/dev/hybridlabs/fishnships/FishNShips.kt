package dev.hybridlabs.fishnships

import dev.hybridlabs.fishnships.Constants.MOD_NAME
import dev.hybridlabs.fishnships.block.FSBlocks
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.item.FSItemGroups
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.network.FSNetworking
import dev.hybridlabs.fishnships.sound.FSSoundEvents
import dev.hybridlabs.fishnships.tag.FSBiomeTags
import dev.hybridlabs.fishnships.tag.FSItemTags
import dev.hybridlabs.fishnships.world.inventory.FSMenuTypes
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger

@Suppress("UnusedExpression")
object FishNShips : ModInitializer {

    private val logger: Logger = Constants.LOG


	override fun onInitialize() {

		logger.info("Initializing $MOD_NAME")

        FSSoundEvents
        FSEntityTypes

        FSBlocks
        FSItems
        FSItemGroups

        FSBiomeTags
        FSItemTags

        FSMenuTypes

        FSNetworking.registerNetworking()
	}
}
