package dev.hybridlabs.fishnships.entity.vehicle

import com.google.common.collect.Lists
import com.google.common.collect.UnmodifiableIterator
import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.hapi.entity.base.vehicle.BaseBoatEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.EntityTypeTags
import net.minecraft.util.ByIdMap
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.WaterAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.DismountHelper
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.util.GeckoLibUtil

open class DinghyEntity(
    entityType: EntityType<out DinghyEntity?>, level: Level,
) :
    BaseBoatEntity(entityType, level),
    VariantHolder<DinghyEntity.Type>,
    GeoEntity {
    private val animCache = GeckoLibUtil.createInstanceCache(this)
    private val paddlePositions: FloatArray = FloatArray(2)
    private var inputLeft = false
    private var inputRight = false
    private var inputUp = false
    private var inputDown = false

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
        return animCache
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_ID_TYPE, Type.OAK.ordinal)
        builder.define(DATA_ID_ALTERNATE, false)
        builder.define(DATA_ID_PADDLE_LEFT, false)
        builder.define(DATA_ID_PADDLE_RIGHT, false)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putString("Type", this.variant.getSerializedName())
        tag.putBoolean("Alternate", this.alternate)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        if (tag.contains("Type", 8)) {
            this.variant = Type.byName(tag.getString("Type"))
        }

        if (tag.contains("Alternate", 1)) {
            this.alternate = tag.getBoolean("Alternate")
        }
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
            if (flag || this.getDamage() > 50.0f) {
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

    open fun getDinghyItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_DINGHY.get()
            2 -> item = FSItems.BIRCH_DINGHY.get()
            3 -> item = FSItems.JUNGLE_DINGHY.get()
            4 -> item = FSItems.ACACIA_DINGHY.get()
            5 -> item = FSItems.CHERRY_DINGHY.get()
            6 -> item = FSItems.DARK_OAK_DINGHY.get()
            7 -> item = FSItems.MANGROVE_DINGHY.get()
            8 -> item = FSItems.CRIMSON_DINGHY.get()
            9 -> item = FSItems.WARPED_DINGHY.get()
            else -> item = FSItems.OAK_DINGHY.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getDinghyItem())
    }

    protected open fun destroy(damageSource: DamageSource) {
        val stack = getDinghyItem()
        this.spawnAtLocation(stack)
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)

        if (stack.item is AxeItem) {
            if (!level().isClientSide) {
                this.alternate = !this.alternate

                if (!player.abilities.instabuild) {
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
                }

                playSound(SoundEvents.AXE_STRIP)
            }

            return InteractionResult.sidedSuccess(level().isClientSide)
        }

        return super.interact(player, hand)
    }

    override fun tick() {
        super.tick()

        if (this.isControlledByLocalInstance) {
            if (this.firstPassenger !is Player) {
                this.setPaddleState(left = false, right = false)
            }

            floatWaterVehicle()
            if (this.level().isClientSide) {
                this.controlDinghy()
                this.level()
                    .sendPacketToServer(ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)))
            }

            this.move(MoverType.SELF, this.deltaMovement)
        } else {
            this.deltaMovement = Vec3.ZERO
        }

        for (i in 0..1) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent && (this.paddlePositions[i] % (Math.PI.toFloat() * 2f)).toDouble() <= (Math.PI.toFloat() / 4f).toDouble() && ((this.paddlePositions[i] + (Math.PI.toFloat() / 8f)) % (Math.PI.toFloat() * 2f)).toDouble() >= (Math.PI.toFloat() / 4f).toDouble()) {
                    val soundevent: SoundEvent? = this.paddleSound
                    if (soundevent != null) {
                        val vec3 = this.getViewVector(1.0f)
                        val d0 = if (i == 1) -vec3.z else vec3.z
                        val d1 = if (i == 1) vec3.x else -vec3.x
                        this.level().playSound(
                            null as Player?,
                            this.x + d0,
                            this.y,
                            this.z + d1,
                            soundevent,
                            this.soundSource,
                            1.0f,
                            0.8f + 0.4f * this.random.nextFloat()
                        )
                    }
                }

                val var10000 = this.paddlePositions
                var10000[i] += (Math.PI.toFloat() / 8f)
            } else {
                this.paddlePositions[i] = 0.0f
            }
        }

        this.checkInsideBlocks()
        val list = this.level()
            .getEntities(this, this.boundingBox.inflate(0.2, -0.01, 0.2), EntitySelector.pushableBy(this))
        if (!list.isEmpty()) {
            val flag = !this.level().isClientSide && this.getControllingPassenger() !is Player

            for (j in list.indices) {
                val entity = list[j] as Entity
                if (!entity.hasPassenger(this)) {
                    if (flag && this.passengers.size < this.maxPassengers && !entity.isPassenger && this.hasEnoughSpaceFor(
                            entity
                        ) && entity is LivingEntity && (entity !is WaterAnimal) && (entity !is Player)
                    ) {
                        entity.startRiding(this)
                    } else {
                        this.push(entity)
                    }
                }
            }
        }
    }

    fun setPaddleState(left: Boolean, right: Boolean) {
        this.entityData.set(DATA_ID_PADDLE_LEFT, left)
        this.entityData.set(DATA_ID_PADDLE_RIGHT, right)
    }

    fun getRowingTime(side: Int, limbSwing: Float): Float {
        return if (this.getPaddleState(side)) Mth.clampedLerp(
            this.paddlePositions[side] - (Math.PI.toFloat() / 8f),
            this.paddlePositions[side],
            limbSwing
        ) else 0.0f
    }

    override fun getPassengerAttachmentPoint(
        passenger: Entity,
        dimensions: EntityDimensions,
        partialTick: Float
    ): Vec3 {
        val xOffset = when (passengers.indexOf(passenger)) {
            0 -> -0.25
            1 -> -0.9
            2 -> 0.65
            else -> 0.0
        }

        val yOffset = dimensions.height() / 3.0

        return Vec3(0.0, yOffset, xOffset)
            .yRot(-yRot * (Math.PI.toFloat() / 180f))
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

    protected fun clampRotation(entityToUpdate: Entity) {
        entityToUpdate.setYBodyRot(this.yRot)
        val f = Mth.wrapDegrees(entityToUpdate.yRot - this.yRot)
        val f1 = Mth.clamp(f, -105.0f, 105.0f)
        entityToUpdate.yRotO += f1 - f
        entityToUpdate.yRot = entityToUpdate.yRot + f1 - f
        entityToUpdate.yHeadRot = entityToUpdate.yRot
    }

    override fun onPassengerTurned(entityToUpdate: Entity) {
        this.clampRotation(entityToUpdate)
    }

    fun getPaddleState(side: Int): Boolean {
        return this.entityData.get(if (side == 0) DATA_ID_PADDLE_LEFT else DATA_ID_PADDLE_RIGHT) as Boolean && this.getControllingPassenger() != null
    }

    override val maxPassengers: Int
        get() = 5

    var alternate: Boolean
        get() = entityData.get(DATA_ID_ALTERNATE)
        set(value) {
            entityData.set(DATA_ID_ALTERNATE, value)
        }

    override fun getControllingPassenger(): LivingEntity? {
        val entity = this.firstPassenger
        val livingentity1: LivingEntity? = entity as? LivingEntity

        return livingentity1
    }

    private fun controlDinghy() {
        if (this.isVehicle) {
            var f = 0.0f
            if (this.inputLeft) {
                --this.deltaRotation
            }

            if (this.inputRight) {
                ++this.deltaRotation
            }

            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                f += 0.005f
            }

            this.yRot += this.deltaRotation
            if (this.inputUp) {
                f += 0.038f
            }

            if (this.inputDown) {
                f -= 0.005f
            }

            this.deltaMovement = this.deltaMovement.add(
                (Mth.sin(-this.yRot * (Math.PI.toFloat() / 180f)) * f).toDouble(),
                0.0,
                (Mth.cos(this.yRot * (Math.PI.toFloat() / 180f)) * f).toDouble()
            )
            this.setPaddleState(
                this.inputRight && !this.inputLeft || this.inputUp,
                this.inputLeft && !this.inputRight || this.inputUp
            )
        }
    }

    fun setInput(
        inputLeft: Boolean,
        inputRight: Boolean,
        inputUp: Boolean,
        inputDown: Boolean,
    ) {
        this.inputLeft = inputLeft
        this.inputRight = inputRight
        this.inputUp = inputUp
        this.inputDown = inputDown
    }

    companion object {
        private val DATA_ID_TYPE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(DinghyEntity::class.java, EntityDataSerializers.INT)
        private val DATA_ID_ALTERNATE: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(DinghyEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val DATA_ID_PADDLE_LEFT: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(DinghyEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val DATA_ID_PADDLE_RIGHT: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(DinghyEntity::class.java, EntityDataSerializers.BOOLEAN)
    }

    override fun setVariant(variant: Type) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal)
    }

    override fun getVariant(): Type {
        return Type.byId(this.entityData.get(DATA_ID_TYPE) as Int)
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

        companion object {
            val CODEC = StringRepresentable.fromEnum(::values)
            private val BY_ID = ByIdMap.continuous({ it.ordinal }, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

            fun byId(id: Int) = BY_ID.apply(id)
            fun byName(key: String) = CODEC.byName(key, OAK)
        }
    }
}