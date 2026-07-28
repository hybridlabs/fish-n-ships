package dev.hybridlabs.fishnships.client.render.entity.misc

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.hybridlabs.fishnships.client.model.entity.misc.SupplyRaftEntityModel
import dev.hybridlabs.fishnships.entity.ship.RaftEntity
import dev.hybridlabs.fishnships.entity.ship.SupplyRaftEntity
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.LightLayer
import org.joml.Matrix4f
import software.bernie.geckolib.renderer.GeoEntityRenderer
import kotlin.math.cos
import kotlin.math.sin

class SupplyRaftEntityRenderer<T : SupplyRaftEntity>(
    context: EntityRendererProvider.Context,
) : GeoEntityRenderer<T>(context, SupplyRaftEntityModel()) {
    val LEASH_RENDER_STEPS: Int = 24

    init {
        this.shadowRadius = 0.3f
    }

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }

    override fun shouldRender(livingEntity: T, camera: Frustum, camX: Double, camY: Double, camZ: Double): Boolean {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true
        }

        val leashHolder = livingEntity.getLeashHolder()
        return leashHolder != null && camera.isVisible(leashHolder.boundingBoxForCulling)
    }

    override fun render(
        entity: T,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)

        entity.getLeashHolder()?.let {
            renderLeash(entity, partialTick, poseStack, bufferSource, it)
        }
    }

    private fun getBlockLight(entity: Entity, pos: BlockPos): Int {
        return if (entity.isOnFire) 15 else entity.level().getBrightness(LightLayer.BLOCK, pos)
    }

    private fun <E : Entity?> renderLeash(
        entityLiving: T,
        partialTicks: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        leashHolder: E,
    ) {
        poseStack.pushPose()
        val vec3 = leashHolder!!.getRopeHoldPosition(partialTicks)
        val d0 = (Mth.lerp(
            partialTicks,
            entityLiving.yRotO,
            entityLiving.yRot
        ) * (Math.PI.toFloat() / 180f)).toDouble() + (Math.PI / 2.0)
        val vec31 = entityLiving.getLeashOffset(partialTicks)
        val d1 = cos(d0) * vec31.z + sin(d0) * vec31.x
        val d2 = sin(d0) * vec31.z - cos(d0) * vec31.x
        val d3 = Mth.lerp(partialTicks.toDouble(), entityLiving.xo, entityLiving.x) + d1
        val d4 = Mth.lerp(partialTicks.toDouble(), entityLiving.yo, entityLiving.y) + vec31.y
        val d5 = Mth.lerp(partialTicks.toDouble(), entityLiving.zo, entityLiving.z) + d2
        poseStack.translate(d1, vec31.y, d2)
        val f = (vec3.x - d3).toFloat()
        val f1 = (vec3.y - d4).toFloat()
        val f2 = (vec3.z - d5).toFloat()
        val f3 = 0.025f
        val vertexconsumer = buffer.getBuffer(RenderType.leash())
        val matrix4f = poseStack.last().pose()
        val f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025f / 2.0f
        val f5 = f2 * f4
        val f6 = f * f4
        val blockpos = BlockPos.containing(entityLiving.getEyePosition(partialTicks))
        val blockpos1 = BlockPos.containing(leashHolder.getEyePosition(partialTicks))
        val i = getBlockLight(entityLiving, blockpos)
        val j = getBlockLight(leashHolder, blockpos1)
        val k = entityLiving.level().getBrightness(LightLayer.SKY, blockpos)
        val l = entityLiving.level().getBrightness(LightLayer.SKY, blockpos1)

        for (i1 in 0..24) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025f, 0.025f, f5, f6, i1, false)
        }

        for (j1 in 24 downTo 0) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025f, 0.0f, f5, f6, j1, true)
        }

        poseStack.popPose()
    }

    private fun addVertexPair(
        consumer: VertexConsumer,
        matrix: Matrix4f,
        p_174310_: Float,
        p_174311_: Float,
        p_174312_: Float,
        entityBlockLightLevel: Int,
        leashHolderBlockLightLevel: Int,
        entitySkyLightLevel: Int,
        leashHolderSkyLightLevel: Int,
        p_174317_: Float,
        p_174318_: Float,
        p_174319_: Float,
        p_174320_: Float,
        index: Int,
        p_174322_: Boolean,
    ) {
        val f = index.toFloat() / 24.0f
        val i = Mth.lerp(f, entityBlockLightLevel.toFloat(), leashHolderBlockLightLevel.toFloat()).toInt()
        val j = Mth.lerp(f, entitySkyLightLevel.toFloat(), leashHolderSkyLightLevel.toFloat()).toInt()
        val k = LightTexture.pack(i, j)
        val f1 = if (index % 2 == (if (p_174322_) 1 else 0)) 0.7f else 1.0f
        val f2 = 0.5f * f1
        val f3 = 0.4f * f1
        val f4 = 0.3f * f1
        val f5 = p_174310_ * f
        val f6 = if (p_174311_ > 0.0f) p_174311_ * f * f else p_174311_ - p_174311_ * (1.0f - f) * (1.0f - f)
        val f7 = p_174312_ * f
        consumer.vertex(matrix, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0f).uv2(k)
            .endVertex()
        consumer.vertex(matrix, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0f)
            .uv2(k).endVertex()
    }
}