package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.CanoeWithChestEntity

class CanoeWithChestEntityModel<T : CanoeWithChestEntity>() : FSVehicleModel<T>(
    "entity/canoe/canoe_with_chest",
    "entity/canoe/canoe",
    "canoe",
    "canoe",
    { it.getVariant().serializedName },
)
