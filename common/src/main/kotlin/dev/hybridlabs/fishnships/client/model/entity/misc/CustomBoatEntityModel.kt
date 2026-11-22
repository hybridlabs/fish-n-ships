package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.CustomBoatEntity

class CustomBoatEntityModel<T : CustomBoatEntity>() : FSVehicleModel<T>(
    "entity/boat/boat",
    "entity/boat/boat",
    "boat",
    "boat",
    { it.getVariant().serializedName },
)
