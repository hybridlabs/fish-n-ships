@file:Suppress("UnstableApiUsage")

package dev.hybridlabs.fishnships.tag

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import dev.hybridlabs.hapi.CommonClass

object FSItemTags {
    val DRIFTWOOD_LOG = create("driftwood_log")
    val DRIFTWOOD_PLANKS = create("driftwood_planks")
    val DRIFTWOOD_SLAB = create("driftwood_slab")

    private fun create(id: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, CommonClass.locate(id))
    }

    private fun createConventional(id: String): TagKey<Item> {
		return TagKey.create(Registries.ITEM, ResourceLocation("c", id))
    }
}
