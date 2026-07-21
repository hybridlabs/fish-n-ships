package dev.hybridlabs.fishnships.block

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

/**
 * The registry of all blocks in Hybrid Aquatic.
 */
object FSBlocks {

    private fun register(id: String, block: Supplier<Block>): RegistryObject<Block> {
        return CommonClass.BLOCKS.register(id, block)
    }
}
