package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object FSNetworking {

    init {
        PayloadTypeRegistry.serverboundPlay().register(TrawlingPayload.type, TrawlingPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(ShipMovementPayload.type, ShipMovementPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(ChangeSailStatePayload.type, ChangeSailStatePayload.CODEC)
    }

    fun registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(TrawlingPayload.type) { payload, context ->
            val vehicle = context.player().controlledVehicle
            if (vehicle is ShipEntity) {
                vehicle.setTrawling(payload.trawling)
            }
        }

        ServerPlayNetworking.registerGlobalReceiver(ShipMovementPayload.type) { payload, context ->
            val vehicle = context.player().controlledVehicle
            if (vehicle is ShipEntity) {
                vehicle.setMoving(payload.moving)
            }
        }

        ServerPlayNetworking.registerGlobalReceiver(ChangeSailStatePayload.type) { payload, context ->
            val vehicle = context.player().controlledVehicle
            if (vehicle is SailboatEntity && vehicle.uuid == payload.vehicleUuid) {
                vehicle.setSailDown(!vehicle.isSailDown())
            }
        }
    }
}