package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.SailboatWithChestEntity

class SailboatWithChestEntityModel<T : SailboatWithChestEntity>() : FSVehicleModel<T>(
    "entity/sailboat/sailboat_with_chest",
    "entity/sailboat/sailboat",
    "sailboat",
    "sailboat",
    { it.getVariant().serializedName },
)
