package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.CustomChestBoatEntity

class CustomChestBoatEntityModel<T : CustomChestBoatEntity>() : FSVehicleModel<T>(
    "entity/boat/boat_with_chest",
    "entity/boat/boat",
    "boat",
    "boat",
    { it.getVariant().serializedName },
)
