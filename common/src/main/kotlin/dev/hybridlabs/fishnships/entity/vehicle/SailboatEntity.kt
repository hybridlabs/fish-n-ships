package dev.hybridlabs.fishnships.entity.vehicle

import com.google.common.collect.Lists
import com.google.common.collect.UnmodifiableIterator
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.platform.Services
import dev.hybridlabs.hapi.entity.base.vehicle.BaseBoatEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.tags.EntityTypeTags
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.DismountHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.gamerules.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.manager.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationController.AnimationStateHandler
import software.bernie.geckolib.animation.state.AnimationTest
import software.bernie.geckolib.animation.RawAnimation

open class SailboatEntity(
    entityType: EntityType<out SailboatEntity>, level: Level,
    private val variant: Type,
) :
    BaseBoatEntity(entityType, level),
    GeoEntity {
    private var inputLeft = false
    private var inputRight = false
    private var inputJumping = false
    private var lastJumpInput = false

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        super.registerControllers(controllers)
        controllers.add(
            AnimationController(
                "Sailing", 4,
                AnimationStateHandler { state: AnimationTest<SailboatEntity> ->
                    // GeckoLib 5 applies a "reset" transition when an animation first starts from
                    // rest, lerping a bone back to the model's base pose before the animation's own
                    // value takes over. For the sail that reads as the banner extending down to full
                    // scale and then vanishing back to its furled sliver on spawn. Drop the
                    // transition for that first animation so the sail appears at its correct pose
                    // immediately, while keeping the 4-tick transition for sail up/down toggles.
                    state.controller.setTransitionTicks(
                        if (state.controller.getCurrentRawAnimation() == null) 0 else 4
                    )

                    if (this.isSailDown())
                        return@AnimationStateHandler state.setAndContinue(SAIL_DOWN_ANIMATION)
                    else return@AnimationStateHandler state.setAndContinue(SAIL_UP_ANIMATION)
                }
            )
        )
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(IS_SAIL_DOWN, false)
    }

    fun setSailDown(value: Boolean) {
        this.entityData.set(IS_SAIL_DOWN, value)
    }

    fun isSailDown(): Boolean {
        return entityData.get(IS_SAIL_DOWN)
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, amount: Float): Boolean {
        if (source.entity != null && this.hasPassenger(source.entity!!)) {
            return false
        }

        if (this.isInvulnerableToBase(source)) {
            return false
        } else if (!this.level().isClientSide && !this.isRemoved) {
            this.setHurtTime(10)
            this.setDamage(this.getDamage() + amount * 10.0f)
            this.markHurt()
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.entity!!)
            val flag = source.entity is Player && (source.entity as Player).abilities.instabuild
            if (flag || this.getDamage() > 40.0f) {
                if (!flag && (level() as ServerLevel).getGameRules().get(GameRules.ENTITY_DROPS)) {
                    this.destroy(source)
                }

                this.discard()
            }

            return true
        } else {
            return true
        }
    }

    open fun getSailboatItem(): Item {
        val item: Item
        when (this.getVariant().ordinal) {
            1 -> item = FSItems.SPRUCE_SAILBOAT.get()
            2 -> item = FSItems.BIRCH_SAILBOAT.get()
            3 -> item = FSItems.JUNGLE_SAILBOAT.get()
            4 -> item = FSItems.ACACIA_SAILBOAT.get()
            5 -> item = FSItems.CHERRY_SAILBOAT.get()
            6 -> item = FSItems.DARK_OAK_SAILBOAT.get()
            7 -> item = FSItems.MANGROVE_SAILBOAT.get()
            8 -> item = FSItems.CRIMSON_SAILBOAT.get()
            9 -> item = FSItems.WARPED_SAILBOAT.get()
            10 -> item = FSItems.DRIFTWOOD_SAILBOAT.get()
            else -> item = FSItems.OAK_SAILBOAT.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getSailboatItem())
    }

    protected open fun destroy(damageSource: DamageSource) {
        val stack = getSailboatItem()
        this.spawnAtLocation(level() as ServerLevel, stack)
    }

    override fun controlBoat() {
        if (this.level().isClientSide) {
            this.controlSailboat()
        }

        this.moveWithSailDown()
    }

    fun moveWithSailDown() {
        if (isSailDown()) {
            val sailSpeed = 0.03

            setDeltaMovement(
                deltaMovement.x + (-Mth.sin((yRot * Mth.DEG_TO_RAD).toDouble()) * sailSpeed),
                deltaMovement.y,
                deltaMovement.z + (Mth.cos((yRot * Mth.DEG_TO_RAD).toDouble()) * sailSpeed)
            )
        }
    }

    override fun getPassengerAttachmentPoint(
        passenger: Entity,
        dimensions: EntityDimensions,
        partialTick: Float
    ): Vec3 {
        var f = 0.2

        if (passengers.size > 1) {
            f = if (passengers.indexOf(passenger) == 0) {
                0.2
            } else {
                -0.6
            }

            if (passenger is Animal) {
                f += 0.2
            }
        }

        return Vec3(
            f,
            dimensions.height() / 3.0,
            0.0
        ).yRot(-yRot * (Math.PI.toFloat() / 180f) - (Math.PI.toFloat() / 2f))
    }

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        super.positionRider(passenger, callback)

        if (!passenger.type.`is`(EntityTypeTags.CAN_TURN_IN_BOATS)) {
            passenger.yRot += deltaRotation
            passenger.yHeadRot += deltaRotation
            clampRotation(passenger)

            if (passenger is Animal && passengers.size == maxPassengers) {
                val rotation = if (passenger.id % 2 == 0) 90f else 270f
                passenger.yBodyRot += rotation
                passenger.yHeadRot += rotation
            }
        }
    }

    protected fun clampRotation(entityToUpdate: Entity) {
        entityToUpdate.setYBodyRot(this.yRot)
        val f = Mth.wrapDegrees(entityToUpdate.yRot - this.yRot)
        val f1 = Mth.clamp(f, -105.0f, 105.0f)
        entityToUpdate.yRotO += f1 - f
        entityToUpdate.yRot = entityToUpdate.yRot + f1 - f
        entityToUpdate.yHeadRot = entityToUpdate.yRot
    }

    override fun getDismountLocationForPassenger(livingEntity: LivingEntity): Vec3 {
        val vec3 = getCollisionHorizontalEscapeVector(
            (this.bbWidth * Mth.SQRT_OF_TWO).toDouble(),
            livingEntity.bbWidth.toDouble(),
            livingEntity.yRot
        )
        val d0 = this.x + vec3.x
        val d1 = this.z + vec3.z
        val blockpos = BlockPos.containing(d0, this.boundingBox.maxY, d1)
        val blockpos1 = blockpos.below()
        if (!this.level().isWaterAt(blockpos1)) {
            val list: MutableList<Vec3> = Lists.newArrayList<Vec3>()
            val d2 = this.level().getBlockFloorHeight(blockpos)
            if (DismountHelper.isBlockFloorValid(d2)) {
                list.add(Vec3(d0, blockpos.y.toDouble() + d2, d1))
            }

            val d3 = this.level().getBlockFloorHeight(blockpos1)
            if (DismountHelper.isBlockFloorValid(d3)) {
                list.add(Vec3(d0, blockpos1.y.toDouble() + d3, d1))
            }

            val var14: UnmodifiableIterator<*> = livingEntity.dismountPoses.iterator()

            while (var14.hasNext()) {
                val pose = var14.next() as Pose

                for (vec31 in list) {
                    if (DismountHelper.canDismountTo(this.level(), vec31, livingEntity, pose)) {
                        livingEntity.pose = pose
                        return vec31
                    }
                }
            }
        }

        return super.getDismountLocationForPassenger(livingEntity)
    }

    override val maxPassengers: Int
        get() = 2

    override fun getControllingPassenger(): LivingEntity? {
        val passenger = this.firstPassenger
        val passengerLivingEntity: LivingEntity? = passenger as? LivingEntity

        return passengerLivingEntity
    }

    private fun controlSailboat() {
        if (this.isVehicle) {
            var f = 0.0f
            if (this.inputLeft) {
                --this.deltaRotation
            }

            if (this.inputRight) {
                ++this.deltaRotation
            }

            this.yRot += this.deltaRotation

            if (this.inputRight != this.inputLeft) {
                f += 0.005f
            }

            if (inputJumping && !lastJumpInput) {
                Services.PLATFORM.changeSailState(this)
            }

            lastJumpInput = inputJumping

            this.deltaMovement = this.deltaMovement.add(
                (Mth.sin((-this.yRot * (Math.PI.toFloat() / 180f)).toDouble()) * f).toDouble(),
                0.0,
                (Mth.cos((this.yRot * (Math.PI.toFloat() / 180f)).toDouble()) * f).toDouble()
            )
        }
    }

    fun setInput(
        inputLeft: Boolean,
        inputRight: Boolean,
        inputJumping: Boolean,
    ) {
        this.inputLeft = inputLeft
        this.inputRight = inputRight
        this.inputJumping = inputJumping
    }

    companion object {
        private val IS_SAIL_DOWN: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(SailboatEntity::class.java, EntityDataSerializers.BOOLEAN)

        val SAIL_UP_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("misc.sail_up")
        val SAIL_DOWN_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("misc.sail_down")
    }

    fun getVariant(): Type {
        return variant
    }

    enum class Type(
        private val key: String,
    ) : StringRepresentable {
        OAK("oak"),
        SPRUCE("spruce"),
        BIRCH("birch"),
        JUNGLE("jungle"),
        ACACIA("acacia"),
        CHERRY("cherry"),
        DARK_OAK("dark_oak"),
        MANGROVE("mangrove"),
        CRIMSON("crimson"),
        WARPED("warped"),
        DRIFTWOOD("driftwood");

        override fun getSerializedName(): String = key

        override fun toString(): String = key
    }
}