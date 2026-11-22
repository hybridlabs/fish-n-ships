package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.CanoeWithDoubleChestEntity

class CanoeWithDoubleChestEntityModel<T : CanoeWithDoubleChestEntity>() : FSVehicleModel<T>(
    "entity/canoe/canoe_with_double_chest",
    "entity/canoe/canoe",
    "canoe",
    "canoe",
    { it.getVariant().serializedName },
)
