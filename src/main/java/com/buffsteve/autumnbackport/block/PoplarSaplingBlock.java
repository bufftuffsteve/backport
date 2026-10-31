package com.buffsteve.autumnbackport.block;

import com.buffsteve.autumnbackport.world.ModTreeFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A sapling that grows into a red, orange or yellow poplar at random, like 26.3.
 * Growth speed, light needs and bone meal work exactly like a vanilla sapling.
 */
public class PoplarSaplingBlock extends SaplingBlock {
    public PoplarSaplingBlock(Properties properties) {
        super(ModTreeFeatures.RED, properties); // the grower passed here is only a default; see below
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            // First step: the sapling gets ready (same as vanilla).
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            // Second step: grow a tree of a random color.
            ModTreeFeatures.randomColor(random).growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }
}
