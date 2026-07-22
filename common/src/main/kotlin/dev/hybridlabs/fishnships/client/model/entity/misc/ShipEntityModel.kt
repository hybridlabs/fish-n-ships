package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.ship.ShipEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
class ShipEntityModel<T : ShipEntity>() :
    GeoModel<T>() {

    override fun getRenderType(animatable: T, texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucent(texture)
    }

    override fun getModelResource(animatable: T): ResourceLocation {
        return CommonClass.locate("geo/entity/ship/ship.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return CommonClass.locate("textures/entity/ship/ship.png")
    }

    fun getFlagTextureResource(animatable: T): ResourceLocation {
        val textureName = when (val color = animatable.getFlagColor()) {
            ShipEntity.FlagColor.NONE -> "ship_flag"
            else -> color.name.lowercase() + "_ship_flag"
        }
        return CommonClass.locate("textures/entity/ship/flag/$textureName.png")
    }

    fun getIcebreakerTextureResource(): ResourceLocation {
        return CommonClass.locate("textures/entity/ship/icebreaker.png")
    }

    fun getTrawlingNetTextureResource(): ResourceLocation {
        return CommonClass.locate("textures/entity/ship/trawling_net.png")
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return CommonClass.locate("animations/entity/ship/ship.animation.json")
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
}