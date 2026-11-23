package dev.hybridlabs.fishnships.entity

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.Constants
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType

/**
 * Translates the shared vehicle entity ids used before per-wood entity types into the current
 * per-wood ids. The old entities stored their wood type in a "Type" tag, so the replacement id can
 * be derived without touching the rest of the entity data.
 *
 * The caller checks the namespace first, so entity loads outside this mod never reach the registry
 * lookups below.
 */
object LegacyVehicleIds {
    private data class Family(val suffix: String, val defaultVariant: String)

    private val FAMILIES = mapOf(
        "boat" to Family("boat", "crimson"),
        "boat_with_chest" to Family("boat_with_chest", "crimson"),
        "sailboat" to Family("sailboat", "oak"),
        "sailboat_with_chest" to Family("sailboat_with_chest", "oak"),
        "canoe" to Family("canoe", "oak"),
        "canoe_with_chest" to Family("canoe_with_chest", "oak"),
        "canoe_with_double_chest" to Family("canoe_with_double_chest", "oak"),
        "dinghy" to Family("dinghy", "oak"),
        "dinghy_with_chest" to Family("dinghy_with_chest", "oak"),
        "raft" to Family("raft", "oak"),
        "supply_raft" to Family("supply_raft", "oak"),
    )

    @JvmStatic
    fun resolve(id: String, variant: String): EntityType<*>? {
        val prefix = "${Constants.MOD_ID}:"
        if (!id.startsWith(prefix)) {
            return null
        }

        val path = id.removePrefix(prefix)
        val family = FAMILIES[path] ?: return null

        // If the shared id is still registered (for example by an alias), let it win.
        if (BuiltInRegistries.ENTITY_TYPE.getOptional(CommonClass.locate(path)).isPresent()) {
            return null
        }

        val wood = variant.ifEmpty { family.defaultVariant }

        return BuiltInRegistries.ENTITY_TYPE
            .getOptional(CommonClass.locate("${wood}_${family.suffix}"))
            .orElse(null)
            ?: BuiltInRegistries.ENTITY_TYPE
                .getOptional(CommonClass.locate("${family.defaultVariant}_${family.suffix}"))
                .orElse(null)
    }
}
