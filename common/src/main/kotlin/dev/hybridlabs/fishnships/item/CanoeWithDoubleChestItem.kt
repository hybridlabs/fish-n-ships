package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.entity.vehicle.CanoeEntity
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

class CanoeWithDoubleChestItem(
    private val variant: CanoeEntity.Type,
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
                val canoe = getCanoe(level, hitresult)
                canoe.yRot = player.yRot

                if (!level.noCollision(canoe, canoe.boundingBox)) {
                    return InteractionResult.FAIL
                } else {
                    if (!level.isClientSide) {
                        level.addFreshEntity(canoe)
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

    private fun getCanoe(level: Level, hitResult: HitResult): CanoeEntity {
        val canoe = FSEntityTypes.CANOES_WITH_DOUBLE_CHEST.getValue(variant).get().create(level, EntitySpawnReason.MOB_SUMMONED)
            ?: throw IllegalStateException("Failed to create canoe")

        canoe.setPos(
            hitResult.location.x,
            hitResult.location.y,
            hitResult.location.z
        )

        return canoe
    }

    companion object {
        val ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable)
    }
}
