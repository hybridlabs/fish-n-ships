package dev.hybridlabs.fishnships.mixin;

import dev.hybridlabs.fishnships.Constants;
import dev.hybridlabs.fishnships.entity.LegacyVehicleIds;
import java.util.Optional;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin {
	@Inject(
			method = "by",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void fishnships$resolveLegacyVehicleId(
			ValueInput input,
			CallbackInfoReturnable<Optional<EntityType<?>>> cir
	) {
		// One field read on the entity-load path. Non-vehicle ids leave immediately.
		String id = input.getStringOr("id", "");
		if (!id.startsWith(Constants.MOD_ID + ":")) {
			return;
		}

		// Legacy vehicles are the only saves that still carry a "Type" tag.
		EntityType<?> legacy = LegacyVehicleIds.resolve(id, input.getStringOr("Type", ""));
		if (legacy != null) {
			cir.setReturnValue(Optional.of(legacy));
		}
	}
}
