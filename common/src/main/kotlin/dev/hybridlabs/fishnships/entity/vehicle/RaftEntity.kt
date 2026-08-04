package dev.hybridlabs.fishnships.entity.vehicle

import com.google.common.collect.Lists
import com.google.common.collect.UnmodifiableIterator
import dev.hybridlabs.fishnships.item.FSItems
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.util.ByIdMap
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.VariantHolder
import net.minecraft.world.entity.decoration.HangingEntity
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.DismountHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity

open class RaftEntity(
    type: EntityType<out RaftEntity>,
    world: Level,
) :
    BaseBoatEntity(type, world),
    VariantHolder<RaftEntity.Type>,
    GeoEntity {
    private var leashHolder: Entity? = null
    private var delayedLeashHolderId = 0
    private var leashInfoTag: CompoundTag? = null

    init {
        noCulling = true
    }
    
    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        if (!isAlive) {
            return InteractionResult.PASS
        }

        if (this.getLeashHolder() === player) {
            this.dropLeash(true, !player.abilities.instabuild)
            this.gameEvent(GameEvent.ENTITY_INTERACT, player)
            return InteractionResult.sidedSuccess(level().isClientSide)
        }

        val result = checkAndHandleImportantInteractions(player, hand)
        if (result.consumesAction()) {
            this.gameEvent(GameEvent.ENTITY_INTERACT, player)
            return result
        }

        return if (player.isSecondaryUseActive) {
            InteractionResult.PASS
        } else if (outOfControlTicks < 60.0f) {
            if (!level().isClientSide) {
                if (player.startRiding(this)) {
                    InteractionResult.CONSUME
                } else {
                    InteractionResult.PASS
                }
            } else {
                InteractionResult.SUCCESS
            }
        } else {
            InteractionResult.PASS
        }
    }

    private fun checkAndHandleImportantInteractions(player: Player, hand: InteractionHand): InteractionResult {
        val itemstack = player.getItemInHand(hand)
        if (itemstack.`is`(Items.LEAD) && this.canBeLeashed(player)) {
            this.setLeashedTo(player, true)
            itemstack.shrink(1)
            return InteractionResult.sidedSuccess(this.level().isClientSide)
        }
        return InteractionResult.PASS
    }

    private fun restoreLeashFromSave() {
        if (this.leashInfoTag != null && this.level() is ServerLevel) {
            if (this.leashInfoTag!!.hasUUID("UUID")) {
                val uuid = this.leashInfoTag!!.getUUID("UUID")
                val entity = (this.level() as ServerLevel).getEntity(uuid)
                if (entity != null) {
                    this.setLeashedTo(entity, true)
                    return
                }
            } else if (this.leashInfoTag!!.contains("X", 99) && this.leashInfoTag!!.contains(
                    "Y",
                    99
                ) && this.leashInfoTag!!.contains("Z", 99)
            ) {
                val blockpos = NbtUtils.readBlockPos(this.leashInfoTag)
                this.setLeashedTo(LeashFenceKnotEntity.getOrCreateKnot(this.level(), blockpos), true)
                return
            }

            if (this.tickCount > 100) {
                this.spawnAtLocation(Items.LEAD)
                this.leashInfoTag = null
            }
        }
    }

    protected fun tickLeash() {
        if (leashInfoTag != null) {
            restoreLeashFromSave()
        }

        if (leashHolder != null && (!isAlive || !leashHolder!!.isAlive)) {
            dropLeash(broadcastPacket = true, dropLeash = true)
        }

        val holder = getLeashHolder()
        if (holder != null && holder.level() === level()) {
            val distance = distanceTo(holder)

            if (distance > 10.0f) {
                dropLeash(true, dropLeash = true)
            } else if (distance > 6.0f && shouldStayCloseToLeashHolder()) {
                val direction = Vec3(
                    holder.x - x,
                    holder.y - y,
                    holder.z - z
                ).normalize()

                val followSpeed = followLeashSpeed()

                val targetVelocity = direction.scale(followSpeed)

                deltaMovement = deltaMovement.lerp(
                    Vec3(targetVelocity.x, deltaMovement.y, targetVelocity.z),
                    0.15
                )
            }
        }
    }

    protected open fun shouldStayCloseToLeashHolder(): Boolean {
        return true
    }

    protected open fun followLeashSpeed(): Double {
        return 1.0
    }

    fun dropLeash(broadcastPacket: Boolean, dropLeash: Boolean) {
        if (this.leashHolder != null) {
            this.leashHolder = null
            this.leashInfoTag = null
            if (!this.level().isClientSide && dropLeash) {
                this.spawnAtLocation(Items.LEAD)
            }

            if (!this.level().isClientSide && broadcastPacket && this.level() is ServerLevel) {
                (this.level() as ServerLevel).chunkSource
                    .broadcast(this, ClientboundSetEntityLinkPacket(this, null as Entity?))
            }
        }
    }

    open fun canBeLeashed(player: Player?): Boolean {
        return !this.isLeashed() && this !is Enemy
    }

    fun isLeashed(): Boolean {
        return this.leashHolder != null
    }

    fun getLeashHolder(): Entity? {
        if (this.leashHolder == null && this.delayedLeashHolderId != 0 && this.level().isClientSide) {
            this.leashHolder = this.level().getEntity(this.delayedLeashHolderId)
        }

        return this.leashHolder
    }

    /**
     * Sets the entity to be leashed to.
     */
    fun setLeashedTo(leashHolder: Entity?, broadcastPacket: Boolean) {
        this.leashHolder = leashHolder
        this.leashInfoTag = null
        if (!this.level().isClientSide && broadcastPacket && this.level() is ServerLevel) {
            (this.level() as ServerLevel).chunkSource
                .broadcast(this, ClientboundSetEntityLinkPacket(this, this.leashHolder))
        }

        if (this.isPassenger) {
            this.stopRiding()
        }
    }

    fun setDelayedLeashHolderId(leashHolderID: Int) {
        this.delayedLeashHolderId = leashHolderID
        this.dropLeash(broadcastPacket = false, dropLeash = false)
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(DATA_ID_TYPE, Type.OAK.ordinal)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        tag.putFloat("Damage", getDamage())

        if (this.leashHolder != null) {
            val compoundtag2 = CompoundTag()
            if (this.leashHolder is LivingEntity) {
                val uuid = this.leashHolder!!.getUUID()
                compoundtag2.putUUID("UUID", uuid)
            } else if (this.leashHolder is HangingEntity) {
                val blockpos = (this.leashHolder as HangingEntity).getPos()
                compoundtag2.putInt("X", blockpos.x)
                compoundtag2.putInt("Y", blockpos.y)
                compoundtag2.putInt("Z", blockpos.z)
            }

            tag.put("Leash", compoundtag2)
        } else if (this.leashInfoTag != null) {
            tag.put("Leash", this.leashInfoTag!!.copy())
        }

        tag.putString("Type", this.variant.getSerializedName())
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        setDamage(tag.getFloat("Damage"))

        if (tag.contains("Leash", 10)) {
            this.leashInfoTag = tag.getCompound("Leash")
        }

        if (tag.contains("Type", 8)) {
            this.variant = Type.byName(tag.getString("Type"))
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

    override fun push(entity: Entity) {
        if (entity is RaftEntity) {
            if (entity.boundingBox.minY < this.boundingBox.maxY) {
                super.push(entity)
            }
        } else if (entity.boundingBox.minY <= this.boundingBox.minY) {
            super.push(entity)
        }
    }

    override fun getControllingPassenger(): LivingEntity? = null

    override fun tick() {
        super.tick()
        this.tickLeash()

        this.move(MoverType.SELF, this.deltaMovement)

        val velocity = this.deltaMovement

        if (velocity.horizontalDistanceSqr() > 1.0E-4) {
            val targetYaw = (Mth.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)).toFloat() - 90.0f

            this.yRot = Mth.approachDegrees(this.yRot, targetYaw, 5.0f)
            this.yRot = this.yRot
            this.setYBodyRot(yRot)
            this.yHeadRot = this.yRot
        }
    }

    override fun positionRider(passenger: Entity, callback: MoveFunction) {
        if (!hasPassenger(passenger)) {
            return
        }

        val yOffset =
            ((if (isRemoved) 0.01 else passengersRidingOffset) + passenger.myRidingOffset).toFloat()

        val (xOffset, zOffset) = when (passengers.size) {
            1 -> when (passengers.indexOf(passenger)) {
                0 -> 0.0 to 0.0
                else -> 0.0 to 0.0
            }

            2 -> when (passengers.indexOf(passenger)) {
                0 -> 0.0 to 0.5
                1 -> 0.0 to -0.5
                else -> 0.0 to 0.0
            }

            3 -> when (passengers.indexOf(passenger)) {
                0 -> 0.0 to 0.5
                1 -> 0.5 to -0.5
                2 -> -0.5 to -0.5
                else -> 0.0 to 0.0
            }

            else -> when (passengers.indexOf(passenger)) {
                0 -> 0.5 to 0.5
                1 -> -0.5 to 0.5
                2 -> 0.5 to -0.5
                3 -> -0.5 to -0.5
                else -> 0.0 to 0.0
            }
        }

        val offset = Vec3(xOffset, 0.0, zOffset)
            .yRot(-yRot * (Math.PI.toFloat() / 180f) - (Math.PI.toFloat() / 2f))

        callback.accept(
            passenger,
            x + offset.x,
            y + yOffset,
            z + offset.z
        )

        passenger.yRot += deltaRotation
        passenger.yHeadRot += deltaRotation
        clampRotation(passenger)
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

    override fun checkFallDamage(y: Double, onGround: Boolean, state: BlockState, pos: BlockPos) {
        this.lastYd = this.deltaMovement.y
        if (!this.isPassenger) {
            if (onGround) {
                if (this.fallDistance > 3.0f) {
                    if (this.status != Status.ON_LAND) {
                        this.resetFallDistance()
                        return
                    }

                    this.causeFallDamage(this.fallDistance, 1.0f, this.damageSources().fall())
                    if (!this.level().isClientSide && !this.isRemoved) {
                        this.kill()
                        if (this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (j in 0..1) {
                                this.spawnAtLocation(Items.STICK)
                            }
                        }
                    }
                }

                this.resetFallDistance()
            } else if (!this.level().getFluidState(this.blockPosition().below()).`is`(FluidTags.WATER) && y < 0.0) {
                this.fallDistance -= y.toFloat()
            }
        }
    }

    override val maxPassengers: Int
        get() = 4

    open fun getRaftItem(): Item {
        val item: Item
        when (this.variant.ordinal) {
            1 -> item = FSItems.SPRUCE_RAFT.get()
            2 -> item = FSItems.BIRCH_RAFT.get()
            3 -> item = FSItems.JUNGLE_RAFT.get()
            4 -> item = FSItems.ACACIA_RAFT.get()
            5 -> item = FSItems.CHERRY_RAFT.get()
            6 -> item = FSItems.DARK_OAK_RAFT.get()
            7 -> item = FSItems.MANGROVE_RAFT.get()
            8 -> item = FSItems.CRIMSON_RAFT.get()
            9 -> item = FSItems.WARPED_RAFT.get()
            else -> item = FSItems.OAK_RAFT.get()
        }

        return item
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(this.getRaftItem())
    }   

    protected open fun destroy(damageSource: DamageSource) {
        val stack = getRaftItem()
        this.spawnAtLocation(stack)
    }

    companion object {
        private val DATA_ID_TYPE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(RaftEntity::class.java, EntityDataSerializers.INT)
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