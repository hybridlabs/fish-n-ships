package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.UUID

data class SailingPacket(val sailing: UUID) : CustomPacketPayload {

    override fun type() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<SailingPacket>(
            CommonClass.locate("sailing")
        )

        val STREAM_CODEC = StreamCodec.of(
            { buf: RegistryFriendlyByteBuf, packet ->
                buf.writeUUID(packet.sailing)
            },
            { buf: RegistryFriendlyByteBuf ->
                SailingPacket(buf.readUUID())
            }
        )

        fun handle(packet: SailingPacket, context: IPayloadContext) {
            val player = context.player()

            context.enqueueWork {
                val vehicle = player.controlledVehicle

                if (vehicle is SailboatEntity && vehicle.uuid == packet.sailing) {
                    vehicle.setSailDown(!vehicle.isSailDown())
                }
            }
        }
    }
}