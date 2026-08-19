package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.CommonClass
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

data class ShipMovementPayload(val moving: Boolean) : CustomPacketPayload {

    companion object {
        val SHIP_MOVEMENT_PAYLOAD_TYPE: ResourceLocation = CommonClass.locate("ship_movement")
        val type: CustomPacketPayload.Type<ShipMovementPayload> =
            CustomPacketPayload.Type(SHIP_MOVEMENT_PAYLOAD_TYPE)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ShipMovementPayload> =
            StreamCodec.composite(
                ByteBufCodecs.BOOL, ShipMovementPayload::moving,
                ::ShipMovementPayload
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}