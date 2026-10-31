package com.buffsteve.autumnbackport.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Poplar leaves: normal leaves plus a falling-leaf particle in the leaves' own color.
 * Same rule as 26.3's UntintedParticleLeavesBlock: a 1% chance each frame, and only
 * when the block underneath doesn't have a full top face for the leaf to land on.
 */
public class PoplarLeavesBlock extends LeavesBlock {
    private static final float PARTICLE_CHANCE = 0.01F;
    private final Supplier<? extends ParticleOptions> particle;

    public PoplarLeavesBlock(Supplier<? extends ParticleOptions> particle, Properties properties) {
        super(properties);
        this.particle = particle;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random); // rain dripping off leaves
        if (random.nextFloat() < PARTICLE_CHANCE) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (!isFaceFull(belowState.getCollisionShape(level, below), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, pos, random, particle.get());
            }
        }
    }
}
