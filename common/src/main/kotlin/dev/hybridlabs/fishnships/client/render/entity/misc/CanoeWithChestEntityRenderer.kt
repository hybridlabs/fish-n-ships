package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CanoeWithChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.CanoeWithChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class CanoeWithChestEntityRenderer<T : CanoeWithChestEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, CanoeWithChestEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}