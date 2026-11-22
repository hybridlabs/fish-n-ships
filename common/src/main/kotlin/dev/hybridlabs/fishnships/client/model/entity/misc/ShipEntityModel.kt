package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.minecraft.resources.Identifier
import software.bernie.geckolib.renderer.base.GeoRenderState

class ShipEntityModel<T : ShipEntity>() : FSVehicleModel<T>(
    "entity/ship/ship",
    "entity/ship/ship",
    "ship",
    "ship",
) {

    override fun getTextureResource(renderState: GeoRenderState): Identifier {
        return CommonClass.locate("textures/entity/ship/ship.png")
    }

    fun getIcebreakerTextureResource(): Identifier {
        return CommonClass.locate("textures/entity/ship/icebreaker.png")
    }

    fun getTrawlingNetTextureResource(): Identifier {
        return CommonClass.locate("textures/entity/ship/trawling_net.png")
    }

    fun getGlassTextureResource(): Identifier {
        return CommonClass.locate("textures/entity/ship/glass.png")
    }
}
