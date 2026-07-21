@file:Suppress("UnstableApiUsage")

package dev.hybridlabs.fishnships.tag

import dev.hybridlabs.fishnships.Constants
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item


object FSItemTags {
    val TURDUCKEN_INGREDIENTS = create("turducken_ingredients")

    private fun create(id: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, ResourceLocation(Constants.MOD_ID, id))
    }
}
