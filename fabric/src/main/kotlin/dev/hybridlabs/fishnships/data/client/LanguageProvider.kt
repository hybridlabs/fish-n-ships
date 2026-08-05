package dev.hybridlabs.fishnships.data.client

import dev.hybridlabs.fishnships.data.FSDataGenerator.filterFishNShips
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.item.FSItemGroups
import dev.hybridlabs.fishnships.item.FSItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob

class LanguageProvider(output: FabricDataOutput) : FabricLanguageProvider(output) {
    override fun generateTranslations(builder: TranslationBuilder) {
        builder.add(
            BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(FSItemGroups.FISH_N_SHIPS.get())
                .orElseThrow { IllegalStateException("Item group not registered") }, "Fish N Ships"
        )

        generateEntities(builder)

        mapOf(
            FSItems.SHIP.get() to "Ship",

            FSItems.OAK_SAILBOAT.get() to "Oak Sailboat",
            FSItems.OAK_SAILBOAT_WITH_CHEST.get() to "Oak Sailboat With Chest",
            FSItems.OAK_CANOE.get() to "Oak Canoe",
            FSItems.OAK_CANOE_WITH_CHEST.get() to "Oak Canoe With Chest",
            FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get() to "Oak Canoe With Double Chest",
            FSItems.OAK_RAFT.get() to "Oak Raft",
            FSItems.OAK_SUPPLY_RAFT.get() to "Oak Supply Raft",

            FSItems.SPRUCE_SAILBOAT.get() to "Spruce Sailboat",
            FSItems.SPRUCE_SAILBOAT_WITH_CHEST.get() to "Spruce Sailboat With Chest",
            FSItems.SPRUCE_CANOE.get() to "Spruce Canoe",
            FSItems.SPRUCE_CANOE_WITH_CHEST.get() to "Spruce Canoe With Chest",
            FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST.get() to "Spruce Canoe With Double Chest",
            FSItems.SPRUCE_RAFT.get() to "Spruce Raft",
            FSItems.SPRUCE_SUPPLY_RAFT.get() to "Spruce Supply Raft",

            FSItems.BIRCH_SAILBOAT.get() to "Birch Sailboat",
            FSItems.BIRCH_SAILBOAT_WITH_CHEST.get() to "Birch Sailboat With Chest",
            FSItems.BIRCH_CANOE.get() to "Birch Canoe",
            FSItems.BIRCH_CANOE_WITH_CHEST.get() to "Birch Canoe With Chest",
            FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST.get() to "Birch Canoe With Double Chest",
            FSItems.BIRCH_RAFT.get() to "Birch Raft",
            FSItems.BIRCH_SUPPLY_RAFT.get() to "Birch Supply Raft",

            FSItems.JUNGLE_SAILBOAT.get() to "Jungle Sailboat",
            FSItems.JUNGLE_SAILBOAT_WITH_CHEST.get() to "Jungle Sailboat With Chest",
            FSItems.JUNGLE_CANOE.get() to "Jungle Canoe",
            FSItems.JUNGLE_CANOE_WITH_CHEST.get() to "Jungle Canoe With Chest",
            FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST.get() to "Jungle Canoe With Double Chest",
            FSItems.JUNGLE_RAFT.get() to "Jungle Raft",
            FSItems.JUNGLE_SUPPLY_RAFT.get() to "Jungle Supply Raft",

            FSItems.ACACIA_SAILBOAT.get() to "Acacia Sailboat",
            FSItems.ACACIA_SAILBOAT_WITH_CHEST.get() to "Acacia Sailboat With Chest",
            FSItems.ACACIA_CANOE.get() to "Acacia Canoe",
            FSItems.ACACIA_CANOE_WITH_CHEST.get() to "Acacia Canoe With Chest",
            FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST.get() to "Acacia Canoe With Double Chest",
            FSItems.ACACIA_RAFT.get() to "Acacia Raft",
            FSItems.ACACIA_SUPPLY_RAFT.get() to "Acacia Supply Raft",

            FSItems.CHERRY_SAILBOAT.get() to "Cherry Sailboat",
            FSItems.CHERRY_SAILBOAT_WITH_CHEST.get() to "Cherry Sailboat With Chest",
            FSItems.CHERRY_CANOE.get() to "Cherry Canoe",
            FSItems.CHERRY_CANOE_WITH_CHEST.get() to "Cherry Canoe With Chest",
            FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST.get() to "Cherry Canoe With Double Chest",
            FSItems.CHERRY_RAFT.get() to "Cherry Raft",
            FSItems.CHERRY_SUPPLY_RAFT.get() to "Cherry Supply Raft",

            FSItems.DARK_OAK_SAILBOAT.get() to "Dark Oak Sailboat",
            FSItems.DARK_OAK_SAILBOAT_WITH_CHEST.get() to "Dark Oak Sailboat With Chest",
            FSItems.DARK_OAK_CANOE.get() to "Dark Oak Canoe",
            FSItems.DARK_OAK_CANOE_WITH_CHEST.get() to "Dark Oak Canoe With Chest",
            FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST.get() to "Dark Oak Canoe With Double Chest",
            FSItems.DARK_OAK_RAFT.get() to "Dark Oak Raft",
            FSItems.DARK_OAK_SUPPLY_RAFT.get() to "Dark Oak Supply Raft",

            FSItems.MANGROVE_SAILBOAT.get() to "Mangrove Sailboat",
            FSItems.MANGROVE_SAILBOAT_WITH_CHEST.get() to "Mangrove Sailboat With Chest",
            FSItems.MANGROVE_CANOE.get() to "Mangrove Canoe",
            FSItems.MANGROVE_CANOE_WITH_CHEST.get() to "Mangrove Canoe With Chest",
            FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST.get() to "Mangrove Canoe With Double Chest",
            FSItems.MANGROVE_RAFT.get() to "Mangrove Raft",
            FSItems.MANGROVE_SUPPLY_RAFT.get() to "Mangrove Supply Raft",

            FSItems.CRIMSON_BOAT.get() to "Crimson Boat",
            FSItems.CRIMSON_BOAT_WITH_CHEST.get() to "Crimson Boat With Chest",
            FSItems.CRIMSON_SAILBOAT.get() to "Crimson Sailboat",
            FSItems.CRIMSON_SAILBOAT_WITH_CHEST.get() to "Crimson Sailboat With Chest",
            FSItems.CRIMSON_CANOE.get() to "Crimson Canoe",
            FSItems.CRIMSON_CANOE_WITH_CHEST.get() to "Crimson Canoe With Chest",
            FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST.get() to "Crimson Canoe With Double Chest",
            FSItems.CRIMSON_RAFT.get() to "Crimson Raft",
            FSItems.CRIMSON_SUPPLY_RAFT.get() to "Crimson Supply Raft",

            FSItems.WARPED_BOAT.get() to "Warped Boat",
            FSItems.WARPED_BOAT_WITH_CHEST.get() to "Warped Boat With Chest",
            FSItems.WARPED_SAILBOAT.get() to "Warped Sailboat",
            FSItems.WARPED_SAILBOAT_WITH_CHEST.get() to "Warped Sailboat With Chest",
            FSItems.WARPED_CANOE.get() to "Warped Canoe",
            FSItems.WARPED_CANOE_WITH_CHEST.get() to "Warped Canoe With Chest",
            FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST.get() to "Warped Canoe With Double Chest",
            FSItems.WARPED_RAFT.get() to "Warped Raft",
            FSItems.WARPED_SUPPLY_RAFT.get() to "Warped Supply Raft",
            
            FSItems.TRAWLING_NET.get() to "Trawling Net",
            FSItems.ICEBREAKER.get() to "Icebreaker",
        ).forEach { (item, translation) ->
            builder.add(item, translation)
        }
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
