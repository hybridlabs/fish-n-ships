package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.SupplyRaftEntity

class SupplyRaftEntityModel<T : SupplyRaftEntity>() : FSVehicleModel<T>(
    "entity/raft/supply_raft",
    "entity/raft/raft",
    "raft",
    "raft",
    { it.getVariant().serializedName },
)
