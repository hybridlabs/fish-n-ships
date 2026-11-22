package dev.hybridlabs.fishnships.client.model.entity.misc

import dev.hybridlabs.fishnships.entity.vehicle.RaftEntity

class RaftEntityModel<T : RaftEntity>() : FSVehicleModel<T>(
    "entity/raft/raft",
    "entity/raft/raft",
    "raft",
    "raft",
    { it.getVariant().serializedName },
)
