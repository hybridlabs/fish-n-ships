package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CanoeEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.CanoeEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class CanoeEntityRenderer<T : CanoeEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, CanoeEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}