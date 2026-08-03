package dev.hybridlabs.fishnships.client.render.entity.misc.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.hybridlabs.fishnships.client.render.entity.misc.SailboatWithChestEntityRenderer
import dev.hybridlabs.fishnships.entity.ship.SailboatWithChestEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.renderer.layer.GeoRenderLayer

class SailboatWithChestWaterpatchEntityLayer<T: SailboatWithChestEntity>(
    renderer: SailboatWithChestEntityRenderer<T>
): GeoRenderLayer<T>(renderer) {

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
        val waterPatchRenderType = RenderType.waterMask()

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, waterPatchRenderType,
            bufferSource.getBuffer(waterPatchRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
            1f, 1f, 1f, 1f)
    }
}