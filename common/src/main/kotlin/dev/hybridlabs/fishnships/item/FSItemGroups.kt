@file:Suppress("UnstableApiUsage")

package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.Constants
import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.platform.Services
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.SpawnEggItem

object  FSItemGroups {

    val FISH_N_SHIPS = register(
        Constants.MOD_ID, CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
        .title(Component.translatable("itemGroup.${Constants.MOD_ID}.main"))
        .icon { ItemStack(FSItems.SHIP.get()) }
        .displayItems { _, entries ->

            entries.accept(Items.OAK_BOAT)
            entries.accept(Items.OAK_CHEST_BOAT)
            entries.accept(FSItems.OAK_SAILBOAT.get())
            entries.accept(FSItems.OAK_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.OAK_CANOE.get())
            entries.accept(FSItems.OAK_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.OAK_RAFT.get())
            entries.accept(FSItems.OAK_SUPPLY_RAFT.get())

            entries.accept(Items.SPRUCE_BOAT)
            entries.accept(Items.SPRUCE_CHEST_BOAT)
            entries.accept(FSItems.SPRUCE_SAILBOAT.get())
            entries.accept(FSItems.SPRUCE_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.SPRUCE_CANOE.get())
            entries.accept(FSItems.SPRUCE_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.SPRUCE_RAFT.get())
            entries.accept(FSItems.SPRUCE_SUPPLY_RAFT.get())

            entries.accept(Items.BIRCH_BOAT)
            entries.accept(Items.BIRCH_CHEST_BOAT)
            entries.accept(FSItems.BIRCH_SAILBOAT.get())
            entries.accept(FSItems.BIRCH_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.BIRCH_CANOE.get())
            entries.accept(FSItems.BIRCH_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.BIRCH_RAFT.get())
            entries.accept(FSItems.BIRCH_SUPPLY_RAFT.get())

            entries.accept(Items.JUNGLE_BOAT)
            entries.accept(Items.JUNGLE_CHEST_BOAT)
            entries.accept(FSItems.JUNGLE_SAILBOAT.get())
            entries.accept(FSItems.JUNGLE_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.JUNGLE_CANOE.get())
            entries.accept(FSItems.JUNGLE_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.JUNGLE_RAFT.get())
            entries.accept(FSItems.JUNGLE_SUPPLY_RAFT.get())

            entries.accept(Items.ACACIA_BOAT)
            entries.accept(Items.ACACIA_CHEST_BOAT)
            entries.accept(FSItems.ACACIA_SAILBOAT.get())
            entries.accept(FSItems.ACACIA_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.ACACIA_CANOE.get())
            entries.accept(FSItems.ACACIA_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.ACACIA_RAFT.get())
            entries.accept(FSItems.ACACIA_SUPPLY_RAFT.get())

            entries.accept(Items.DARK_OAK_BOAT)
            entries.accept(Items.DARK_OAK_CHEST_BOAT)
            entries.accept(FSItems.DARK_OAK_SAILBOAT.get())
            entries.accept(FSItems.DARK_OAK_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.DARK_OAK_CANOE.get())
            entries.accept(FSItems.DARK_OAK_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.DARK_OAK_RAFT.get())
            entries.accept(FSItems.DARK_OAK_SUPPLY_RAFT.get())

            entries.accept(Items.MANGROVE_BOAT)
            entries.accept(Items.MANGROVE_CHEST_BOAT)
            entries.accept(FSItems.MANGROVE_SAILBOAT.get())
            entries.accept(FSItems.MANGROVE_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.MANGROVE_CANOE.get())
            entries.accept(FSItems.MANGROVE_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.MANGROVE_RAFT.get())
            entries.accept(FSItems.MANGROVE_SUPPLY_RAFT.get())

            entries.accept(Items.CHERRY_BOAT)
            entries.accept(Items.CHERRY_CHEST_BOAT)
            entries.accept(FSItems.CHERRY_SAILBOAT.get())
            entries.accept(FSItems.CHERRY_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.CHERRY_CANOE.get())
            entries.accept(FSItems.CHERRY_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.CHERRY_RAFT.get())
            entries.accept(FSItems.CHERRY_SUPPLY_RAFT.get())

            entries.accept(FSItems.CRIMSON_BOAT.get())
            entries.accept(FSItems.CRIMSON_BOAT_WITH_CHEST.get())
            entries.accept(FSItems.CRIMSON_SAILBOAT.get())
            entries.accept(FSItems.CRIMSON_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.CRIMSON_CANOE.get())
            entries.accept(FSItems.CRIMSON_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.CRIMSON_RAFT.get())
            entries.accept(FSItems.CRIMSON_SUPPLY_RAFT.get())

            entries.accept(FSItems.WARPED_BOAT.get())
            entries.accept(FSItems.WARPED_BOAT_WITH_CHEST.get())
            entries.accept(FSItems.WARPED_SAILBOAT.get())
            entries.accept(FSItems.WARPED_SAILBOAT_WITH_CHEST.get())
            entries.accept(FSItems.WARPED_CANOE.get())
            entries.accept(FSItems.WARPED_CANOE_WITH_CHEST.get())
            entries.accept(FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST.get())
            entries.accept(FSItems.WARPED_RAFT.get())
            entries.accept(FSItems.WARPED_SUPPLY_RAFT.get())

            if (Services.PLATFORM.isModLoaded("hybrid_aquatic")) {
                entries.accept(FSItems.DRIFTWOOD_BOAT.get())
                entries.accept(FSItems.DRIFTWOOD_BOAT_WITH_CHEST.get())
                entries.accept(FSItems.DRIFTWOOD_SAILBOAT.get())
                entries.accept(FSItems.DRIFTWOOD_SAILBOAT_WITH_CHEST.get())
                entries.accept(FSItems.DRIFTWOOD_CANOE.get())
                entries.accept(FSItems.DRIFTWOOD_CANOE_WITH_CHEST.get())
                entries.accept(FSItems.DRIFTWOOD_CANOE_WITH_DOUBLE_CHEST.get())
                entries.accept(FSItems.DRIFTWOOD_RAFT.get())
                entries.accept(FSItems.DRIFTWOOD_SUPPLY_RAFT.get())
            }

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