package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.ShipEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.ShipFlagEntityLayer
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.ShipIcebreakerEntityLayer
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.ShipTrawlingNetEntityLayer
import dev.hybridlabs.fishnships.entity.ship.ShipEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class ShipEntityRenderer<T : ShipEntity>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<T>(context, ShipEntityModel()) {

    init {
        addRenderLayer(ShipFlagEntityLayer(this))
        addRenderLayer(ShipIcebreakerEntityLayer(this))
        addRenderLayer(ShipTrawlingNetEntityLayer(this))
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }
}