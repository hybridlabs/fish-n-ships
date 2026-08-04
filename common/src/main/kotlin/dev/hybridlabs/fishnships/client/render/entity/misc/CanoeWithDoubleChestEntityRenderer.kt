package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CanoeWithDoubleChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.WaterPatchEntityLayer
import dev.hybridlabs.fishnships.entity.ship.CanoeWithDoubleChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class CanoeWithDoubleChestEntityRenderer<T : CanoeWithDoubleChestEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, CanoeWithDoubleChestEntityModel()) {

    init {
        addRenderLayer(WaterPatchEntityLayer(this))
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}