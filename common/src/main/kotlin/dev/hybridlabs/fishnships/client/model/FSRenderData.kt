package dev.hybridlabs.fishnships.client.model

import software.bernie.geckolib.constant.dataticket.DataTicket

/**
 * Model-local render state data. GeckoLib 5 models and render layers only receive a render state,
 * so values a vehicle exposes for resource selection or bone posing are captured while the entity
 * is still available and read back through these tickets.
 */
object FSRenderData {
    /** The serialized name of a vehicle's wood variant. */
    @JvmField
    val VARIANT_NAME: DataTicket<String> = DataTicket.create("variant_name", String::class.java)

    /** Whether a dinghy is using its alternate model. */
    @JvmField
    val ALTERNATE: DataTicket<Boolean> = DataTicket.create("alternate", Boolean::class.java)

    /** The rowing time of the left paddle, or NaN when the vehicle has no paddles. */
    @JvmField
    val PADDLE_LEFT: DataTicket<Float> = DataTicket.create("paddle_left", Float::class.java)

    /** The rowing time of the right paddle, or NaN when the vehicle has no paddles. */
    @JvmField
    val PADDLE_RIGHT: DataTicket<Float> = DataTicket.create("paddle_right", Float::class.java)

    /** Whether a ship has an icebreaker installed. */
    @JvmField
    val HAS_ICEBREAKER: DataTicket<Boolean> = DataTicket.create("has_icebreaker", Boolean::class.java)

    /** Whether a ship has a trawling net installed. */
    @JvmField
    val HAS_TRAWLING_NET: DataTicket<Boolean> = DataTicket.create("has_trawling_net", Boolean::class.java)
}
