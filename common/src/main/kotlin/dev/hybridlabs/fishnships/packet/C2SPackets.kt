package dev.hybridlabs.fishnships.packet

import dev.hybridlabs.fishnships.CommonClass
import net.minecraft.resources.ResourceLocation

object C2SPackets {
    val TRAWLING_PACKET_ID: ResourceLocation = CommonClass.locate("ship_trawling")
    val SHIP_MOVEMENT_PACKET_ID: ResourceLocation = CommonClass.locate("ship_moving")
    val CHANGE_SAIL_STATE_PACKET_IT: ResourceLocation = CommonClass.locate("change_sail_state")
}