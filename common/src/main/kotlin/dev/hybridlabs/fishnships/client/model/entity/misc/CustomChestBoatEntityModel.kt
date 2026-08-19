package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.CustomBoatEntity
import dev.hybridlabs.fishnships.entity.vehicle.CustomChestBoatEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
class CustomChestBoatEntityModel<T : CustomChestBoatEntity>() :
    GeoModel<T>() {

    override fun getRenderType(animatable: T, texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucent(texture)
    }

    override fun getModelResource(animatable: T): ResourceLocation {
        return CommonClass.locate("geo/entity/boat/boat_with_chest.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return when (animatable.variant) {
            CustomBoatEntity.Type.CRIMSON -> CRIMSON_TEXTURE
            CustomBoatEntity.Type.WARPED -> WARPED_TEXTURE
            CustomBoatEntity.Type.DRIFTWOOD -> DRIFTWOOD_TEXTURE
        }
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return CommonClass.locate("animations/entity/boat/boat.animation.json")
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
        boat: CustomBoatEntity,
        side: Int,
        paddle: GeoBone,
        partialTick: Float
    ) {
        val f = boat.getRowingTime(side, partialTick) + Mth.PI

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
        private val CRIMSON_TEXTURE =
            CommonClass.locate("textures/entity/boat/crimson_boat.png")
        private val WARPED_TEXTURE =
            CommonClass.locate("textures/entity/boat/warped_boat.png")
        private val DRIFTWOOD_TEXTURE =
            CommonClass.locate("textures/entity/boat/driftwood_boat.png")
    }
}