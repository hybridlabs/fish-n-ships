package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.DinghyWithChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.DinghyWithChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class DinghyWithChestEntityRenderer<T : DinghyWithChestEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, DinghyWithChestEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}