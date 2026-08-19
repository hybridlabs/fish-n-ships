package dev.hybridlabs.fishnships.data.client

import dev.hybridlabs.fishnships.item.FSItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates

class ModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockModelGenerators) {
    }

    override fun generateItemModels(generator: ItemModelGenerators) {
        setOf(
        FSItems.SHIP.get(),
        FSItems.ICEBREAKER.get(),
        FSItems.TRAWLING_NET.get(),
            
        FSItems.OAK_SAILBOAT.get(),
        FSItems.OAK_SAILBOAT_WITH_CHEST.get(),
        FSItems.OAK_CANOE.get(),
        FSItems.OAK_CANOE_WITH_CHEST.get(),
        FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.OAK_RAFT.get(),
        FSItems.OAK_SUPPLY_RAFT.get(),
        FSItems.OAK_DINGHY.get(),
        FSItems.OAK_DINGHY_WITH_CHEST.get(),

        FSItems.SPRUCE_SAILBOAT.get(),
        FSItems.SPRUCE_SAILBOAT_WITH_CHEST.get(),
        FSItems.SPRUCE_CANOE.get(),
        FSItems.SPRUCE_CANOE_WITH_CHEST.get(),
        FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.SPRUCE_RAFT.get(),
        FSItems.SPRUCE_SUPPLY_RAFT.get(),
        FSItems.SPRUCE_DINGHY.get(),
        FSItems.SPRUCE_DINGHY_WITH_CHEST.get(),

        FSItems.BIRCH_SAILBOAT.get(),
        FSItems.BIRCH_SAILBOAT_WITH_CHEST.get(),
        FSItems.BIRCH_CANOE.get(),
        FSItems.BIRCH_CANOE_WITH_CHEST.get(),
        FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.BIRCH_RAFT.get(),
        FSItems.BIRCH_SUPPLY_RAFT.get(),
        FSItems.BIRCH_DINGHY.get(),
        FSItems.BIRCH_DINGHY_WITH_CHEST.get(),

        FSItems.JUNGLE_SAILBOAT.get(),
        FSItems.JUNGLE_SAILBOAT_WITH_CHEST.get(),
        FSItems.JUNGLE_CANOE.get(),
        FSItems.JUNGLE_CANOE_WITH_CHEST.get(),
        FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.JUNGLE_RAFT.get(),
        FSItems.JUNGLE_SUPPLY_RAFT.get(),
        FSItems.JUNGLE_DINGHY.get(),
        FSItems.JUNGLE_DINGHY_WITH_CHEST.get(),

        FSItems.ACACIA_SAILBOAT.get(),
        FSItems.ACACIA_SAILBOAT_WITH_CHEST.get(),
        FSItems.ACACIA_CANOE.get(),
        FSItems.ACACIA_CANOE_WITH_CHEST.get(),
        FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.ACACIA_RAFT.get(),
        FSItems.ACACIA_SUPPLY_RAFT.get(),
        FSItems.ACACIA_DINGHY.get(),
        FSItems.ACACIA_DINGHY_WITH_CHEST.get(),

        FSItems.DARK_OAK_SAILBOAT.get(),
        FSItems.DARK_OAK_SAILBOAT_WITH_CHEST.get(),
        FSItems.DARK_OAK_CANOE.get(),
        FSItems.DARK_OAK_CANOE_WITH_CHEST.get(),
        FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.DARK_OAK_RAFT.get(),
        FSItems.DARK_OAK_SUPPLY_RAFT.get(),
        FSItems.DARK_OAK_DINGHY.get(),
        FSItems.DARK_OAK_DINGHY_WITH_CHEST.get(),

        FSItems.MANGROVE_SAILBOAT.get(),
        FSItems.MANGROVE_SAILBOAT_WITH_CHEST.get(),
        FSItems.MANGROVE_CANOE.get(),
        FSItems.MANGROVE_CANOE_WITH_CHEST.get(),
        FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.MANGROVE_RAFT.get(),
        FSItems.MANGROVE_SUPPLY_RAFT.get(),
        FSItems.MANGROVE_DINGHY.get(),
        FSItems.MANGROVE_DINGHY_WITH_CHEST.get(),
            
        FSItems.CHERRY_SAILBOAT.get(),
        FSItems.CHERRY_SAILBOAT_WITH_CHEST.get(),
        FSItems.CHERRY_CANOE.get(),
        FSItems.CHERRY_CANOE_WITH_CHEST.get(),
        FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.CHERRY_RAFT.get(),
        FSItems.CHERRY_SUPPLY_RAFT.get(),
        FSItems.CHERRY_DINGHY.get(),
        FSItems.CHERRY_DINGHY_WITH_CHEST.get(),
            
        FSItems.CRIMSON_BOAT.get(),
        FSItems.CRIMSON_BOAT_WITH_CHEST.get(),
        FSItems.CRIMSON_SAILBOAT.get(),
        FSItems.CRIMSON_SAILBOAT_WITH_CHEST.get(),
        FSItems.CRIMSON_CANOE.get(),
        FSItems.CRIMSON_CANOE_WITH_CHEST.get(),
        FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.CRIMSON_RAFT.get(),
        FSItems.CRIMSON_SUPPLY_RAFT.get(),
        FSItems.CRIMSON_DINGHY.get(),
        FSItems.CRIMSON_DINGHY_WITH_CHEST.get(),
            
        FSItems.WARPED_BOAT.get(),
        FSItems.WARPED_BOAT_WITH_CHEST.get(),
        FSItems.WARPED_SAILBOAT.get(),
        FSItems.WARPED_SAILBOAT_WITH_CHEST.get(),
        FSItems.WARPED_CANOE.get(),
        FSItems.WARPED_CANOE_WITH_CHEST.get(),
        FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.WARPED_RAFT.get(),
        FSItems.WARPED_SUPPLY_RAFT.get(),
        FSItems.WARPED_DINGHY.get(),
        FSItems.WARPED_DINGHY_WITH_CHEST.get(),
            
        FSItems.DRIFTWOOD_BOAT.get(),
        FSItems.DRIFTWOOD_BOAT_WITH_CHEST.get(),
        FSItems.DRIFTWOOD_SAILBOAT.get(),
        FSItems.DRIFTWOOD_SAILBOAT_WITH_CHEST.get(),
        FSItems.DRIFTWOOD_CANOE.get(),
        FSItems.DRIFTWOOD_CANOE_WITH_CHEST.get(),
        FSItems.DRIFTWOOD_CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.DRIFTWOOD_RAFT.get(),
        FSItems.DRIFTWOOD_SUPPLY_RAFT.get(),
        FSItems.DRIFTWOOD_DINGHY.get(),
        FSItems.DRIFTWOOD_DINGHY_WITH_CHEST.get(),
            
        ).forEach { item ->
            generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM)
        }
    }
}