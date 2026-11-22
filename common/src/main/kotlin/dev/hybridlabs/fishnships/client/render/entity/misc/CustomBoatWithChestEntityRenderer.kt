package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CustomChestBoatEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.CustomChestBoatEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class CustomBoatWithChestEntityRenderer<T : CustomChestBoatEntity>(
    context: EntityRendererProvider.Context,
) : FSVehicleRenderer<T>(context, CustomChestBoatEntityModel()) {

    init {
        withRenderLayer(WaterPatchEntityLayer(this))
    }
}