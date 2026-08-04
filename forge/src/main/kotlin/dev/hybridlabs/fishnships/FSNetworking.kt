package dev.hybridlabs.fishnships

import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.game.ServerPacketListener
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.Supplier

object FSNetworking {
    private const val PROTOCOL_VERSION = "1"
    val CHANNEL: SimpleChannel = NetworkRegistry.newSimpleChannel(
        CommonClass.locate("main"),
        { PROTOCOL_VERSION },
        { anObject: String? -> PROTOCOL_VERSION == anObject },
        { anObject: String? -> PROTOCOL_VERSION == anObject })
    var messageId: Int = 0

    @Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
    fun registerPackets() {
        CHANNEL.registerMessage(
            messageId++,
            TrawlingPacket::class.java,
            { obj: TrawlingPacket?, buffer: FriendlyByteBuf? -> obj!!.encoder(buffer!!) },
            { buffer: FriendlyByteBuf? -> TrawlingPacket(buffer!!) },
            { obj: TrawlingPacket?, ctx: Supplier<NetworkEvent.Context?>? -> obj!!.handle(ctx!!) })
        CHANNEL.registerMessage(
            messageId++,
            MovingPacket::class.java,
            { obj: MovingPacket?, buffer: FriendlyByteBuf? -> obj!!.encoder(buffer!!) },
            { buffer: FriendlyByteBuf? -> MovingPacket(buffer!!) },
            { obj: MovingPacket?, ctx: Supplier<NetworkEvent.Context?>? -> obj!!.handle(ctx!!) })
    }

    fun sendTrawlingPacket(trawling: Boolean) {
        CHANNEL.sendToServer(TrawlingPacket(trawling))
    }

    fun sendMovingPacket(trawling: Boolean) {
        CHANNEL.sendToServer(MovingPacket(trawling))
    }

    fun handle(msg: MovingPacket, ctx: Supplier<NetworkEvent.Context?>) {
        ctx.get()!!.enqueueWork { handleMovingPacket(msg, ctx) }
        ctx.get()!!.packetHandled = true
    }

    fun handle(msg: TrawlingPacket, ctx: Supplier<NetworkEvent.Context?>) {
        ctx.get()!!.enqueueWork { handleTrawlingPacket(msg, ctx) }
        ctx.get()!!.packetHandled = true
    }

    fun handleTrawlingPacket(packet: TrawlingPacket, ctx: Supplier<NetworkEvent.Context?>) {
        val listener = ctx.get()!!.networkManager.packetListener
        if (listener is ServerPacketListener) {
            ctx.get()!!.enqueueWork {
                val sender = ctx.get()!!.sender
                val vehicle = sender!!.controlledVehicle
                if (vehicle != null && vehicle is ShipEntity) {
                    vehicle.setTrawling(packet.trawling)
                }
            }
        }
    }

    fun handleMovingPacket(packet: MovingPacket, ctx: Supplier<NetworkEvent.Context?>) {
        val listener = ctx.get()!!.networkManager.packetListener
        if (listener is ServerPacketListener) {
            ctx.get()!!.enqueueWork {
                val sender = ctx.get()!!.sender
                val vehicle = sender!!.controlledVehicle
                if (vehicle != null && vehicle is ShipEntity) {
                    vehicle.setMoving(packet.moving)
                }
            }
        }
    }

    class TrawlingPacket {
        var trawling: Boolean = false

        constructor(trawling: Boolean) {
            this.trawling = trawling
        }

        constructor(buffer: FriendlyByteBuf) {
            this.trawling = buffer.readBoolean()
        }

        fun encoder(buffer: FriendlyByteBuf) {
            buffer.writeBoolean(trawling)
        }

        fun handle(ctx: Supplier<NetworkEvent.Context?>) {
            ctx.get()!!.enqueueWork { handle(this, ctx) }
            ctx.get()!!.packetHandled = true
        }
    }

    class MovingPacket {
        var moving: Boolean = false

        constructor(moving: Boolean) {
            this.moving = moving
        }

        constructor(buffer: FriendlyByteBuf) {
            this.moving = buffer.readBoolean()
        }

        fun encoder(buffer: FriendlyByteBuf) {
            buffer.writeBoolean(moving)
        }

        fun handle(ctx: Supplier<NetworkEvent.Context?>) {
            ctx.get()!!.enqueueWork { handle(this, ctx) }
            ctx.get()!!.packetHandled = true
        }
    }
}