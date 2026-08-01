package dev.hybridlabs.fishnships.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.hybridlabs.fishnships.entity.ship.CanoeEntity;
import dev.hybridlabs.fishnships.entity.ship.SailboatEntity;
import dev.hybridlabs.fishnships.entity.ship.ShipEntity;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.Entity;
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