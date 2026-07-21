@file:Suppress("UnstableApiUsage")

package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.Constants
import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem

object  FSItemGroups {

    val FISH_N_SHIPS = register(
        Constants.MOD_ID, CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
        .title(Component.translatable("itemGroup.${Constants.MOD_ID}.spawn_eggs"))
        .icon { ItemStack(FSItems.SHIP.get()) }
        .displayItems { _, entries ->

            entries.accept(FSItems.SHIP.get())
            entries.accept(FSItems.TRAWLING_NET.get())
            entries.accept(FSItems.ICEBREAKER.get())

            BuiltInRegistries.ITEM.forEach { item ->
                val id = BuiltInRegistries.ITEM.getKey(item)
                if (id.namespace != Constants.MOD_ID) {
                    return@forEach
                }
                if (item is SpawnEggItem) {
                    entries.accept(item)
                }
            }
        }
        .build()
    )

    private fun register(id: String, itemGroup: CreativeModeTab): RegistryObject<CreativeModeTab> {
        return CommonClass.CREATIVE_MODE_TABS.register(id) { itemGroup }
    }
}