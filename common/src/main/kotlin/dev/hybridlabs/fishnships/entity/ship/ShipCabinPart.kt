package dev.hybridlabs.fishnships.entity.ship

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.Pose
import net.minecraft.world.item.ItemStack

class ShipCabinPart(parentMob: ShipEntity, name: String?, width: Float, height: Float) :
    Entity(parentMob.type, parentMob.level()) {
    val parentMob: ShipEntity
    val name: String?
    private val size: EntityDimensions = EntityDimensions.scalable(width, height)

    init {
        this.refreshDimensions()
        this.parentMob = parentMob
        this.name = name
    }

    override fun defineSynchedData() {
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
    }

    override fun isPickable(): Boolean {
        return true
    }

    override fun getPickResult(): ItemStack? {
        return this.parentMob.pickResult
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        return if (isInvulnerableTo(source)) {
            false
        } else {
            parentMob.hurt(source, amount)
        }
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun isPushable(): Boolean {
        return true
    }

    override fun `is`(entity: Entity): Boolean {
        return this === entity || this.parentMob === entity
    }

    override fun getAddEntityPacket(): Packet<ClientGamePacketListener?> {
        throw UnsupportedOperationException()
    }

    override fun getDimensions(pose: Pose): EntityDimensions {
        return this.size
    }

    override fun shouldBeSaved(): Boolean {
        return false
    }
}