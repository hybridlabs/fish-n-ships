package dev.hybridlabs.fishnships.client.render.entity.misc.layer

import dev.hybridlabs.fishnships.client.model.FSRenderData
import dev.hybridlabs.fishnships.client.model.entity.misc.ShipEntityModel
import dev.hybridlabs.fishnships.client.render.entity.misc.ShipEntityRenderer
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier
import software.bernie.geckolib.renderer.layer.builtin.TextureLayerGeoLayer

class ShipTrawlingNetEntityLayer<T : ShipEntity>(
    renderer: ShipEntityRenderer<T>,
) : TextureLayerGeoLayer<T, Void, EntityRenderState>(renderer, PLACEHOLDER_TEXTURE) {

    override fun getTextureResource(renderState: EntityRenderState): Identifier {
        return (geoModel as ShipEntityModel<*>).getTrawlingNetTextureResource()
    }

    override fun getRenderType(renderState: EntityRenderState): RenderType? {
        if (renderState.getGeckolibData(FSRenderData.HAS_TRAWLING_NET) != true) return null

        return RenderTypes.entityTranslucent(getTextureResource(renderState))
    }

    companion object {
        private val PLACEHOLDER_TEXTURE = Identifier.withDefaultNamespace("textures/misc/white.png")
    }
}
