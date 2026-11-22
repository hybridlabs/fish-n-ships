package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.SailboatWithChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.SailboatWithChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class SailboatWithChestEntityRenderer<T : SailboatWithChestEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, SailboatWithChestEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}