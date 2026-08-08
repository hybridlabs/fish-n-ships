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
    //region fish
    val SHIP =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.SHIP,
            ::ShipEntityRenderer
        )

    val CUSTOM_BOAT =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.CUSTOM_BOAT,
            ::CustomBoatEntityRenderer
        )

    val CUSTOM_BOAT_WITH_CHEST =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.CUSTOM_CHEST_BOAT,
            ::CustomBoatWithChestEntityRenderer
        )

    val SAILBOAT =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.SAILBOAT,
            ::SailboatEntityRenderer
        )

    val SAILBOAT_WITH_CHEST =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.SAILBOAT_WITH_CHEST,
            ::SailboatWithChestEntityRenderer
        )

    val CANOE =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.CANOE,
            ::CanoeEntityRenderer
        )

    val CANOE_WITH_CHEST =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.CANOE_WITH_CHEST,
            ::CanoeWithChestEntityRenderer
        )

    val CANOE_WITH_DOUBLE_CHEST =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.CANOE_WITH_DOUBLE_CHEST,
            ::CanoeWithDoubleChestEntityRenderer
        )

    val RAFT =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.RAFT,
            ::RaftEntityRenderer
        )

    val SUPPLY_RAFT =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.SUPPLY_RAFT,
            ::SupplyRaftEntityRenderer
        )

    val DINGHY =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.DINGHY,
            ::DinghyEntityRenderer
        )

    val DINGHY_WITH_CHEST =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.DINGHY_WITH_CHEST,
            ::DinghyWithChestEntityRenderer
        )
}
