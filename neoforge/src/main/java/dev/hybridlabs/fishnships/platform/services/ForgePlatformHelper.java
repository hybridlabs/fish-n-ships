package dev.hybridlabs.fishnships.platform.services;

import dev.hybridlabs.fishnships.Constants;
import dev.hybridlabs.fishnships.network.FSNetworking;
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class ForgePlatformHelper implements PlatformHelper {

    public static IEventBus getEventBus() {
        final ModContainer cont =
                ModList.get().getModContainerById(Constants.MOD_ID).orElseThrow();
        return cont.getEventBus();
    }

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public void sendTrawlingToServer(boolean enabled) {
        FSNetworking.INSTANCE.sendTrawlingPacket(enabled);
    }

    @Override
    public void sendMovementToServer(boolean moving) {
        FSNetworking.INSTANCE.sendMovingPacket(moving);
    }

    @Override
    public void changeSailState(SailboatEntity sailBoat) {
        FSNetworking.INSTANCE.sendSailingPacket(sailBoat.getUUID());
    }
}
