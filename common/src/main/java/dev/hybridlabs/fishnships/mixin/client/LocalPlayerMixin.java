package dev.hybridlabs.fishnships.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.hybridlabs.fishnships.entity.vehicle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    public Input input;
    
    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }
    
    
    @Inject(method = "rideTick", at = @At("TAIL"))
    private void handleShipInput(CallbackInfo ci) {
        if (this.getControlledVehicle() instanceof ShipEntity ship) {

            boolean sprint = Minecraft.getInstance().options.keySprint.isDown();

            ship.setInput(
                    this.input.left,
                    this.input.right,
                    this.input.up,
                    this.input.down,
                    this.input.jumping,
                    sprint
            );
        }
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void handleCustomBoatInput(CallbackInfo ci) {
        if (this.getControlledVehicle() instanceof CustomBoatEntity customBoat) {

            customBoat.setInput(
                    this.input.left,
                    this.input.right,
                    this.input.up,
                    this.input.down
            );
        }
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void handleCanoeInput(CallbackInfo ci) {
        if (this.getControlledVehicle() instanceof CanoeEntity canoe) {

            canoe.setInput(
                    this.input.left,
                    this.input.right,
                    this.input.up,
                    this.input.down
            );
        }
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void handleDinghyInput(CallbackInfo ci) {
        if (this.getControlledVehicle() instanceof DinghyEntity dinghy) {

            dinghy.setInput(
                    this.input.left,
                    this.input.right,
                    this.input.up,
                    this.input.down
            );
        }
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void handleSailboatInput(CallbackInfo ci) {
        if (this.getControlledVehicle() instanceof SailboatEntity canoe) {

            canoe.setInput(
                    this.input.left,
                    this.input.right,
                    this.input.jumping
            );
        }
    }
}