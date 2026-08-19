package dev.hybridlabs.fishnships.network

import dev.hybridlabs.fishnships.CommonClass
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import java.util.*

data class ChangeSailStatePayload(val vehicleUuid: UUID) : CustomPacketPayload {

    companion object {
        val CHANGE_SAIL_STATE_PAYLOAD_TYPE: ResourceLocation = CommonClass.locate("change_sail_state")
        val type: CustomPacketPayload.Type<ChangeSailStatePayload> =
            CustomPacketPayload.Type(CHANGE_SAIL_STATE_PAYLOAD_TYPE)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ChangeSailStatePayload> =
            StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, ChangeSailStatePayload::vehicleUuid,
                ::ChangeSailStatePayload
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}