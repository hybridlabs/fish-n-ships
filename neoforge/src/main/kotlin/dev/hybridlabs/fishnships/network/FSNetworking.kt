package dev.hybridlabs.fishnships.network

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import java.util.*

object FSNetworking {

    private const val PROTOCOL_VERSION = "1"

    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(PROTOCOL_VERSION)

        registrar.playToServer(
            MovingPacket.TYPE,
            MovingPacket.STREAM_CODEC,
            MovingPacket::handle
        )

        registrar.playToServer(
            TrawlingPacket.TYPE,
            TrawlingPacket.STREAM_CODEC,
            TrawlingPacket::handle
        )

        registrar.playToServer(
            SailingPacket.TYPE,
            SailingPacket.STREAM_CODEC,
            SailingPacket::handle
        )
    }

    fun sendMovingPacket(moving: Boolean) {
        PacketDistributor.sendToServer(MovingPacket(moving))
    }

    fun sendTrawlingPacket(trawling: Boolean) {
        PacketDistributor.sendToServer(TrawlingPacket(trawling))
    }

    fun sendSailingPacket(uuid: UUID) {
        PacketDistributor.sendToServer(SailingPacket(uuid))
    }
}