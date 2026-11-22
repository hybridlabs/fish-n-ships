package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.FSRenderData
import dev.hybridlabs.fishnships.client.model.entity.misc.ShipEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.ShipGlassEntityLayer
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.ShipIcebreakerEntityLayer
import dev.hybridlabs.fishnships.client.render.entity.misc.layer.ShipTrawlingNetEntityLayer
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.EntityRenderState

class ShipEntityRenderer<T : ShipEntity>(
    context: EntityRendererProvider.Context
) : FSVehicleRenderer<T>(context, ShipEntityModel()) {

    init {
        withRenderLayer(ShipGlassEntityLayer(this))
        withRenderLayer(ShipIcebreakerEntityLayer(this))
        withRenderLayer(ShipTrawlingNetEntityLayer(this))
    }

    override fun addRenderData(
        animatable: T,
        relatedObject: Void?,
        renderState: EntityRenderState,
        partialTick: Float,
    ) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick)
        renderState.addGeckolibData(FSRenderData.HAS_ICEBREAKER, animatable.hasIceBreaker())
        renderState.addGeckolibData(FSRenderData.HAS_TRAWLING_NET, animatable.hasTrawlingNet())
    }
}
