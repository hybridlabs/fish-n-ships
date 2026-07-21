package dev.hybridlabs.fishnships.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.entity.SpawnPlacements.SpawnPredicate
import net.minecraft.world.entity.SpawnPlacements.Type
import net.minecraft.world.level.levelgen.Heightmap

/**
 * Registers spawn restrictions for all entities when initialised.
 */
@Suppress("UNCHECKED_CAST")
object SpawnRestrictionRegistry {
    fun registerSpawnRestrictions() {
    }

    private fun <T : Mob> register(
        entityType: EntityType<T>,
        location: Type,
        predicate: SpawnPredicate<T>,
    ) {
        SpawnPlacements.register(entityType, location, Heightmap.Types.WORLD_SURFACE, predicate)
    }
}
