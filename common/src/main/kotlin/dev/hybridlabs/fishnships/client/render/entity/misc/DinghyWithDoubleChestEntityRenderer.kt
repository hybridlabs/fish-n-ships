package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.DinghyWithDoubleChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.DinghyWithDoubleChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class DinghyWithDoubleChestEntityRenderer<T : DinghyWithDoubleChestEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, DinghyWithDoubleChestEntityModel()) {

    init {
        addRenderLayer(WaterPatchEntityLayer(this))
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}