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
        generator.generateFlatItem(FSItems.SHIP.get(), ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(FSItems.ICEBREAKER.get(), ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(FSItems.TRAWLING_NET.get(), ModelTemplates.FLAT_ITEM)
    }
}