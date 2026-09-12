@file:Suppress("UnstableApiUsage")

package dev.hybridlabs.fishnships.tag

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import dev.hybridlabs.hapi.CommonClass
import net.minecraft.world.level.block.Block

object FSBlockTags {
    val BREAKABLE_ICE = create("breakable_ice")

    private fun create(id: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, CommonClass.locate(id))
    }

    private fun createConventional(id: String): TagKey<Block> {
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", id))
    }
}
