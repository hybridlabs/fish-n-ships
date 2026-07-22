package dev.hybridlabs.fishnships.client.render.entity.misc.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.hybridlabs.fishnships.client.render.entity.misc.ShipEntityRenderer
import dev.hybridlabs.fishnships.client.model.entity.misc.ShipEntityModel
import dev.hybridlabs.fishnships.entity.ship.ShipEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.renderer.layer.GeoRenderLayer

class ShipTrawlingNetEntityLayer<T: ShipEntity>(
    renderer: ShipEntityRenderer<T>
): GeoRenderLayer<T>(renderer) {

    private fun getTrawlingNetTexture(): ResourceLocation {
        return (geoModel as ShipEntityModel).getTrawlingNetTextureResource()
    }

    override fun render(
        poseStack: PoseStack,
        animatable: T,
        bakedModel: BakedGeoModel,
        renderType: RenderType,
        bufferSource: MultiBufferSource,
        buffer: VertexConsumer,
        partialTick: Float,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val trawlingNetTexture = getTrawlingNetTexture()
        val trawlingNetRenderType = RenderType.entityTranslucent(trawlingNetTexture)
        if (!animatable.hasTrawlingNet()) return

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, trawlingNetRenderType,
            bufferSource.getBuffer(trawlingNetRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
            1f, 1f, 1f, 1f)
    }
}