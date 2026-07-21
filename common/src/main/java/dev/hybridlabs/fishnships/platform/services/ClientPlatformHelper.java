package dev.hybridlabs.fishnships.platform.services;

import dev.hybridlabs.fishnships.platform.registration.RegistryObject;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface ClientPlatformHelper {
    <E extends Entity> void registerEntityRenderer(
            RegistryObject<EntityType<E>> entityType,
            EntityRendererProvider<E> entityRendererFactory);
}
