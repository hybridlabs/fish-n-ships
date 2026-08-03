package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.ship.CanoeEntity
import dev.hybridlabs.fishnships.entity.ship.CanoeWithChestEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.core.animatable.model.CoreGeoBone
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
class CanoeWithChestEntityModel<T : CanoeWithChestEntity>() :
    GeoModel<T>() {

    override fun getRenderType(animatable: T, texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucent(texture)
    }

    override fun getModelResource(animatable: T): ResourceLocation {
        return CommonClass.locate("geo/entity/canoe/canoe_with_chest.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return CommonClass.locate("textures/entity/canoe/oak_canoe_with_chest.png")
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return CommonClass.locate("animations/entity/canoe/canoe.animation.json")
    }

    override fun setCustomAnimations(
        animatable: T,
        instanceId: Long,
        animationState: AnimationState<T>,
    ) {
        val partialTick = animationState.partialTick

        val body = animationProcessor.getBone(PartNames.BODY)
        val leftPaddle = animationProcessor.getBone("paddle_left")
        val rightPaddle = animationProcessor.getBone("paddle_right")

        val yaw = Mth.rotLerp(partialTick, animatable.yRotO, animatable.yRot)
        body.rotY = -yaw * Mth.DEG_TO_RAD

        animatePaddle(animatable, 0, leftPaddle, partialTick)
        animatePaddle(animatable, 1, rightPaddle, partialTick)
    }

    private fun animatePaddle(
        canoe: CanoeEntity,
        side: Int,
        paddle: CoreGeoBone,
        partialTick: Float
    ) {
        val f = canoe.getRowingTime(side, partialTick) + Mth.PI

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
        private val OAK_TEXTURE =
            CommonClass.locate("textures/entity/canoe/oak_canoe_with_chest.png")
        private val SPRUCE_TEXTURE =
            CommonClass.locate("textures/entity/canoe/spruce_canoe_with_chest.png")
        private val BIRCH_TEXTURE =
            CommonClass.locate("textures/entity/canoe/birch_canoe_with_chest.png")
        private val JUNGLE_TEXTURE =
            CommonClass.locate("textures/entity/canoe/jungle_canoe_with_chest.png")
        private val ACACIA_TEXTURE =
            CommonClass.locate("textures/entity/canoe/acacia_canoe_with_chest.png")
        private val CHERRY_TEXTURE =
            CommonClass.locate("textures/entity/canoe/cherry_canoe_with_chest.png")
        private val DARK_OAK_TEXTURE =
            CommonClass.locate("textures/entity/canoe/dark_oak_canoe_with_chest.png")
        private val MANGROVE_TEXTURE =
            CommonClass.locate("textures/entity/canoe/mangrove_canoe_with_chest.png")
        private val CRIMSON_TEXTURE =
            CommonClass.locate("textures/entity/canoe/crimson_canoe_with_chest.png")
        private val WARPED_TEXTURE =
            CommonClass.locate("textures/entity/canoe/warped_canoe_with_chest.png")
    }
}