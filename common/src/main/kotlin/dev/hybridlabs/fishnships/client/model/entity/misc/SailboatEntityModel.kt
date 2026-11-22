package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity

class SailboatEntityModel<T : SailboatEntity>() : FSVehicleModel<T>(
    "entity/sailboat/sailboat",
    "entity/sailboat/sailboat",
    "sailboat",
    "sailboat",
    { it.getVariant().serializedName },
)
