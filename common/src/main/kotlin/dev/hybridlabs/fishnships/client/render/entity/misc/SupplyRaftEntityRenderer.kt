package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.SupplyRaftEntityModel
import dev.hybridlabs.fishnships.entity.vehicle.SupplyRaftEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class SupplyRaftEntityRenderer<T : SupplyRaftEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, SupplyRaftEntityModel()) {

    init {
    }
}