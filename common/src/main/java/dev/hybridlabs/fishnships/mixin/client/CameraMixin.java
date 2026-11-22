package dev.hybridlabs.fishnships.mixin.client;

import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Entity entity;

    @Shadow
    private boolean detached;

    @Shadow
    private float getMaxZoom(float startingDistance) {
        throw new AssertionError();
    }

    @Shadow
    protected abstract void move(float distanceOffset, float verticalOffset, float horizontalOffset);

    @Inject(
		method = "alignWithEntity",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", shift = At.Shift.AFTER)
	)
	private void changeCameraPosInShip(float partialTick, CallbackInfo ci) {
		Entity cameraEntity = this.entity;
		if (!this.detached && cameraEntity.getRootVehicle() != cameraEntity && cameraEntity.getRootVehicle() instanceof ShipEntity ship) {
			var shipRotationRadX = Math.toRadians(ship.getRotationVector().x);
			this.move((float)(shipRotationRadX * 0.25), (float)(Math.abs(shipRotationRadX * 0.25)), 0.0f);
		}
	}

    @ModifyArg(
            method = "alignWithEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"), index = 0
    )
    private float changeCameraDistance(float original) {
        return entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof ShipEntity ? 7.5f : original;
    }

    @ModifyArg(
            method = "alignWithEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;move(FFF)V"), index = 1
    )
    private float changeCameraHeight(float original) {
        return entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof ShipEntity ? getMaxZoom(3.0f) : original;
    }
}
