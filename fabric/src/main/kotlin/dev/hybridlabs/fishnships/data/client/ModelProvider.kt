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
        FSItems.SAILBOAT.get(),
        FSItems.CANOE.get(),
        FSItems.CANOE_WITH_CHEST.get(),
        FSItems.CANOE_WITH_DOUBLE_CHEST.get(),
        FSItems.RAFT.get(),
        FSItems.SUPPLY_RAFT.get(),
        ).forEach { item ->
            generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM)
        }
    }
}