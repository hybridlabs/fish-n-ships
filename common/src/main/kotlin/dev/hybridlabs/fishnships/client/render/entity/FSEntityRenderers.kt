package dev.hybridlabs.fishnships.client.render.entity

import dev.hybridlabs.fishnships.client.render.entity.misc.CanoeEntityRenderer
import dev.hybridlabs.fishnships.client.render.entity.misc.ShipEntityRenderer
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

    val CANOE =
        ClientServices.RENDERER.registerEntityRenderer(
            FSEntityTypes.CANOE,
            ::CanoeEntityRenderer
        )
}
