package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.CanoeEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.CanoeWaterpatchEntityLayer
import dev.hybridlabs.fishnships.entity.ship.CanoeEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class CanoeEntityRenderer<T : CanoeEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, CanoeEntityModel()) {

    init {
        addRenderLayer(CanoeWaterpatchEntityLayer(this))
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}