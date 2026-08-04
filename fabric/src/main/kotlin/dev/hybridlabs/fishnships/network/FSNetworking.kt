package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import dev.hybridlabs.fishnships.packet.C2SPackets
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object FSNetworking {

    fun registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(C2SPackets.TRAWLING_PACKET_ID) { _, client, _, buf, _ ->
            val vehicle = client.controlledVehicle
            if (vehicle != null && vehicle is ShipEntity) {
                vehicle.setTrawling(buf.readBoolean())
            }
        }

        ServerPlayNetworking.registerGlobalReceiver(C2SPackets.SHIP_MOVEMENT_PACKET_ID) { _, client, _, buf, _ ->
            val vehicle = client.controlledVehicle
            if (vehicle != null && vehicle is ShipEntity) {
                vehicle.setMoving(buf.readBoolean())
            }
        }

        ServerPlayNetworking.registerGlobalReceiver(C2SPackets.CHANGE_SAIL_STATE_PACKET_IT) { _, serverPlayer, _, buf, _ ->
            val vehicle = serverPlayer.controlledVehicle
            val vehicleUUID = buf.readUUID()
            if (vehicle != null && vehicle is SailboatEntity && vehicle.uuid == vehicleUUID) {
                vehicle.setSailDown(!vehicle.isSailDown())
            }
        }
    }
}
