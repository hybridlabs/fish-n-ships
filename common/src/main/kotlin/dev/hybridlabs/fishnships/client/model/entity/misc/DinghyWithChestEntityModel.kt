package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.DinghyEntity
import dev.hybridlabs.fishnships.entity.vehicle.DinghyWithChestEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animatable.model.CoreGeoBone
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
class DinghyWithChestEntityModel<T : DinghyWithChestEntity>() :
    GeoModel<T>() {

    override fun getRenderType(animatable: T, texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucent(texture)
    }

    override fun getModelResource(animatable: T): ResourceLocation {
        return if (animatable.alternate) {
            ALT_MODEL
        } else {
            DEFAULT_MODEL
        }
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return if (animatable.alternate) {
            when (animatable.variant) {
                DinghyEntity.Type.OAK -> ALT_OAK_TEXTURE
                DinghyEntity.Type.SPRUCE -> ALT_SPRUCE_TEXTURE
                DinghyEntity.Type.BIRCH -> ALT_BIRCH_TEXTURE
                DinghyEntity.Type.JUNGLE -> ALT_JUNGLE_TEXTURE
                DinghyEntity.Type.ACACIA -> ALT_ACACIA_TEXTURE
                DinghyEntity.Type.CHERRY -> ALT_CHERRY_TEXTURE
                DinghyEntity.Type.DARK_OAK -> ALT_DARK_OAK_TEXTURE
                DinghyEntity.Type.MANGROVE -> ALT_MANGROVE_TEXTURE
                DinghyEntity.Type.CRIMSON -> ALT_CRIMSON_TEXTURE
                DinghyEntity.Type.WARPED -> ALT_WARPED_TEXTURE
                DinghyEntity.Type.DRIFTWOOD -> ALT_DRIFTWOOD_TEXTURE
            }
        } else {
            when (animatable.variant) {
                DinghyEntity.Type.OAK -> OAK_TEXTURE
                DinghyEntity.Type.SPRUCE -> SPRUCE_TEXTURE
                DinghyEntity.Type.BIRCH -> BIRCH_TEXTURE
                DinghyEntity.Type.JUNGLE -> JUNGLE_TEXTURE
                DinghyEntity.Type.ACACIA -> ACACIA_TEXTURE
                DinghyEntity.Type.CHERRY -> CHERRY_TEXTURE
                DinghyEntity.Type.DARK_OAK -> DARK_OAK_TEXTURE
                DinghyEntity.Type.MANGROVE -> MANGROVE_TEXTURE
                DinghyEntity.Type.CRIMSON -> CRIMSON_TEXTURE
                DinghyEntity.Type.WARPED -> WARPED_TEXTURE
                DinghyEntity.Type.DRIFTWOOD -> DRIFTWOOD_TEXTURE
            }
        }
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return CommonClass.locate("animations/entity/dinghy/dinghy.animation.json")
    }

    override fun setCustomAnimations(
        animatable: T,
        instanceId: Long,
        animationState: AnimationState<T>,
    ) {
        val partialTick: Float = animationState.partialTick

        val body = animationProcessor.getBone(PartNames.BODY)
        val leftPaddle = animationProcessor.getBone("paddle_left")
        val rightPaddle = animationProcessor.getBone("paddle_right")

        val yaw = Mth.rotLerp(partialTick, animatable.yRotO, animatable.yRot)
        body.rotY = -yaw * Mth.DEG_TO_RAD

        animatePaddle(animatable, 0, leftPaddle, partialTick)
        animatePaddle(animatable, 1, rightPaddle, partialTick)
    }

    private fun animatePaddle(
        dinghy: DinghyEntity,
        side: Int,
        paddle: CoreGeoBone,
        partialTick: Float
    ) {
        val f = dinghy.getRowingTime(side, partialTick) + Mth.PI

        val xAnim = Mth.clampedLerp(
            -Mth.PI / 3f,
            -0.2617994f,
            (Mth.sin(-f) + 1f) / 2f
        )

        val yAnim = Mth.clampedLerp(
            -Mth.PI / 4f,
            Mth.PI / 4f,
            1f - (Mth.sin(-f + 1f) + 1f) / 2f
        )

        if (side == 0) {
            paddle.rotX = Math.toRadians(-124.0).toFloat() + xAnim
            paddle.rotY = Math.toRadians(-90.0).toFloat() + yAnim
            paddle.rotZ = Math.toRadians(-165.0).toFloat()
        } else {
            paddle.rotX = Math.toRadians(-124.0).toFloat() + xAnim
            paddle.rotY = Math.toRadians(90.0).toFloat() - yAnim
            paddle.rotZ = Math.toRadians(165.0).toFloat()
        }
    }

    companion object {
        private val DEFAULT_MODEL =
            CommonClass.locate("geo/entity/dinghy/dinghy_with_chest.geo.json")
        private val ALT_MODEL =
            CommonClass.locate("geo/entity/dinghy/alt_dinghy_with_chest.geo.json")

        private val OAK_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/oak_dinghy.png")
        private val SPRUCE_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/spruce_dinghy.png")
        private val BIRCH_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/birch_dinghy.png")
        private val JUNGLE_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/jungle_dinghy.png")
        private val ACACIA_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/acacia_dinghy.png")
        private val CHERRY_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/cherry_dinghy.png")
        private val DARK_OAK_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/dark_oak_dinghy.png")
        private val MANGROVE_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/mangrove_dinghy.png")
        private val CRIMSON_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/crimson_dinghy.png")
        private val WARPED_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/warped_dinghy.png")
        private val DRIFTWOOD_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/driftwood_dinghy.png")

        private val ALT_OAK_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_oak_dinghy.png")
        private val ALT_SPRUCE_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_spruce_dinghy.png")
        private val ALT_BIRCH_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_birch_dinghy.png")
        private val ALT_JUNGLE_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_jungle_dinghy.png")
        private val ALT_ACACIA_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_acacia_dinghy.png")
        private val ALT_CHERRY_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_cherry_dinghy.png")
        private val ALT_DARK_OAK_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_dark_oak_dinghy.png")
        private val ALT_MANGROVE_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_mangrove_dinghy.png")
        private val ALT_CRIMSON_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_crimson_dinghy.png")
        private val ALT_WARPED_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_warped_dinghy.png")
        private val ALT_DRIFTWOOD_TEXTURE =
            CommonClass.locate("textures/entity/dinghy/alt_driftwood_dinghy.png")
    }
}