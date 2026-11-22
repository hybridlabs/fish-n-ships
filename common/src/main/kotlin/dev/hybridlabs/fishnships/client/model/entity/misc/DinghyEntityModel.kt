package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.client.model.FSRenderData
import dev.hybridlabs.fishnships.entity.vehicle.DinghyEntity
import net.minecraft.resources.Identifier
import software.bernie.geckolib.renderer.base.GeoRenderState

class DinghyEntityModel<T : DinghyEntity>() : FSVehicleModel<T>(
    "entity/dinghy/dinghy",
    "entity/dinghy/dinghy",
    "dinghy",
    "dinghy",
    { it.getVariant().serializedName },
) {

    override fun addAdditionalStateData(animatable: T, relatedObject: Any?, renderState: GeoRenderState) {
        super.addAdditionalStateData(animatable, relatedObject, renderState)
        renderState.addGeckolibData(FSRenderData.ALTERNATE, animatable.alternate)
    }

    override fun getModelResource(renderState: GeoRenderState): Identifier {
        return if (renderState.getGeckolibData(FSRenderData.ALTERNATE) == true) {
            CommonClass.locate("entity/dinghy/alt_dinghy")
        } else {
            super.getModelResource(renderState)
        }
    }

    override fun getTextureResource(renderState: GeoRenderState): Identifier {
        return texture(renderState, if (renderState.getGeckolibData(FSRenderData.ALTERNATE) == true) "alt_" else "")
    }
}
