package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.SailboatWithChestEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.SailboatWithChestWaterpatchEntityLayer
import dev.hybridlabs.fishnships.entity.ship.SailboatWithChestEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SailboatWithChestEntityRenderer<T : SailboatWithChestEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, SailboatWithChestEntityModel()) {

    init {
        addRenderLayer(SailboatWithChestWaterpatchEntityLayer(this))
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}