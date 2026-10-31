package com.buffsteve.autumnbackport.entity;

import com.buffsteve.autumnbackport.registry.ModItems;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Cushion, ported from 26.3: a flat, colored seat you place on top of a block and sit on.
 *
 * It's an entity (like a painting), not a block, so it can sit on top of slabs, stairs,
 * carpets, cauldron rims and so on. Right-click to sit (sneak to skip), punch to pick it up.
 * Breaks if the block under it goes away, if it gets buried, or if it touches fire or lava.
 */
public class Cushion extends BlockAttachedEntity {
    private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
    // 1.21.1 can't sync a DyeColor directly, so the color goes over the network as its number.
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Cushion.class, EntityDataSerializers.INT);
    private static final double TINY = 1.0E-7;
    /** How often (in ticks) it checks for fire and lava. */
    private static final int HAZARD_CHECK_INTERVAL = 10;

    public Cushion(EntityType<? extends Cushion> type, Level level) {
        super(type, level);
    }

    public DyeColor getColor() {
        return DyeColor.byId(entityData.get(DATA_COLOR));
    }

    public void setColor(DyeColor color) {
        entityData.set(DATA_COLOR, color.getId());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_COLOR, DEFAULT_COLOR.getId());
    }

    // ---- Position and size ----

    /** Unlike paintings, a cushion sits exactly where it was placed (not snapped to the block grid). */
    @Override
    public void setPos(double x, double y, double z) {
        setPosRaw(x, y, z);
        super.setPos(x, y, z);
    }

    @Override
    protected void recalculateBoundingBox() {
        setBoundingBox(makeBoundingBox());
    }

    // ---- Survival rules ----

    @Override
    public boolean survives() {
        return wouldSurviveAt(level(), getBoundingBox());
    }

    /** Placing: there must be something under it, and it can't be inside a block. */
    public static boolean canBePlacedAt(Level level, AABB box) {
        return wouldSurviveAt(level, box) && !isAnchorBuried(level, box);
    }

    public static boolean wouldSurviveAt(Level level, AABB box) {
        return hasAnchorBelow(level, box) && !isCoveredBySuffocatingBlocks(level, box);
    }

    /** Something with a shape directly under (within 1/8 block of) the cushion's bottom. */
    private static boolean hasAnchorBelow(Level level, AABB box) {
        AABB below = new AABB(box.minX, box.minY - 0.015625, box.minZ, Math.nextDown(box.maxX), box.minY, Math.nextDown(box.maxZ))
                .expandTowards(0.0, -0.125, 0.0);
        for (BlockPos pos : blocksIn(below)) {
            VoxelShape shape = level.getBlockState(pos).getShape(level, pos);
            if (!shape.isEmpty() && shape.bounds().move(pos).intersects(below)) {
                return true;
            }
        }
        return false;
    }

    /** True if the cushion's bottom layer is completely inside a single block's collision shape. */
    private static boolean isAnchorBuried(Level level, AABB box) {
        AABB bottom = new AABB(box.minX, box.minY, box.minZ, box.maxX, box.minY + 0.015625, box.maxZ).deflate(TINY);
        VoxelShape bottomShape = Shapes.create(bottom);
        for (VoxelShape collision : level.getBlockCollisions(null, bottom)) {
            if (Shapes.join(bottomShape, collision, BooleanOp.ONLY_FIRST).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /** True if every block the cushion overlaps would suffocate you (it's been buried). */
    private static boolean isCoveredBySuffocatingBlocks(Level level, AABB box) {
        for (BlockPos pos : blocksIn(box.deflate(TINY))) {
            if (!level.getBlockState(pos).isSuffocating(level, pos)) {
                return false;
            }
        }
        return true;
    }

    /** Every block position the box touches. */
    private static Iterable<BlockPos> blocksIn(AABB box) {
        return BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ),
                Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ));
    }

    // ---- Fire and lava ----

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel && !isRemoved() && tickCount % HAZARD_CHECK_INTERVAL == 0) {
            if (level().getFluidState(blockPosition()).is(FluidTags.LAVA)) {
                hurt(damageSources().lava(), 4.0F);
            } else {
                destroyIfInFire(serverLevel);
            }
        }
    }

    /** Burns up if it touches a fire block. Also checked right after placing. */
    public void destroyIfInFire(ServerLevel level) {
        if (isRemoved()) {
            return;
        }
        for (BlockPos pos : blocksIn(getBoundingBox().deflate(TINY))) {
            if (level.getBlockState(pos).is(BlockTags.FIRE)) {
                hurt(damageSources().inFire(), 1.0F);
                return;
            }
        }
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        if (!isRemoved()) {
            kill();
            dropItem(lightning);
        }
    }

    // ---- Breaking ----

    /** Adventure-mode players and spawn protection can't break it. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player
                && (!player.mayBuild() || !level().mayInteract(player, getPos()))) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void dropItem(@Nullable Entity breaker) {
        playSound(SoundEvents.WOOL_BREAK, 1.0F, 1.0F); // 26.3 has its own cushion sounds
        showBreakingParticles();
        if (!(level() instanceof ServerLevel serverLevel) || !serverLevel.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            return;
        }
        if (breaker instanceof Player player && player.hasInfiniteMaterials()) {
            return; // creative mode: no drop
        }
        spawnAtLocation(itemWithData());
    }

    private void showBreakingParticles() {
        if (level() instanceof ServerLevel serverLevel) {
            BlockState wool = woolBlock(getColor()).defaultBlockState();
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, wool),
                    getX(), getY(0.6666666666666666), getZ(), 10,
                    getBbWidth() / 4.0F, getBbHeight() / 4.0F, getBbWidth() / 4.0F, 0.05);
        }
    }

    private static Block woolBlock(DyeColor color) {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(
                net.minecraft.resources.ResourceLocation.withDefaultNamespace(color.getName() + "_wool"));
    }

    /** The item for this color, keeping a name given with a name tag or anvil. */
    private ItemStack itemWithData() {
        ItemStack stack = new ItemStack(ModItems.CUSHIONS.get(getColor()).get());
        if (hasCustomName()) {
            stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, getCustomName());
        }
        return stack;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.CUSHIONS.get(getColor()).get());
    }

    // ---- Sitting ----

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive() || isVehicle()) {
            return InteractionResult.PASS;
        }
        if (!level().isClientSide && player.startRiding(this)) {
            playSound(SoundEvents.WOOL_PLACE, 1.0F, 1.0F); // sit sound
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!level().isClientSide && getRemovalReason() == null) {
            playSound(SoundEvents.WOOL_STEP, 1.0F, 1.0F); // get-up sound
        }
    }

    // ---- Saving (same "color" field as 26.3, so cushions survive the upgrade) ----

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("color", getColor().getSerializedName());
    }

    /**
     * Doesn't read the saved block position (TileX/Y/Z) like paintings do: a cushion's position
     * already comes from where it is, and cushions placed by camp structures carry the position
     * of the world they were built in, which would only log an error.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        setColor(DyeColor.byName(tag.getString("color"), DEFAULT_COLOR));
    }
}
