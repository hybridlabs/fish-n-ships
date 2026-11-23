package dev.hybridlabs.fishnships.client.render.entity

import dev.hybridlabs.fishnships.client.render.entity.misc.SailboatEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.CanoeEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.CanoeWithChestEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.CanoeWithDoubleChestEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.CustomBoatEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.CustomBoatWithChestEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.DinghyEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.DinghyWithChestEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.RaftEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.SailboatWithChestEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.ShipEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.SupplyRaftEntityRenderer
import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.platform.ClientServices

@Suppress("unused")
object FSEntityRenderers {
    val SHIP =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.SHIP,
            ::ShipEntityRenderer
        )

    // Every wood type is its own entity type, so every one needs its own renderer registration.
    val CUSTOM_BOATS = FSEntityTypes.CUSTOM_BOATS.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::CustomBoatEntityRenderer)
        entityType
    }

    val CUSTOM_CHEST_BOATS = FSEntityTypes.CUSTOM_CHEST_BOATS.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::CustomBoatWithChestEntityRenderer)
        entityType
    }

    val SAILBOATS = FSEntityTypes.SAILBOATS.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::SailboatEntityRenderer)
        entityType
    }

    val SAILBOATS_WITH_CHEST = FSEntityTypes.SAILBOATS_WITH_CHEST.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::SailboatWithChestEntityRenderer)
        entityType
    }

    val CANOES = FSEntityTypes.CANOES.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::CanoeEntityRenderer)
        entityType
    }

    val CANOES_WITH_CHEST = FSEntityTypes.CANOES_WITH_CHEST.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::CanoeWithChestEntityRenderer)
        entityType
    }

    val CANOES_WITH_DOUBLE_CHEST = FSEntityTypes.CANOES_WITH_DOUBLE_CHEST.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::CanoeWithDoubleChestEntityRenderer)
        entityType
    }

    val DINGHIES = FSEntityTypes.DINGHIES.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::DinghyEntityRenderer)
        entityType
    }

    val DINGHIES_WITH_CHEST = FSEntityTypes.DINGHIES_WITH_CHEST.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::DinghyWithChestEntityRenderer)
        entityType
    }

    val RAFTS = FSEntityTypes.RAFTS.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::RaftEntityRenderer)
        entityType
    }

    val SUPPLY_RAFTS = FSEntityTypes.SUPPLY_RAFTS.mapValues { (_, entityType) ->
        ClientServices.RENDERER.registerEntityRenderer(entityType, ::SupplyRaftEntityRenderer)
        entityType
    }
}
