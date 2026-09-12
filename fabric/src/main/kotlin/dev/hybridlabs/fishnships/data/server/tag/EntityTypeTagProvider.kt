package dev.hybridlabs.fishnships.data.server.tag

import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.hapi.tag.HAPIEntityTags
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup

class EntityTypeTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.EntityTypeTagProvider(output, registriesFuture) {

    override fun addTags(arg: HolderLookup.Provider) {
        // Hybrid API's boat tag. Our vehicles do not extend vanilla's Boat, so HAPI needs to be
        // told about them for things like lily pad breaking to work. Declared by resource location
        // rather than by importing HAPI, since Fish N Ships does not depend on it.
        getOrCreateTagBuilder(HAPIEntityTags.BOATS)
            .add(
                FSEntityTypes.SHIP.get(),
                FSEntityTypes.CUSTOM_BOAT.get(),
                FSEntityTypes.CUSTOM_CHEST_BOAT.get(),
                FSEntityTypes.SAILBOAT.get(),
                FSEntityTypes.SAILBOAT_WITH_CHEST.get(),
                FSEntityTypes.CANOE.get(),
                FSEntityTypes.CANOE_WITH_CHEST.get(),
                FSEntityTypes.CANOE_WITH_DOUBLE_CHEST.get(),
                FSEntityTypes.DINGHY.get(),
                FSEntityTypes.DINGHY_WITH_CHEST.get(),
                FSEntityTypes.RAFT.get(),
                FSEntityTypes.SUPPLY_RAFT.get()
            )
    }
}
