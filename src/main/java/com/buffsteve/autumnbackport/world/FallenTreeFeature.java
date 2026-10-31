package com.buffsteve.autumnbackport.world;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

/**
 * A fallen tree: a one-block stump, and a few blocks away a log lying on its side.
 * Only generates when the whole log fits (26.3 would leave just the stump).
 * Port of 1.21.5's minecraft:fallen_tree (26.3 uses it for fallen poplars).
 */
public class FallenTreeFeature extends Feature<FallenTreeConfiguration> {
    private static final int MAX_GROUND_GAP = 2;
    private static final int MAX_FALL_HEIGHT = 5;

    public FallenTreeFeature() {
        super(FallenTreeConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<FallenTreeConfiguration> context) {
        FallenTreeConfiguration config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int length = config.logLength().sample(random) - 2;
        BlockPos.MutableBlockPos start = origin.relative(direction, 2 + random.nextInt(2)).mutable();
        findGround(level, start);
        // Change from 26.3: there, the stump is placed even when the log doesn't fit, which leaves
        // lots of lone stumps on hilly ground. Here, both are placed or neither is.
        if (!canPlaceWholeLog(level, length, start, direction)) {
            return false;
        }
        placeStump(config, level, random, origin.mutable());
        placeLog(config, level, random, length, start, direction);
        return true;
    }

    /** Starting one block up, steps down until it finds open space over solid ground. */
    private void findGround(WorldGenLevel level, BlockPos.MutableBlockPos pos) {
        pos.move(Direction.UP, 1);
        for (int i = 0; i <= MAX_FALL_HEIGHT; i++) {
            if (canPlaceOn(level, pos)) {
                return;
            }
            pos.move(Direction.DOWN);
        }
    }

    private void placeStump(FallenTreeConfiguration config, WorldGenLevel level, RandomSource random, BlockPos.MutableBlockPos pos) {
        BlockPos stump = placeLogBlock(config, level, random, pos, Function.identity());
        decorate(level, random, Set.of(stump), config.stumpDecorators());
    }

    /** Every block of the log must be free, with no more than 2 blocks in a row over a gap. */
    private boolean canPlaceWholeLog(WorldGenLevel level, int length, BlockPos.MutableBlockPos pos, Direction direction) {
        int gap = 0;
        for (int i = 0; i < length; i++) {
            if (!TreeFeature.validTreePos(level, pos)) {
                return false;
            }
            if (!isOverSolidGround(level, pos)) {
                if (++gap > MAX_GROUND_GAP) {
                    return false;
                }
            } else {
                gap = 0;
            }
            pos.move(direction);
        }
        pos.move(direction.getOpposite(), length);
        return true;
    }

    private void placeLog(FallenTreeConfiguration config, WorldGenLevel level, RandomSource random, int length,
                          BlockPos.MutableBlockPos pos, Direction direction) {
        Set<BlockPos> logs = new HashSet<>();
        for (int i = 0; i < length; i++) {
            logs.add(placeLogBlock(config, level, random, pos,
                    state -> state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis())));
            pos.move(direction);
        }
        decorate(level, random, logs, config.logDecorators());
    }

    private boolean canPlaceOn(LevelAccessor level, BlockPos pos) {
        return TreeFeature.validTreePos(level, pos) && isOverSolidGround(level, pos);
    }

    private boolean isOverSolidGround(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);
    }

    private BlockPos placeLogBlock(FallenTreeConfiguration config, WorldGenLevel level, RandomSource random,
                                   BlockPos.MutableBlockPos pos, Function<BlockState, BlockState> modifier) {
        level.setBlock(pos, modifier.apply(config.trunkProvider().getState(random, pos)), 3);
        markAboveForPostProcessing(level, pos);
        return pos.immutable();
    }

    private void decorate(WorldGenLevel level, RandomSource random, Set<BlockPos> logs, List<TreeDecorator> decorators) {
        if (!decorators.isEmpty()) {
            TreeDecorator.Context context = new TreeDecorator.Context(level,
                    (pos, state) -> level.setBlock(pos, state, 19), random, logs, Set.of(), Set.of());
            decorators.forEach(decorator -> decorator.place(context));
        }
    }
}
