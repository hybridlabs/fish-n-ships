package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.DinghyEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.DinghyEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class DinghyEntityRenderer<T : DinghyEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, DinghyEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}