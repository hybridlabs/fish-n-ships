package dev.hybridlabs.fishnships.platform;

import dev.hybridlabs.fishnships.Constants;
import dev.hybridlabs.fishnships.network.FSNetworking;
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity;
import dev.hybridlabs.fishnships.platform.services.PlatformHelper;
import net.minecraft.world.entity.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLLoader;
import thedarkcolour.kotlinforforge.KotlinModContainer;

public class ForgePlatformHelper implements PlatformHelper {

    public static IEventBus getEventBus() {
        final ModContainer cont = ModList.get().getModContainerById(Constants.MOD_ID).orElseThrow();
        if (cont instanceof FMLModContainer fmlModContainer) {
            return fmlModContainer.getEventBus();
        } else if (cont instanceof KotlinModContainer kotlinModContainer) {
            return kotlinModContainer.getEventBus$kfflang();
        } else {
            throw new ClassCastException("The container of the mod " + Constants.MOD_ID + " is not a FML one!");
        }
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
        FSNetworking.INSTANCE.sendSailingPacket(sailBoat.isSailDown());
    }
}
