package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

data class MovingPacket(val moving: Boolean) : CustomPacketPayload {

    override fun type() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<MovingPacket>(
            CommonClass.locate("moving")
        )

        val STREAM_CODEC = StreamCodec.of(
            { buf: RegistryFriendlyByteBuf, packet ->
                buf.writeBoolean(packet.moving)
            },
            { buf: RegistryFriendlyByteBuf ->
                MovingPacket(buf.readBoolean())
            }
        )

        fun handle(packet: MovingPacket, context: IPayloadContext) {
            val player = context.player()

            context.enqueueWork {
                val vehicle = player.controlledVehicle

                if (vehicle is ShipEntity) {
                    vehicle.setMoving(packet.moving)
                }
            }
        }
    }
}