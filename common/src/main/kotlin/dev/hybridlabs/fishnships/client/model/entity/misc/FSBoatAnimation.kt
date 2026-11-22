package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.CanoeEntity
import dev.hybridlabs.fishnships.entity.vehicle.CustomBoatEntity
import dev.hybridlabs.fishnships.entity.vehicle.DinghyEntity
import dev.hybridlabs.fishnships.client.model.FSRenderData
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import software.bernie.geckolib.animation.state.BoneSnapshot
import software.bernie.geckolib.renderer.base.BoneSnapshots
import software.bernie.geckolib.renderer.base.GeoRenderState
import software.bernie.geckolib.renderer.base.RenderPassInfo

/**
 * Captures and applies the vehicle-specific bone poses that GeckoLib 4 models used to set in
 * `setCustomAnimations`. GeckoLib 5 only exposes animations to the renderer, so the body yaw and
 * paddle rowing times are captured on the render state and applied to bone snapshots.
 */
object FSBoatAnimation {

    fun capture(animatable: Entity, renderState: GeoRenderState, partialTick: Float) {
        when (animatable) {
            is CanoeEntity -> capturePaddles(
                animatable.getRowingTime(0, partialTick),
                animatable.getRowingTime(1, partialTick),
                renderState
            )

            is CustomBoatEntity -> capturePaddles(
                animatable.getRowingTime(0, partialTick),
                animatable.getRowingTime(1, partialTick),
                renderState
            )

            is DinghyEntity -> capturePaddles(
                animatable.getRowingTime(0, partialTick),
                animatable.getRowingTime(1, partialTick),
                renderState
            )
        }
    }

    private fun capturePaddles(left: Float, right: Float, renderState: GeoRenderState) {
        renderState.addGeckolibData(FSRenderData.PADDLE_LEFT, left)
        renderState.addGeckolibData(FSRenderData.PADDLE_RIGHT, right)
    }

    fun adjust(renderPassInfo: RenderPassInfo<EntityRenderState>, snapshots: BoneSnapshots) {
        val left = renderPassInfo.getGeckolibData(FSRenderData.PADDLE_LEFT) ?: return
        val right = renderPassInfo.getGeckolibData(FSRenderData.PADDLE_RIGHT) ?: return

        snapshots.get("paddle_left").ifPresent { posePaddle(it, left, 0) }
        snapshots.get("paddle_right").ifPresent { posePaddle(it, right, 1) }
    }

    private fun posePaddle(bone: BoneSnapshot, rowingTime: Float, side: Int) {
        val f = rowingTime + Math.PI.toFloat()

        val xAnim = Mth.clampedLerp(
            -Math.PI.toFloat() / 3f,
            -0.2617994f,
            ((Mth.sin(-f.toDouble()) + 1.0) / 2.0).toFloat()
        )

        val yAnim = Mth.clampedLerp(
            -Math.PI.toFloat() / 4f,
            Math.PI.toFloat() / 4f,
            (1.0 - (Mth.sin((-f + 1f).toDouble()) + 1.0) / 2.0).toFloat()
        )

        val rotX = Math.toRadians(-124.0).toFloat() + xAnim

        if (side == 0) {
            bone.setRotation(
                rotX,
                Math.toRadians(-90.0).toFloat() + yAnim,
                Math.toRadians(-165.0).toFloat()
            )
        } else {
            bone.setRotation(
                rotX,
                Math.toRadians(90.0).toFloat() - yAnim,
                Math.toRadians(165.0).toFloat()
            )
        }
    }
}
