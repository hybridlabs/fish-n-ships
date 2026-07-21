package dev.hybridlabs.fishnships.data.client

import dev.hybridlabs.fishnships.data.FSDataGenerator.filterFishNShips
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.item.FSItemGroups
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

    }

    private fun generateEntities(builder: TranslationBuilder) {
        val entityNameMap = mapOf<EntityType<*>, String>(
            FSEntityTypes.SHIP.get() to "Ship",
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
