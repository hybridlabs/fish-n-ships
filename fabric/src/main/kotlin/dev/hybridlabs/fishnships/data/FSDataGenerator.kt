package dev.hybridlabs.fishnships.data

import dev.hybridlabs.fishnships.Constants
import dev.hybridlabs.fishnships.data.client.LanguageProvider
import dev.hybridlabs.fishnships.data.client.ModelProvider
import dev.hybridlabs.fishnships.data.client.SoundProvider
import dev.hybridlabs.fishnships.data.server.RecipeProvider
import dev.hybridlabs.fishnships.data.server.loot.EntityTypeLootTableProvider
import dev.hybridlabs.fishnships.data.server.loot.GenericLootTableProvider
import dev.hybridlabs.fishnships.data.server.tag.BiomeTagProvider
import dev.hybridlabs.fishnships.data.server.tag.ItemTagProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.Registry

object FSDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		val pack = generator.createPack()
		pack.addProvider(::LanguageProvider)
		pack.addProvider(::ModelProvider)
        pack.addProvider(::SoundProvider)
		pack.addProvider(::EntityTypeLootTableProvider)
		pack.addProvider(::GenericLootTableProvider)
		pack.addProvider(::BiomeTagProvider)
		pack.addProvider(::ItemTagProvider)
		pack.addProvider(::RecipeProvider)
	}

	fun <T> filterFishNShips(registry: Registry<T>): (T) -> Boolean {
		return { o ->
			val id = registry.getKey(o)
			id?.namespace == Constants.MOD_ID
		}
	}
}
