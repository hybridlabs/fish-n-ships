package dev.hybridlabs.fishnships.entity

import dev.hybridlabs.fishnships.utils.FSSpawnGroup
import net.minecraft.world.entity.MobCategory

object ForgeSpawnGroupRegistry {
    fun createFishNShipsSpawnGroups() {
        // Extend the MobCategory enum with our spawn groups
        for (group in FSSpawnGroup.values()) {
            MobCategory.create(
                group.location.path,
                group.location.toString(),
                group.spawnCap,
                group.peaceful,
                group.rare,
                group.immediateDespawnRange
            )
        }
    }
}
