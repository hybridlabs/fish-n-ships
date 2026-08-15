package dev.hybridlabs.fishnships.data.client

import dev.hybridlabs.fishnships.data.FSDataGenerator.filterFishNShips
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.item.FSItemGroups
import dev.hybridlabs.fishnships.item.FSItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import java.util.concurrent.CompletableFuture

class LanguageProvider( output: FabricDataOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output,lookupProvider) {
    override fun generateTranslations(lookupProvider: HolderLookup.Provider, builder: TranslationBuilder) {
        builder.add(
            BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(FSItemGroups.FISH_N_SHIPS.get())
                .orElseThrow { IllegalStateException("Item group not registered") }, "Fish N Ships"
        )

        mapOf(
            FSItems.SHIP.get() to "Ship",

            FSItems.OAK_SAILBOAT.get() to "Oak Sailboat",
            FSItems.OAK_SAILBOAT_WITH_CHEST.get() to "Oak Sailboat With Chest",
            FSItems.OAK_CANOE.get() to "Oak Canoe",
            FSItems.OAK_CANOE_WITH_CHEST.get() to "Oak Canoe With Chest",
            FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get() to "Oak Canoe With Double Chest",
            FSItems.OAK_RAFT.get() to "Oak Raft",
            FSItems.OAK_SUPPLY_RAFT.get() to "Oak Supply Raft",
            FSItems.OAK_DINGHY.get() to "Oak Dinghy",
            FSItems.OAK_DINGHY_WITH_CHEST.get() to "Oak Dinghy With Chest",

            FSItems.SPRUCE_SAILBOAT.get() to "Spruce Sailboat",
            FSItems.SPRUCE_SAILBOAT_WITH_CHEST.get() to "Spruce Sailboat With Chest",
            FSItems.SPRUCE_CANOE.get() to "Spruce Canoe",
            FSItems.SPRUCE_CANOE_WITH_CHEST.get() to "Spruce Canoe With Chest",
            FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST.get() to "Spruce Canoe With Double Chest",
            FSItems.SPRUCE_RAFT.get() to "Spruce Raft",
            FSItems.SPRUCE_SUPPLY_RAFT.get() to "Spruce Supply Raft",
            FSItems.SPRUCE_DINGHY.get() to "Spruce Dinghy",
            FSItems.SPRUCE_DINGHY_WITH_CHEST.get() to "Spruce Dinghy With Chest",

            FSItems.BIRCH_SAILBOAT.get() to "Birch Sailboat",
            FSItems.BIRCH_SAILBOAT_WITH_CHEST.get() to "Birch Sailboat With Chest",
            FSItems.BIRCH_CANOE.get() to "Birch Canoe",
            FSItems.BIRCH_CANOE_WITH_CHEST.get() to "Birch Canoe With Chest",
            FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST.get() to "Birch Canoe With Double Chest",
            FSItems.BIRCH_RAFT.get() to "Birch Raft",
            FSItems.BIRCH_SUPPLY_RAFT.get() to "Birch Supply Raft",
            FSItems.BIRCH_DINGHY.get() to "Birch Dinghy",
            FSItems.BIRCH_DINGHY_WITH_CHEST.get() to "Birch Dinghy With Chest",

            FSItems.JUNGLE_SAILBOAT.get() to "Jungle Sailboat",
            FSItems.JUNGLE_SAILBOAT_WITH_CHEST.get() to "Jungle Sailboat With Chest",
            FSItems.JUNGLE_CANOE.get() to "Jungle Canoe",
            FSItems.JUNGLE_CANOE_WITH_CHEST.get() to "Jungle Canoe With Chest",
            FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST.get() to "Jungle Canoe With Double Chest",
            FSItems.JUNGLE_RAFT.get() to "Jungle Raft",
            FSItems.JUNGLE_SUPPLY_RAFT.get() to "Jungle Supply Raft",
            FSItems.JUNGLE_DINGHY.get() to "Jungle Dinghy",
            FSItems.JUNGLE_DINGHY_WITH_CHEST.get() to "Jungle Dinghy With Chest",

            FSItems.ACACIA_SAILBOAT.get() to "Acacia Sailboat",
            FSItems.ACACIA_SAILBOAT_WITH_CHEST.get() to "Acacia Sailboat With Chest",
            FSItems.ACACIA_CANOE.get() to "Acacia Canoe",
            FSItems.ACACIA_CANOE_WITH_CHEST.get() to "Acacia Canoe With Chest",
            FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST.get() to "Acacia Canoe With Double Chest",
            FSItems.ACACIA_RAFT.get() to "Acacia Raft",
            FSItems.ACACIA_SUPPLY_RAFT.get() to "Acacia Supply Raft",
            FSItems.ACACIA_DINGHY.get() to "Acacia Dinghy",
            FSItems.ACACIA_DINGHY_WITH_CHEST.get() to "Acacia Dinghy With Chest",

            FSItems.CHERRY_SAILBOAT.get() to "Cherry Sailboat",
            FSItems.CHERRY_SAILBOAT_WITH_CHEST.get() to "Cherry Sailboat With Chest",
            FSItems.CHERRY_CANOE.get() to "Cherry Canoe",
            FSItems.CHERRY_CANOE_WITH_CHEST.get() to "Cherry Canoe With Chest",
            FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST.get() to "Cherry Canoe With Double Chest",
            FSItems.CHERRY_RAFT.get() to "Cherry Raft",
            FSItems.CHERRY_SUPPLY_RAFT.get() to "Cherry Supply Raft",
            FSItems.CHERRY_DINGHY.get() to "Cherry Dinghy",
            FSItems.CHERRY_DINGHY_WITH_CHEST.get() to "Cherry Dinghy With Chest",

            FSItems.DARK_OAK_SAILBOAT.get() to "Dark Oak Sailboat",
            FSItems.DARK_OAK_SAILBOAT_WITH_CHEST.get() to "Dark Oak Sailboat With Chest",
            FSItems.DARK_OAK_CANOE.get() to "Dark Oak Canoe",
            FSItems.DARK_OAK_CANOE_WITH_CHEST.get() to "Dark Oak Canoe With Chest",
            FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST.get() to "Dark Oak Canoe With Double Chest",
            FSItems.DARK_OAK_RAFT.get() to "Dark Oak Raft",
            FSItems.DARK_OAK_SUPPLY_RAFT.get() to "Dark Oak Supply Raft",
            FSItems.DARK_OAK_DINGHY.get() to "Dark Oak Dinghy",
            FSItems.DARK_OAK_DINGHY_WITH_CHEST.get() to "Dark Oak Dinghy With Chest",

            FSItems.MANGROVE_SAILBOAT.get() to "Mangrove Sailboat",
            FSItems.MANGROVE_SAILBOAT_WITH_CHEST.get() to "Mangrove Sailboat With Chest",
            FSItems.MANGROVE_CANOE.get() to "Mangrove Canoe",
            FSItems.MANGROVE_CANOE_WITH_CHEST.get() to "Mangrove Canoe With Chest",
            FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST.get() to "Mangrove Canoe With Double Chest",
            FSItems.MANGROVE_RAFT.get() to "Mangrove Raft",
            FSItems.MANGROVE_SUPPLY_RAFT.get() to "Mangrove Supply Raft",
            FSItems.MANGROVE_DINGHY.get() to "Mangrove Dinghy",
            FSItems.MANGROVE_DINGHY_WITH_CHEST.get() to "Mangrove Dinghy With Chest",

            FSItems.CRIMSON_BOAT.get() to "Crimson Boat",
            FSItems.CRIMSON_BOAT_WITH_CHEST.get() to "Crimson Boat With Chest",
            FSItems.CRIMSON_SAILBOAT.get() to "Crimson Sailboat",
            FSItems.CRIMSON_SAILBOAT_WITH_CHEST.get() to "Crimson Sailboat With Chest",
            FSItems.CRIMSON_CANOE.get() to "Crimson Canoe",
            FSItems.CRIMSON_CANOE_WITH_CHEST.get() to "Crimson Canoe With Chest",
            FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST.get() to "Crimson Canoe With Double Chest",
            FSItems.CRIMSON_RAFT.get() to "Crimson Raft",
            FSItems.CRIMSON_SUPPLY_RAFT.get() to "Crimson Supply Raft",
            FSItems.CRIMSON_DINGHY.get() to "Crimson Dinghy",
            FSItems.CRIMSON_DINGHY_WITH_CHEST.get() to "Crimson Dinghy With Chest",

            FSItems.WARPED_BOAT.get() to "Warped Boat",
            FSItems.WARPED_BOAT_WITH_CHEST.get() to "Warped Boat With Chest",
            FSItems.WARPED_SAILBOAT.get() to "Warped Sailboat",
            FSItems.WARPED_SAILBOAT_WITH_CHEST.get() to "Warped Sailboat With Chest",
            FSItems.WARPED_CANOE.get() to "Warped Canoe",
            FSItems.WARPED_CANOE_WITH_CHEST.get() to "Warped Canoe With Chest",
            FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST.get() to "Warped Canoe With Double Chest",
            FSItems.WARPED_RAFT.get() to "Warped Raft",
            FSItems.WARPED_SUPPLY_RAFT.get() to "Warped Supply Raft",
            FSItems.WARPED_DINGHY.get() to "Warped Dinghy",
            FSItems.WARPED_DINGHY_WITH_CHEST.get() to "Warped Dinghy With Chest",

            FSItems.DRIFTWOOD_BOAT.get() to "Driftwood Boat",
            FSItems.DRIFTWOOD_BOAT_WITH_CHEST.get() to "Driftwood Boat With Chest",
            FSItems.DRIFTWOOD_SAILBOAT.get() to "Driftwood Sailboat",
            FSItems.DRIFTWOOD_SAILBOAT_WITH_CHEST.get() to "Driftwood Sailboat With Chest",
            FSItems.DRIFTWOOD_CANOE.get() to "Driftwood Canoe",
            FSItems.DRIFTWOOD_CANOE_WITH_CHEST.get() to "Driftwood Canoe With Chest",
            FSItems.DRIFTWOOD_CANOE_WITH_DOUBLE_CHEST.get() to "Driftwood Canoe With Double Chest",
            FSItems.DRIFTWOOD_RAFT.get() to "Driftwood Raft",
            FSItems.DRIFTWOOD_SUPPLY_RAFT.get() to "Driftwood Supply Raft",
            FSItems.DRIFTWOOD_DINGHY.get() to "Driftwood Dinghy",
            FSItems.DRIFTWOOD_DINGHY_WITH_CHEST.get() to "Driftwood Dinghy With Chest",
            
            FSItems.TRAWLING_NET.get() to "Trawling Net",
            FSItems.ICEBREAKER.get() to "Icebreaker",
        ).forEach { (item, translation) ->
            builder.add(item, translation)
        }

        generateEntities(builder)
    }

    private fun generateEntities(builder: TranslationBuilder) {
        val entityNameMap = mapOf<EntityType<*>, String>(
            FSEntityTypes.SHIP.get() to "Ship",
            FSEntityTypes.CUSTOM_BOAT.get() to "Boat",
            FSEntityTypes.CUSTOM_CHEST_BOAT.get() to "Boat With Chest",
            FSEntityTypes.SAILBOAT.get() to "Sailboat",
            FSEntityTypes.SAILBOAT_WITH_CHEST.get() to "Sailboat With Chest",
            FSEntityTypes.CANOE.get() to "Canoe",
            FSEntityTypes.CANOE_WITH_CHEST.get() to "Canoe With Chest",
            FSEntityTypes.CANOE_WITH_DOUBLE_CHEST.get() to "Canoe With Double Chest",
            FSEntityTypes.DINGHY.get() to "Dinghy",
            FSEntityTypes.DINGHY_WITH_CHEST.get() to "Dinghy With Chest",
            FSEntityTypes.RAFT.get() to "Raft",
            FSEntityTypes.SUPPLY_RAFT.get() to "Supply Raft",
        )

        val nonPresentEntityNames = mutableListOf<EntityType<*>>()

        BuiltInRegistries.ENTITY_TYPE
            .filter(filterFishNShips(BuiltInRegistries.ENTITY_TYPE))
            .forEach { type ->
                if (type.baseClass.isAssignableFrom(Mob::class.java)) {
                    if (!entityNameMap.containsKey(type)) {
                        nonPresentEntityNames.add(type)
                    }
                }
            }

        if (nonPresentEntityNames.isNotEmpty()) {
            throw IllegalStateException("Entity to display name map does not contain ${nonPresentEntityNames.joinToString()}. Please modify ${javaClass.simpleName} accordingly.")
        }

        entityNameMap.forEach { (entityType, translation) ->
            val id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
            val translationKey = entityType.descriptionId
            val namespace = id.namespace
            val path = id.path
            builder.add(translationKey, translation)
            builder.add("item.$namespace.${path}_spawn_egg", "$translation Spawn Egg")
        }
    }
}
