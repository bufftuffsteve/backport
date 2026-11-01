package com.buffsteve.autumnbackport.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Red shrub, ported from 26.3 (where it reuses the plain "bush" block class).
 * Grows on dirt like grass. Bone meal spreads a new shrub to an empty spot next to it.
 */
public class RedShrubBlock extends BushBlock implements BonemealableBlock {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 13, 16);

    public RedShrubBlock(Properties properties) {
        super(properties);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // ---- Bone meal: spread to one free neighbouring spot ----

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (canSpreadTo(level, pos.relative(direction), state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        for (Direction direction : Util.shuffledCopy(Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new), random)) {
            BlockPos target = pos.relative(direction);
            if (canSpreadTo(level, target, state)) {
                level.setBlockAndUpdate(target, defaultBlockState());
                return;
            }
        }
    }

    private static boolean canSpreadTo(LevelReader level, BlockPos target, BlockState state) {
        return level.isEmptyBlock(target) && state.canSurvive(level, target);
    }
}
