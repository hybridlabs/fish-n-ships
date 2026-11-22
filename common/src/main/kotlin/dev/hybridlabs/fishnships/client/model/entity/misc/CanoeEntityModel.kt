package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.CanoeEntity

class CanoeEntityModel<T : CanoeEntity>() : FSVehicleModel<T>(
    "entity/canoe/canoe",
    "entity/canoe/canoe",
    "canoe",
    "canoe",
    { it.getVariant().serializedName },
)
