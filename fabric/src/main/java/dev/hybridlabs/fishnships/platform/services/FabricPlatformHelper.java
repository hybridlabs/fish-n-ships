package dev.hybridlabs.fishnships.platform.services;

import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity;
import dev.hybridlabs.fishnships.network.ChangeSailStatePayload;
import dev.hybridlabs.fishnships.network.ShipMovementPayload;
import dev.hybridlabs.fishnships.network.TrawlingPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
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
        ClientPlayNetworking.send(new TrawlingPayload(enabled));
    }

    @Override
    public void sendMovementToServer(boolean moving) {
        ClientPlayNetworking.send(new ShipMovementPayload(moving));
    }

    @Override
    public void changeSailState(SailboatEntity sailBoat) {
        ClientPlayNetworking.send(new ChangeSailStatePayload(sailBoat.getUUID()));
    }
}