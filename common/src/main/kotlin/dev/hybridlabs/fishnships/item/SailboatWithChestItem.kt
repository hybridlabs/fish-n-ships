package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.entity.vehicle.SailboatEntity
import dev.hybridlabs.fishnships.entity.vehicle.SailboatWithChestEntity
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.HitResult

class SailboatWithChestItem(
    private val variant: SailboatEntity.Type,
    properties: Properties
) : Item(properties) {

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val itemstack = player.getItemInHand(hand)
        val hitresult: HitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY)
        if (hitresult.type == HitResult.Type.MISS) {
            return InteractionResult.PASS
        } else {
            val vec3 = player.getViewVector(1.0f)
            val list = level.getEntities(
                player,
                player.boundingBox.expandTowards(vec3.scale(5.0)).inflate(1.0),
                ENTITY_PREDICATE
            )
            if (!list.isEmpty()) {
                val vec31 = player.eyePosition

                for (entity in list) {
                    val aabb = entity.boundingBox.inflate(entity.pickRadius.toDouble())
                    if (aabb.contains(vec31)) {
                        return InteractionResult.PASS
                    }
                }
            }

            if (hitresult.type == HitResult.Type.BLOCK) {
                val sailboat = getSailboat(level, hitresult)
                sailboat.setVariant(variant)
                sailboat.yRot = player.yRot

                if (!level.noCollision(sailboat, sailboat.boundingBox)) {
                    return InteractionResult.FAIL
                } else {
                    if (!level.isClientSide) {
                        level.addFreshEntity(sailboat)
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.location)
                        if (!player.abilities.instabuild) {
                            itemstack.shrink(1)
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this))
                    return if (level.isClientSide) InteractionResult.SUCCESS else InteractionResult.SUCCESS_SERVER
                }
            } else {
                return InteractionResult.PASS
            }
        }
    }

    private fun getSailboat(level: Level, hitResult: HitResult): SailboatWithChestEntity {
        val sailboat = FSEntityTypes.SAILBOAT_WITH_CHEST.get().create(level, EntitySpawnReason.MOB_SUMMONED)
            ?: throw IllegalStateException("Failed to create sailboat")

        sailboat.setPos(
            hitResult.location.x,
            hitResult.location.y,
            hitResult.location.z
        )

        return sailboat
    }

    companion object {
        val ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable)
    }
}
