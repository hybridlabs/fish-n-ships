package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

data class TrawlingPacket(val trawling: Boolean) : CustomPacketPayload {

    override fun type() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<TrawlingPacket>(
            CommonClass.locate("trawling")
        )

        val STREAM_CODEC = StreamCodec.of(
            { buf: RegistryFriendlyByteBuf, packet ->
                buf.writeBoolean(packet.trawling)
            },
            { buf: RegistryFriendlyByteBuf ->
                TrawlingPacket(buf.readBoolean())
            }
        )

        fun handle(packet: TrawlingPacket, context: IPayloadContext) {
            val player = context.player()

            context.enqueueWork {
                val vehicle = player.controlledVehicle

                if (vehicle is ShipEntity) {
                    vehicle.setTrawling(packet.trawling)
                }
            }
        }
    }
}