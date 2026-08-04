package dev.hybridlabs.fishnships.entity.vehicle

import com.google.common.collect.Lists
import com.google.common.collect.UnmodifiableIterator
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.platform.Services
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.util.ByIdMap
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.DismountHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationController.AnimationStateHandler
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation

open class SailboatEntity(
    entityType: EntityType<out SailboatEntity>, level: Level,
) :
    BaseBoatEntity(entityType, level),
    VariantHolder<SailboatEntity.Type>,
    GeoEntity {
    private var inputLeft = false
    private var inputRight = false
    private var inputJumping = false
    private var lastJumpInput = false

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        super.registerControllers(controllers)
        controllers.add(
            AnimationController(
                this, "Sailing",
                AnimationStateHandler { state: AnimationState<SailboatEntity> ->
                    if (this.isSailDown())
                        return@AnimationStateHandler state.setAndContinue(SAIL_DOWN_ANIMATION)
                    else return@AnimationStateHandler state.setAndContinue(SAIL_UP_ANIMATION)
                }
            )
        )
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(DATA_ID_TYPE, Type.OAK.ordinal)
        this.entityData.define(IS_SAIL_DOWN, false)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putString("Type", this.variant.getSerializedName())
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        if (tag.contains("Type", 8)) {
            this.variant = Type.byName(tag.getString("Type"))
        }
    }

    fun setSailDown(value: Boolean) {
        this.entityData.set(IS_SAIL_DOWN, value)
    }

    fun isSailDown(): Boolean {
        return entityData.get(IS_SAIL_DOWN)
    }

    override fun getPassengersRidingOffset(): Double {
        return -0.1
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (source.entity != null && this.hasPassenger(source.entity)) {
            return false
        }

        if (this.isInvulnerableTo(source)) {
            return false
        } else if (!this.level().isClientSide && !this.isRemoved) {
            this.setHurtTime(10)
            this.setDamage(this.getDamage() + amount * 10.0f)
            this.markHurt()
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.entity)
            val flag = source.entity is Player && (source.entity as Player).abilities.instabuild
            if (flag || this.getDamage() > 90.0f) {
                if (!flag && this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
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
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_SAILBOAT.get()
            2 -> item = FSItems.BIRCH_SAILBOAT.get()
            3 -> item = FSItems.JUNGLE_SAILBOAT.get()
            4 -> item = FSItems.ACACIA_SAILBOAT.get()
            5 -> item = FSItems.CHERRY_SAILBOAT.get()
            6 -> item = FSItems.DARK_OAK_SAILBOAT.get()
            7 -> item = FSItems.MANGROVE_SAILBOAT.get()
            8 -> item = FSItems.CRIMSON_SAILBOAT.get()
            9 -> item = FSItems.WARPED_SAILBOAT.get()
            else -> item = FSItems.OAK_SAILBOAT.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getSailboatItem())
    }

    protected open fun destroy(damageSource: DamageSource) {
        val stack = getSailboatItem()
        this.spawnAtLocation(stack)
    }

    override fun tick() {

        super.tick()

        if (isControlledByLocalInstance && level().isClientSide) {
            controlSailboat()
        }

        moveWithSailDown()
    }

    fun moveWithSailDown() {
        if (isSailDown()) {
            val sailSpeed = 0.03

            setDeltaMovement(
                deltaMovement.x + (-Mth.sin(yRot * Mth.DEG_TO_RAD) * sailSpeed),
                deltaMovement.y,
                deltaMovement.z + (Mth.cos(yRot * Mth.DEG_TO_RAD) * sailSpeed)
            )
        }
    }

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        if (this.hasPassenger(passenger)) {
            var f = 0.2f
            val f1 =
                ((if (this.isRemoved) 0.01 else this.passengersRidingOffset) + passenger.myRidingOffset).toFloat()
            if (this.passengers.size > 1) {
                val i = this.passengers.indexOf(passenger)
                f = if (i == 0) {
                    0.2f
                } else {
                    -0.6f
                }

                if (passenger is Animal) {
                    f += 0.2f
                }
            }

            val vec3 = (Vec3(
                f.toDouble(),
                0.0,
                0.0
            )).yRot(-this.yRot * (Math.PI.toFloat() / 180f) - (Math.PI.toFloat() / 2f))
            callback.accept(passenger, this.x + vec3.x, this.y + f1.toDouble(), this.z + vec3.z)
            passenger.yRot += this.deltaRotation
            passenger.yHeadRot += this.deltaRotation
            this.clampRotation(passenger)
            if (passenger is Animal && this.passengers.size == this.maxPassengers) {
                val j = if (passenger.id % 2 == 0) 90 else 270
                passenger.setYBodyRot(passenger.yBodyRot + j.toFloat())
                passenger.setYHeadRot(passenger.getYHeadRot() + j.toFloat())
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

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        return if (player.isSecondaryUseActive) {
            InteractionResult.PASS
        } else if (this.outOfControlTicks < 60.0f) {
            if (!this.level().isClientSide) {
                if (player.startRiding(this)) InteractionResult.CONSUME else InteractionResult.PASS
            } else {
                InteractionResult.SUCCESS
            }
        } else {
            InteractionResult.PASS
        }
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
                (Mth.sin(-this.yRot * (Math.PI.toFloat() / 180f)) * f).toDouble(),
                0.0,
                (Mth.cos(this.yRot * (Math.PI.toFloat() / 180f)) * f).toDouble()
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
        private val DATA_ID_TYPE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(SailboatEntity::class.java, EntityDataSerializers.INT)
        private val IS_SAIL_DOWN: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(SailboatEntity::class.java, EntityDataSerializers.BOOLEAN)

        val SAIL_UP_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("misc.sail_up")
        val SAIL_DOWN_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("misc.sail_down")
    }

    override fun setVariant(variant: Type) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal)
    }

    override fun getVariant(): Type {
        return Type.byId(this.entityData.get(DATA_ID_TYPE) as Int)
    }

    enum class Type(
        val planks: Block,
        private val key: String,
    ) : StringRepresentable {
        OAK(Blocks.OAK_PLANKS, "oak"),
        SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"),
        BIRCH(Blocks.BIRCH_PLANKS, "birch"),
        JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"),
        ACACIA(Blocks.ACACIA_PLANKS, "acacia"),
        CHERRY(Blocks.CHERRY_PLANKS, "cherry"),
        DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak"),
        MANGROVE(Blocks.MANGROVE_PLANKS, "mangrove"),
        CRIMSON(Blocks.CRIMSON_PLANKS, "crimson"),
        WARPED(Blocks.WARPED_PLANKS, "warped");

        override fun getSerializedName(): String = key

        override fun toString(): String = key

        companion object {
            val CODEC = StringRepresentable.fromEnum(::values)
            private val BY_ID = ByIdMap.continuous({ it.ordinal }, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

            fun byId(id: Int) = BY_ID.apply(id)
            fun byName(key: String) = CODEC.byName(key, OAK)
        }
    }
}