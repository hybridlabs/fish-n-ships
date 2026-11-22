package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.RaftEntityModel
import dev.hybridlabs.fishnships.entity.vehicle.RaftEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class RaftEntityRenderer<T : RaftEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, RaftEntityModel()) {

    init {
    }
}