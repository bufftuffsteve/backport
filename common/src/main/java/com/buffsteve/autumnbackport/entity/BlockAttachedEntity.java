package com.buffsteve.autumnbackport.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Copy of 1.21's BlockAttachedEntity (the shared base of paintings, item frames and cushions),
 * which 1.20.1 doesn't have yet. An entity that stays put, breaks when it can't survive
 * (checked every 100 ticks), breaks when hit or pushed, and drops itself.
 */
public abstract class BlockAttachedEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int checkInterval;
    protected BlockPos pos;

    protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected abstract void recalculateBoundingBox();

    public abstract boolean survives();

    public abstract void dropItem(@Nullable Entity entity);

    @Override
    public void tick() {
        if (!level().isClientSide) {
            checkBelowWorld();
            if (checkInterval++ == 100) {
                checkInterval = 0;
                if (!isRemoved() && !survives()) {
                    discard();
                    dropItem(null);
                }
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player player) {
            return !level().mayInteract(player, pos) || hurt(damageSources().playerAttack(player), 0.0F);
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        if (!isRemoved() && !level().isClientSide) {
            kill();
            markHurt();
            dropItem(source.getEntity());
        }
        return true;
    }

    @Override
    public void move(MoverType type, Vec3 movement) {
        if (!level().isClientSide && !isRemoved() && movement.lengthSqr() > 0.0) {
            kill();
            dropItem(null);
        }
    }

    @Override
    public void push(double x, double y, double z) {
        if (!level().isClientSide && !isRemoved() && x * x + y * y + z * z > 0.0) {
            kill();
            dropItem(null);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        BlockPos blockPos = getPos();
        tag.putInt("TileX", blockPos.getX());
        tag.putInt("TileY", blockPos.getY());
        tag.putInt("TileZ", blockPos.getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        BlockPos blockPos = new BlockPos(tag.getInt("TileX"), tag.getInt("TileY"), tag.getInt("TileZ"));
        if (!blockPos.closerThan(blockPosition(), 16.0)) {
            LOGGER.error("Block-attached entity at invalid position: {}", blockPos);
        } else {
            pos = blockPos;
        }
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public void setPos(double x, double y, double z) {
        pos = BlockPos.containing(x, y, z);
        recalculateBoundingBox();
        hasImpulse = true;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
    }

    @Override
    public void refreshDimensions() {
    }

    /** 1.20.1 entities say how they're sent to clients; this is the standard spawn packet. */
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
