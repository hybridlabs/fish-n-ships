package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.client.model.FSRenderData
import net.minecraft.resources.Identifier
import com.geckolib.animatable.GeoAnimatable
import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState

/**
 * Base model for every Fish N' Ships vehicle. The vehicle's variant is captured as its serialized
 * name, which maps directly onto `textures/entity/<dir>/<variant>_<suffix>.png`.
 */
abstract class FSVehicleModel<T : GeoAnimatable>(
    private val modelPath: String,
    private val animationPath: String,
    private val textureDir: String,
    private val textureSuffix: String,
    private val variantName: ((T) -> String)? = null,
) : GeoModel<T>() {

    override fun addAdditionalStateData(animatable: T, relatedObject: Any?, renderState: GeoRenderState) {
        variantName?.let { renderState.addGeckolibData(FSRenderData.VARIANT_NAME, it(animatable)) }
    }

    override fun getModelResource(renderState: GeoRenderState): Identifier {
        return CommonClass.locate(modelPath)
    }

    override fun getTextureResource(renderState: GeoRenderState): Identifier {
        return texture(renderState)
    }

    override fun getAnimationResource(animatable: T): Identifier {
        return CommonClass.locate(animationPath)
    }

    protected fun texture(renderState: GeoRenderState, prefix: String = ""): Identifier {
        val variant = renderState.getGeckolibData(FSRenderData.VARIANT_NAME) ?: "oak"

        return CommonClass.locate("textures/entity/$textureDir/$prefix${variant}_$textureSuffix.png")
    }
}
