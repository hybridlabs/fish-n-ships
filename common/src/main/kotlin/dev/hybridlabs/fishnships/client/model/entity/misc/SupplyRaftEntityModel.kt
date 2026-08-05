package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.RaftEntity
import dev.hybridlabs.fishnships.entity.vehicle.SupplyRaftEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
class SupplyRaftEntityModel<T : SupplyRaftEntity>() :
    GeoModel<T>() {

    override fun getRenderType(animatable: T, texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucent(texture)
    }

    override fun getModelResource(animatable: T): ResourceLocation {
        return CommonClass.locate("geo/entity/raft/supply_raft.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return when (animatable.variant) {
            RaftEntity.Type.OAK -> OAK_TEXTURE
            RaftEntity.Type.SPRUCE -> SPRUCE_TEXTURE
            RaftEntity.Type.BIRCH -> BIRCH_TEXTURE
            RaftEntity.Type.JUNGLE -> JUNGLE_TEXTURE
            RaftEntity.Type.ACACIA -> ACACIA_TEXTURE
            RaftEntity.Type.CHERRY -> CHERRY_TEXTURE
            RaftEntity.Type.DARK_OAK -> DARK_OAK_TEXTURE
            RaftEntity.Type.MANGROVE -> MANGROVE_TEXTURE
            RaftEntity.Type.CRIMSON -> CRIMSON_TEXTURE
            RaftEntity.Type.WARPED -> WARPED_TEXTURE
        }
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return CommonClass.locate("animations/entity/raft/raft.animation.json")
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
            CommonClass.locate("textures/entity/raft/oak_supply_raft.png")
        private val SPRUCE_TEXTURE =
            CommonClass.locate("textures/entity/raft/spruce_supply_raft.png")
        private val BIRCH_TEXTURE =
            CommonClass.locate("textures/entity/raft/birch_supply_raft.png")
        private val JUNGLE_TEXTURE =
            CommonClass.locate("textures/entity/raft/jungle_supply_raft.png")
        private val ACACIA_TEXTURE =
            CommonClass.locate("textures/entity/raft/acacia_supply_raft.png")
        private val CHERRY_TEXTURE =
            CommonClass.locate("textures/entity/raft/cherry_supply_raft.png")
        private val DARK_OAK_TEXTURE =
            CommonClass.locate("textures/entity/raft/dark_oak_supply_raft.png")
        private val MANGROVE_TEXTURE =
            CommonClass.locate("textures/entity/raft/mangrove_supply_raft.png")
        private val CRIMSON_TEXTURE =
            CommonClass.locate("textures/entity/raft/crimson_supply_raft.png")
        private val WARPED_TEXTURE =
            CommonClass.locate("textures/entity/raft/warped_supply_raft.png")
    }
}