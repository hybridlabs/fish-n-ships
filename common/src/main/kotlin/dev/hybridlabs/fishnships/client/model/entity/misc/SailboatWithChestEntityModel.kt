package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import dev.hybridlabs.fishnships.entity.vehicle.SailboatWithChestEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
class SailboatWithChestEntityModel<T : SailboatWithChestEntity>() :
    GeoModel<T>() {

    override fun getRenderType(animatable: T, texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucent(texture)
    }

    override fun getModelResource(animatable: T): ResourceLocation {
        return CommonClass.locate("geo/entity/sailboat/sailboat_with_chest.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return when (animatable.variant) {
            SailboatEntity.Type.OAK -> OAK_TEXTURE
            SailboatEntity.Type.SPRUCE -> SPRUCE_TEXTURE
            SailboatEntity.Type.BIRCH -> BIRCH_TEXTURE
            SailboatEntity.Type.JUNGLE -> JUNGLE_TEXTURE
            SailboatEntity.Type.ACACIA -> ACACIA_TEXTURE
            SailboatEntity.Type.CHERRY -> CHERRY_TEXTURE
            SailboatEntity.Type.DARK_OAK -> DARK_OAK_TEXTURE
            SailboatEntity.Type.MANGROVE -> MANGROVE_TEXTURE
            SailboatEntity.Type.CRIMSON -> CRIMSON_TEXTURE
            SailboatEntity.Type.WARPED -> WARPED_TEXTURE
            SailboatEntity.Type.DRIFTWOOD -> DRIFTWOOD_TEXTURE
        }
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return CommonClass.locate("animations/entity/sailboat/sailboat.animation.json")
    }

    override fun setCustomAnimations(
        animatable: T,
        instanceId: Long,
        animationState: AnimationState<T>,
    ) {
        val deltaTime: Float = animationState.partialTick
        val body = animationProcessor.getBone(PartNames.BODY)

        val yaw = Mth.rotLerp(deltaTime, animatable.yRotO, animatable.yRot)
        body.rotY = -yaw * Mth.DEG_TO_RAD
    }

    companion object {
        private val OAK_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/oak_sailboat.png")
        private val SPRUCE_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/spruce_sailboat.png")
        private val BIRCH_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/birch_sailboat.png")
        private val JUNGLE_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/jungle_sailboat.png")
        private val ACACIA_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/acacia_sailboat.png")
        private val CHERRY_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/cherry_sailboat.png")
        private val DARK_OAK_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/dark_oak_sailboat.png")
        private val MANGROVE_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/mangrove_sailboat.png")
        private val CRIMSON_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/crimson_sailboat.png")
        private val WARPED_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/warped_sailboat.png")
        private val DRIFTWOOD_TEXTURE =
            CommonClass.locate("textures/entity/sailboat/driftwood_sailboat.png")
    }
}