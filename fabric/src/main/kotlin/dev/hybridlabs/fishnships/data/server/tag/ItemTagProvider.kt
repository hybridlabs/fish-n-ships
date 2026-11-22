package dev.hybridlabs.fishnships.data.server.tag

import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.tag.FSItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.tags.TagAppender
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import java.util.concurrent.CompletableFuture

class ItemTagProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricTagsProvider.ItemTagsProvider(output, registriesFuture) {
    override fun addTags(arg: HolderLookup.Provider) {
        setOf(
            FSItems.OAK_SAILBOAT.get(),
            FSItems.SPRUCE_SAILBOAT.get(),
            FSItems.BIRCH_SAILBOAT.get(),
            FSItems.DARK_OAK_SAILBOAT.get(),
            FSItems.ACACIA_SAILBOAT.get(),
            FSItems.JUNGLE_SAILBOAT.get(),
            FSItems.MANGROVE_SAILBOAT.get(),
            FSItems.CHERRY_SAILBOAT.get(),
            FSItems.CRIMSON_SAILBOAT.get(),
            FSItems.WARPED_SAILBOAT.get(),
            FSItems.OAK_CANOE.get(),
            FSItems.SPRUCE_CANOE.get(),
            FSItems.BIRCH_CANOE.get(),
            FSItems.DARK_OAK_CANOE.get(),
            FSItems.ACACIA_CANOE.get(),
            FSItems.JUNGLE_CANOE.get(),
            FSItems.MANGROVE_CANOE.get(),
            FSItems.CHERRY_CANOE.get(),
            FSItems.CRIMSON_CANOE.get(),
            FSItems.WARPED_CANOE.get(),
            FSItems.CRIMSON_BOAT.get(),
            FSItems.WARPED_BOAT.get(),
        ).forEach { item ->
            valueLookupBuilder(ItemTags.BOATS).add(item)
        }
        
        setOf(
            FSItems.OAK_SAILBOAT_WITH_CHEST.get(),
            FSItems.SPRUCE_SAILBOAT_WITH_CHEST.get(),
            FSItems.BIRCH_SAILBOAT_WITH_CHEST.get(),
            FSItems.DARK_OAK_SAILBOAT_WITH_CHEST.get(),
            FSItems.ACACIA_SAILBOAT_WITH_CHEST.get(),
            FSItems.JUNGLE_SAILBOAT_WITH_CHEST.get(),
            FSItems.MANGROVE_SAILBOAT_WITH_CHEST.get(),
            FSItems.CHERRY_SAILBOAT_WITH_CHEST.get(),
            FSItems.CRIMSON_SAILBOAT_WITH_CHEST.get(),
            FSItems.WARPED_SAILBOAT_WITH_CHEST.get(),
            FSItems.OAK_CANOE_WITH_CHEST.get(),
            FSItems.SPRUCE_CANOE_WITH_CHEST.get(),
            FSItems.BIRCH_CANOE_WITH_CHEST.get(),
            FSItems.DARK_OAK_CANOE_WITH_CHEST.get(),
            FSItems.ACACIA_CANOE_WITH_CHEST.get(),
            FSItems.JUNGLE_CANOE_WITH_CHEST.get(),
            FSItems.MANGROVE_CANOE_WITH_CHEST.get(),
            FSItems.CHERRY_CANOE_WITH_CHEST.get(),
            FSItems.CRIMSON_CANOE_WITH_CHEST.get(),
            FSItems.WARPED_CANOE_WITH_CHEST.get(),
            FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST.get(),
            FSItems.CRIMSON_BOAT_WITH_CHEST.get(),
            FSItems.WARPED_BOAT_WITH_CHEST.get(),
        ).forEach { item ->
            valueLookupBuilder(ItemTags.CHEST_BOATS).add(item)
        }

        builder(FSItemTags.DRIFTWOOD_LOG)
            .addOptionalItem(Identifier.fromNamespaceAndPath("hybrid_aquatic", "driftwood_log"))

        builder(FSItemTags.DRIFTWOOD_PLANKS)
            .addOptionalItem(Identifier.fromNamespaceAndPath("hybrid_aquatic", "driftwood_planks"))

        builder(FSItemTags.DRIFTWOOD_SLAB)
            .addOptionalItem(Identifier.fromNamespaceAndPath("hybrid_aquatic", "driftwood_slab"))
    }

    private fun TagAppender<ResourceKey<Item>, Item>.addOptionalItem(id: Identifier) =
        addOptional(ResourceKey.create(Registries.ITEM, id))
}
