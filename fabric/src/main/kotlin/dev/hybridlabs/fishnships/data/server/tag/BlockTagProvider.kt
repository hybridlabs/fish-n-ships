package dev.hybridlabs.fishnships.data.server.tag

import dev.hybridlabs.fishnships.tag.FSBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import java.util.concurrent.CompletableFuture

class BlockTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun addTags(arg: HolderLookup.Provider) {

        getOrCreateTagBuilder(FSBlockTags.BREAKABLE_ICE)
            .add(Blocks.ICE)
            .add(Blocks.FROSTED_ICE)
            .addOptional(ResourceLocation.fromNamespaceAndPath("ecologics", "thin_ice"))
    }
}