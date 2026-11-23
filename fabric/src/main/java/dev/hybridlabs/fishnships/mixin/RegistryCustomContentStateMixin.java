package dev.hybridlabs.fishnships.mixin;

import dev.hybridlabs.fishnships.entity.LegacyVehicleIds;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric API 0.166+ keeps a snapshot of the registry entries a world used and warns when an entry is
 * gone. The per-wood vehicle split removed the old shared entity ids, but the migration in
 * {@link dev.hybridlabs.fishnships.mixin.EntityTypeMixin} restores those entities when they load, so
 * they are not lost content.
 *
 * <p>This mixin drops the legacy vehicle ids from that warning. If anything else is missing, the
 * warning stays and shows only the entries that really are gone.
 *
 * <p>The target is a Fabric API implementation class, so the mixin is pseudo and the injection is
 * optional: another Fabric API version can change or remove it without breaking the game.
 */
@Pseudo
@Mixin(
        targets = "net.fabricmc.fabric.impl.registry.sync.validate.RegistryCustomContentState",
        remap = false
)
public class RegistryCustomContentStateMixin {
    private static final Identifier ENTITY_TYPE_REGISTRY = Identifier.parse("minecraft:entity_type");

    @Inject(method = "validate", at = @At("RETURN"), require = 0)
    private void fishnships$ignoreMigratedVehicleEntries(
            RegistryAccess registryAccess,
            CallbackInfoReturnable<Object> cir
    ) {
        Object missing = cir.getReturnValue();
        if (missing == null) {
            return;
        }

        try {
            Method entriesAccessor = missing.getClass().getMethod("entries");
            if (!(entriesAccessor.invoke(missing) instanceof Map<?, ?> missingByRegistry)) {
                return;
            }

            if (!(missingByRegistry.get(ENTITY_TYPE_REGISTRY) instanceof List<?> missingEntityTypes)) {
                return;
            }

            Iterator<?> iterator = missingEntityTypes.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof Identifier id && LegacyVehicleIds.isLegacyVehicleEntity(id)) {
                    iterator.remove();
                }
            }

            if (missingEntityTypes.isEmpty()) {
                missingByRegistry.remove(ENTITY_TYPE_REGISTRY);
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // Fabric API changed its internals; leave the warning untouched.
        }
    }
}
