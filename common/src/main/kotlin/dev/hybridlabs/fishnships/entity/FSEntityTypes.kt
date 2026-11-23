package dev.hybridlabs.fishnships.entity

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.CanoeEntity
import dev.hybridlabs.fishnships.entity.vehicle.CanoeWithChestEntity
import dev.hybridlabs.fishnships.entity.vehicle.CanoeWithDoubleChestEntity
import dev.hybridlabs.fishnships.entity.vehicle.CustomBoatEntity
import dev.hybridlabs.fishnships.entity.vehicle.CustomChestBoatEntity
import dev.hybridlabs.fishnships.entity.vehicle.DinghyEntity
import dev.hybridlabs.fishnships.entity.vehicle.DinghyWithChestEntity
import dev.hybridlabs.fishnships.entity.vehicle.RaftEntity
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import dev.hybridlabs.fishnships.entity.vehicle.SailboatWithChestEntity
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import dev.hybridlabs.fishnships.entity.vehicle.SupplyRaftEntity
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object FSEntityTypes {

    val SHIP = registerMisc(
        "ship",
        ::ShipEntity,
            EntityDimensions.fixed(4.75f, 1.0f),
    )

    // Vanilla gave every wood type its own boat entity type in 1.21.2 (24w39a). Our vehicles follow
    // that convention: the entity id matches the item id, and the wood type is encoded by the id
    // rather than by a shared "boat"/"sailboat"/... entity type.
    val CUSTOM_BOATS: Map<CustomBoatEntity.Type, RegistryObject<EntityType<CustomBoatEntity>>> =
        CustomBoatEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_boat",
                { entityType, level ->
                    CustomBoatEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(1.375f, 0.5625f),
            )
        }

    val CUSTOM_CHEST_BOATS: Map<CustomBoatEntity.Type, RegistryObject<EntityType<CustomChestBoatEntity>>> =
        CustomBoatEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_boat_with_chest",
                { entityType, level ->
                    CustomChestBoatEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(1.375f, 0.5625f),
            )
        }

    val SAILBOATS: Map<SailboatEntity.Type, RegistryObject<EntityType<SailboatEntity>>> =
        SailboatEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_sailboat",
                { entityType, level ->
                    SailboatEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(1.375f, 0.5625f),
            )
        }

    val SAILBOATS_WITH_CHEST: Map<SailboatEntity.Type, RegistryObject<EntityType<SailboatWithChestEntity>>> =
        SailboatEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_sailboat_with_chest",
                { entityType, level ->
                    SailboatWithChestEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(1.375f, 0.5625f),
            )
        }

    val CANOES: Map<CanoeEntity.Type, RegistryObject<EntityType<CanoeEntity>>> =
        CanoeEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_canoe",
                { entityType, level ->
                    CanoeEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.5f, 0.5625f),
            )
        }

    val CANOES_WITH_CHEST: Map<CanoeEntity.Type, RegistryObject<EntityType<CanoeWithChestEntity>>> =
        CanoeEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_canoe_with_chest",
                { entityType, level ->
                    CanoeWithChestEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.5f, 0.5625f),
            )
        }

    val CANOES_WITH_DOUBLE_CHEST: Map<CanoeEntity.Type, RegistryObject<EntityType<CanoeWithDoubleChestEntity>>> =
        CanoeEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_canoe_with_double_chest",
                { entityType, level ->
                    CanoeWithDoubleChestEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.5f, 0.5625f),
            )
        }

    val DINGHIES: Map<DinghyEntity.Type, RegistryObject<EntityType<DinghyEntity>>> =
        DinghyEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_dinghy",
                { entityType, level ->
                    DinghyEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.8f, 0.5625f),
            )
        }

    val DINGHIES_WITH_CHEST: Map<DinghyEntity.Type, RegistryObject<EntityType<DinghyWithChestEntity>>> =
        DinghyEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_dinghy_with_chest",
                { entityType, level ->
                    DinghyWithChestEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.8f, 0.5625f),
            )
        }

    val RAFTS: Map<RaftEntity.Type, RegistryObject<EntityType<RaftEntity>>> =
        RaftEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_raft",
                { entityType, level ->
                    RaftEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.75f, 0.5625f),
            )
        }

    val SUPPLY_RAFTS: Map<RaftEntity.Type, RegistryObject<EntityType<SupplyRaftEntity>>> =
        RaftEntity.Type.entries.associateWith { variant ->
            registerMisc(
                "${variant.serializedName}_supply_raft",
                { entityType, level ->
                    SupplyRaftEntity(entityType, level, variant)
                },
                EntityDimensions.fixed(2.75f, 0.5625f),
            )
        }

    //#region Misc Entity Registration
    private fun <T : Entity> registerMisc(
        id: String,
        entityFactory: EntityType.EntityFactory<T>,
        dimensions: EntityDimensions,
    ): RegistryObject<EntityType<T>> {
        return CommonClass.ENTITY_TYPES.register(id) {
            EntityType.Builder
                .of(entityFactory, MobCategory.MISC)
                .sized(dimensions.width, dimensions.height)
                .clientTrackingRange(10)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, CommonClass.locate(id)))
        }
    }
}
