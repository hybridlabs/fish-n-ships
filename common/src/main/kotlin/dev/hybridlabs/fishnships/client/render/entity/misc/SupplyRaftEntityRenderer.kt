package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.SupplyRaftEntityModel
import dev.hybridlabs.fishnships.entity.vehicle.SupplyRaftEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SupplyRaftEntityRenderer<T : SupplyRaftEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, SupplyRaftEntityModel()) {

    init {
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}