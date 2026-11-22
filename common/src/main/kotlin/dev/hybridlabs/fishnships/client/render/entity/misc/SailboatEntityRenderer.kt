package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.SailboatEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class SailboatEntityRenderer<T : SailboatEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, SailboatEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}