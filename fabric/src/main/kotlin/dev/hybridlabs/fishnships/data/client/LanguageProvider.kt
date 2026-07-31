package dev.hybridlabs.fishnships.data.client

import dev.hybridlabs.fishnships.data.FSDataGenerator.filterFishNShips
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.item.FSItemGroups
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.sound.FSSoundEvents
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.Util
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob

class LanguageProvider(output: FabricDataOutput) : FabricLanguageProvider(output) {
    override fun generateTranslations(builder: TranslationBuilder) {
        builder.add(
            BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(FSItemGroups.FISH_N_SHIPS.get())
                .orElseThrow { IllegalStateException("Item group not registered") }, "Fish N Ships"
        )

        generateEntities(builder)//Sound Events
        mapOf(
            FSSoundEvents.ALBATROSS_AMBIENT to "Albatross squawks",
            FSSoundEvents.ALBATROSS_HURT to "Albatross hurts",
            FSSoundEvents.ALBATROSS_DIE to "Albatross dies",

        ).forEach { (soundEvent, translation) ->
            builder.add(Util.makeDescriptionId("subtitles", soundEvent.get().location), translation)
        }
        mapOf(
            FSItems.SHIP.get() to "Ship",
            FSItems.SAILBOAT.get() to "Sailboat",
            FSItems.CANOE.get() to "Canoe",
            FSItems.CANOE_WITH_CHEST.get() to "Canoe With Chest",
            FSItems.CANOE_WITH_DOUBLE_CHEST.get() to "Canoe With Double Chest",
            FSItems.RAFT.get() to "Raft",
            FSItems.SUPPLY_RAFT.get() to "Supply Raft",
        ).forEach { (item, translation) ->
            builder.add(item, translation)
        }
    }

    private fun generateEntities(builder: TranslationBuilder) {
        val entityNameMap = mapOf<EntityType<*>, String>(
            FSEntityTypes.SHIP.get() to "Ship",
            FSEntityTypes.SAILBOAT.get() to "Sailboat",
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
