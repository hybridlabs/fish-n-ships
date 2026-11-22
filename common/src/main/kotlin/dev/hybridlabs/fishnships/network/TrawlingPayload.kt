package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.CommonClass
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class TrawlingPayload(val trawling: Boolean) : CustomPacketPayload {

    companion object {
        val TRAWLING_PAYLOAD_TYPE: Identifier = CommonClass.locate("trawling")
        val type: CustomPacketPayload.Type<TrawlingPayload> =
            CustomPacketPayload.Type(TRAWLING_PAYLOAD_TYPE)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, TrawlingPayload> =
            StreamCodec.composite(
                ByteBufCodecs.BOOL, TrawlingPayload::trawling,
                ::TrawlingPayload
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}