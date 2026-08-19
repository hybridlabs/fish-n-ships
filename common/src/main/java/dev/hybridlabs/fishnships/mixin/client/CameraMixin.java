package dev.hybridlabs.fishnships.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.hybridlabs.fishnships.entity.vehicle.ShipEntity;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Entity entity;

    @Shadow
    protected abstract double getMaxZoom(double startingDistance);

    @Shadow
    protected abstract void move(double distanceOffset, double verticalOffset, double horizontalOffset);

    @Inject(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", shift = At.Shift.AFTER)
    )
    private void changeCameraPosInShip(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!detached && entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof ShipEntity ship) {
            var shipRotationRadX = Math.toRadians(ship.getRotationVector().x);
            this.move(shipRotationRadX * 0.25, Math.abs(shipRotationRadX * 0.25), 0.0);
        }
    }

    @ModifyArg(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D"), index = 0
    )
    private double changeCameraDistance(double original) {
        return entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof ShipEntity ? 7.5d : original;
    }

    @ModifyExpressionValue(
            method = "setup",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D", ordinal = 0),
                    to = @At(value = "TAIL")),
            at = @At(value = "CONSTANT", args = "doubleValue=0.0", ordinal = 0)
    )
    private double changeCameraHeight(double original) {
        return entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof ShipEntity ? getMaxZoom(3.0) : original;
    }
}