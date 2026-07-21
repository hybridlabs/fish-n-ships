package dev.hybridlabs.fishnships.tag

import dev.hybridlabs.fishnships.Constants
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

object FSBiomeTags {

    val PUFFIN_SPAWN_BIOMES = create("puffin_spawn_biomes")

    private fun create(id: String): TagKey<Biome> {
        return TagKey.create(Registries.BIOME, ResourceLocation(Constants.MOD_ID, id))
    }
}
