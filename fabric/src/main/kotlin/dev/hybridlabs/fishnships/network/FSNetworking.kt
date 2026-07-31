package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.entity.ship.ShipEntity
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
    }
}
