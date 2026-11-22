package dev.hybridlabs.fishnships.client.render.entity.misc.layer

import dev.hybridlabs.fishnships.client.render.entity.misc.FSVehicleRenderer
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import com.geckolib.animatable.GeoAnimatable
import com.geckolib.renderer.layer.builtin.TextureLayerGeoLayer

class WaterPatchEntityLayer<T>(
    renderer: FSVehicleRenderer<T>,
) : TextureLayerGeoLayer<T, Void, EntityRenderState>(renderer, PLACEHOLDER_TEXTURE)
    where T : Entity, T : GeoAnimatable {

    override fun getRenderType(renderState: EntityRenderState): RenderType {
        return RenderTypes.waterMask()
    }

    companion object {
        private val PLACEHOLDER_TEXTURE = Identifier.withDefaultNamespace("textures/misc/white.png")
    }
}
