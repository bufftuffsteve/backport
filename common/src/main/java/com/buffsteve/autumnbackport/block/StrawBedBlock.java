package com.buffsteve.autumnbackport.block;

import com.buffsteve.autumnbackport.registry.ModStats;
import com.mojang.serialization.MapCodec;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Straw bed, ported from 26.3: a cheap, single-use bed.
 *
 * - Sleeps like a normal bed (night or thunderstorm, no monsters nearby).
 * - Never sets your spawn point (see ModEvents.shouldBlockSpawnPoint).
 * - Falls apart when you get up, whether you slept through the night or not.
 * - In the Nether and End it just falls apart when used, instead of exploding.
 * - Low (4px) and not bouncy.
 *
 * Built on 1.21.1's BedBlock so all the vanilla sleeping logic works, but drawn
 * as a normal block model instead of the bed renderer.
 */
public class StrawBedBlock extends BedBlock {
    public static final MapCodec<StrawBedBlock> CODEC = simpleCodec(StrawBedBlock::new);

    // 26.3 shapes: a 4px mattress on both halves, plus a 5px pillow on the far half of the head.
    private static final VoxelShape BASE = Block.box(0, 0, 0, 16, 4, 16);
    private static final Map<Direction, VoxelShape> HEAD_SHAPES = new EnumMap<>(Direction.class);

    static {
        HEAD_SHAPES.put(Direction.NORTH, Shapes.or(BASE, Block.box(0, 0, 0, 16, 5, 8)));
        HEAD_SHAPES.put(Direction.SOUTH, Shapes.or(BASE, Block.box(0, 0, 8, 16, 5, 16)));
        HEAD_SHAPES.put(Direction.EAST, Shapes.or(BASE, Block.box(8, 0, 0, 16, 5, 16)));
        HEAD_SHAPES.put(Direction.WEST, Shapes.or(BASE, Block.box(0, 0, 0, 8, 5, 16)));
    }

    /** Where the player lies: 26.3 puts them 2px above the mattress top (4px + 2px). */
    public static final double SLEEP_HEIGHT = 4.0 / 16.0 + 0.125;

    public StrawBedBlock(Properties properties) {
        // The color is only used by the vanilla bed renderer, which this block doesn't use.
        super(DyeColor.YELLOW, properties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<BedBlock> codec() {
        return (MapCodec<BedBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PART) == BedPart.HEAD ? HEAD_SHAPES.get(state.getValue(FACING)) : BASE;
    }

    // ---- Drawn with its JSON model, no block entity ----

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    // ---- Using it ----

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        // Nether and End: a normal bed explodes; a straw bed just falls apart.
        if (!canSetSpawn(level)) {
            if (!level.isClientSide) {
                destroyBed(level, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    /** Lying down counts toward the new statistic. Called from LivingEntityMixin on every loader. */
    public static void onLieDown(LivingEntity sleeper, BlockPos pos) {
        if (sleeper instanceof ServerPlayer player && player.level().getBlockState(pos).getBlock() instanceof StrawBedBlock) {
            player.awardStat(ModStats.SLEEP_IN_STRAW_BED.get());
        }
    }

    /**
     * Getting up destroys the bed, whether you slept through the night or not.
     * Called from LivingEntityMixin, after vanilla has marked the bed as free again.
     */
    public static void onGetUp(LivingEntity sleeper, BlockPos pos) {
        Level level = sleeper.level();
        if (!level.isClientSide && level.getBlockState(pos).getBlock() instanceof StrawBedBlock) {
            destroyBed(level, pos);
        }
    }

    /** Removes the bed without dropping it. The other half disappears on its own. */
    private static void destroyBed(Level level, BlockPos pos) {
        // 26.3 has its own straw "break" sound; hay's break sound is the closest on 1.21.1.
        level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    // ---- Not bouncy (normal beds are); full fall damage, like landing on the ground ----

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }
}
