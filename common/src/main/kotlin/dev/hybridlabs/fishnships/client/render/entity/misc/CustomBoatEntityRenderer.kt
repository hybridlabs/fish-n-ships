package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CustomBoatEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.CustomBoatEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class CustomBoatEntityRenderer<T : CustomBoatEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, CustomBoatEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}