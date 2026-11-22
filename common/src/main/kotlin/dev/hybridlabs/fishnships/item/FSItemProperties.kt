package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.CommonClass
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

/**
 * Items require their registry id before construction (1.21.5+). Rather than threading the id
 * through every item factory, registration opens a small thread-local scope that
 * [of] reads while the factory builds its properties.
 */
object FSItemProperties {
    private val currentId = ThreadLocal<ResourceKey<Item>?>()

    fun <T> withId(id: String, factory: () -> T): T {
        val previous = currentId.get()
        currentId.set(ResourceKey.create(Registries.ITEM, CommonClass.locate(id)))

        try {
            return factory()
        } finally {
            currentId.set(previous)
        }
    }

    fun of(): Item.Properties {
        val id = currentId.get() ?: error("Item properties created outside an id scope")

        return Item.Properties().setId(id)
    }
}
