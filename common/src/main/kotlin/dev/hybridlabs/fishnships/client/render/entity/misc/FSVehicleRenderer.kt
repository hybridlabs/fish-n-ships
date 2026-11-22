package dev.hybridlabs.fishnships.client.render.entity.misc

import dev.hybridlabs.fishnships.client.model.entity.misc.FSBoatAnimation
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer
import software.bernie.geckolib.renderer.base.BoneSnapshots
import software.bernie.geckolib.renderer.base.RenderPassInfo

/**
 * Shared GeckoLib 5 renderer for the mod's vehicles. GeckoLib 4 let models pose bones in
 * `setCustomAnimations`; GeckoLib 5 moved that to the renderer, so the body yaw and paddle poses
 * are applied here for every vehicle.
 */
open class FSVehicleRenderer<T>(
    context: EntityRendererProvider.Context,
    model: GeoModel<T>,
) : GeoEntityRenderer<T, EntityRenderState>(context, model)
    where T : Entity, T : GeoAnimatable {

    init {
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }

    override fun getRenderType(renderState: EntityRenderState, texture: Identifier): RenderType? {
        return RenderTypes.entityTranslucent(texture)
    }

    override fun addRenderData(
        animatable: T,
        relatedObject: Void?,
        renderState: EntityRenderState,
        partialTick: Float,
    ) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick)
        FSBoatAnimation.capture(animatable, renderState, partialTick)
    }

    override fun adjustModelBonesForRender(
        renderPassInfo: RenderPassInfo<EntityRenderState>,
        snapshots: BoneSnapshots,
    ) {
        FSBoatAnimation.adjust(renderPassInfo, snapshots)
    }
}
