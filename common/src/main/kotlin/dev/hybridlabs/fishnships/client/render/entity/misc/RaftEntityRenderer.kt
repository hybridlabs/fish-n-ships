package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.RaftEntityModel
import dev.hybridlabs.fishnships.entity.vehicle.RaftEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class RaftEntityRenderer<T : RaftEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, RaftEntityModel()) {

    init {
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}