package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CanoeWithDoubleChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.CanoeWithDoubleChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class CanoeWithDoubleChestEntityRenderer<T : CanoeWithDoubleChestEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, CanoeWithDoubleChestEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}