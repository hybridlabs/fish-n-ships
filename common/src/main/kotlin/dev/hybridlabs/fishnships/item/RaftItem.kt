package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.entity.FSEntityTypes
import dev.hybridlabs.fishnships.entity.vehicle.RaftEntity
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.HitResult

class RaftItem(
    private val variant: RaftEntity.Type,
    properties: Properties
) : Item(properties) {

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val itemstack = player.getItemInHand(hand)
        val hitresult: HitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY)
        if (hitresult.type == HitResult.Type.MISS) {
            return InteractionResultHolder.pass<ItemStack?>(itemstack)
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
                        return InteractionResultHolder.pass<ItemStack?>(itemstack)
                    }
                }
            }

            if (hitresult.type == HitResult.Type.BLOCK) {
                val raft = getRaft(level, hitresult)
                raft.variant = variant
                raft.yRot = player.yRot

                if (!level.noCollision(raft, raft.boundingBox)) {
                    return InteractionResultHolder.fail(itemstack)
                } else {
                    if (!level.isClientSide) {
                        level.addFreshEntity(raft)
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.location)
                        if (!player.abilities.instabuild) {
                            itemstack.shrink(1)
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this))
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide())
                }
            } else {
                return InteractionResultHolder.pass<ItemStack?>(itemstack)
            }
        }
    }

    private fun getRaft(level: Level, hitResult: HitResult): RaftEntity {
        val raft = FSEntityTypes.RAFT.get().create(level)
            ?: throw IllegalStateException("Failed to create raft")

        raft.setPos(
            hitResult.location.x,
            hitResult.location.y,
            hitResult.location.z
        )

        return raft
    }

    companion object {
        val ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable)
    }
}
