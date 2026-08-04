package dev.hybridlabs.fishnships.platform.services;

import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity;
import dev.hybridlabs.fishnships.packet.C2SPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendTrawlingToServer(boolean enabled) {
        FriendlyByteBuf packetData = PacketByteBufs.create();
        packetData.writeBoolean(enabled);
        ResourceLocation packetId = C2SPackets.INSTANCE.getTRAWLING_PACKET_ID();
        if (ClientPlayNetworking.canSend(packetId))
            ClientPlayNetworking.send(packetId, packetData);
    }

    @Override
    public void sendMovementToServer(boolean moving) {
        FriendlyByteBuf packetData = PacketByteBufs.create();
        packetData.writeBoolean(moving);
        ResourceLocation packetId = C2SPackets.INSTANCE.getSHIP_MOVEMENT_PACKET_ID();
        if (ClientPlayNetworking.canSend(packetId))
            ClientPlayNetworking.send(packetId, packetData);
    }
    
    @Override
    public void changeSailState(SailboatEntity sailBoat) {
        FriendlyByteBuf packetData = PacketByteBufs.create();
        packetData.writeUUID(sailBoat.getUUID());
        ResourceLocation packetId = C2SPackets.INSTANCE.getCHANGE_SAIL_STATE_PACKET_IT();
        if (ClientPlayNetworking.canSend(packetId))
            ClientPlayNetworking.send(packetId, packetData);
    }
}