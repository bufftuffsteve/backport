package com.buffsteve.autumnbackport.world;

import com.buffsteve.autumnbackport.block.ShelfMushroomBlock;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

/**
 * Puts shelf mushrooms on the lower trunk of a newly grown tree.
 * Line-by-line port of 26.3's ShelfMushroomDecorator, ID minecraft:shelf_mushroom.
 *
 * With chance {@code probability}, it tries the 1st to 4th log above the base, on two
 * neighbouring sides of the trunk, 25% per side, at most one mushroom per log.
 * Never next to water, and never stacked directly on another shelf mushroom.
 */
public class ShelfMushroomDecorator extends TreeDecorator {
    public static final MapCodec<ShelfMushroomDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
            .fieldOf("probability")
            .xmap(ShelfMushroomDecorator::new, decorator -> decorator.probability);

    private static final int MIN_HEIGHT_OFFSET = 1;
    private static final int MAX_HEIGHT_OFFSET = 4;
    private static final float PER_SIDE_CHANCE = 0.25F;

    private final float probability;

    public ShelfMushroomDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreePlacers.SHELF_MUSHROOM_DECORATOR.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        if (random.nextFloat() >= probability) {
            return;
        }
        List<BlockPos> logs = context.logs(); // already sorted bottom to top
        if (logs.isEmpty()) {
            return;
        }
        if (logs.getFirst().getY() == logs.getLast().getY()) {
            placeOnFallenLog(context, logs, random);
        } else {
            placeOnStandingTree(context, logs, random);
        }
    }

    private static void placeOnStandingTree(Context context, List<BlockPos> logs, RandomSource random) {
        Direction first = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction[] sides = {first, first.getClockWise()};
        int baseY = logs.getFirst().getY();
        for (BlockPos log : logs) {
            int height = log.getY() - baseY;
            if (height < MIN_HEIGHT_OFFSET || height > MAX_HEIGHT_OFFSET) {
                continue;
            }
            for (Direction side : sides) {
                if (random.nextFloat() > PER_SIDE_CHANCE) {
                    continue;
                }
                BlockPos target = log.relative(side);
                if (canReplace(context, target) && !isShelfMushroom(context, target.below())) {
                    place(context, target, side, random);
                    break; // one per log
                }
            }
        }
    }

    /** Lying logs (26.3's fallen trees). Not used by poplars, kept so the decorator works the same anywhere. */
    private static void placeOnFallenLog(Context context, List<BlockPos> logs, RandomSource random) {
        boolean alongX = logs.getFirst().getX() != logs.getLast().getX();
        Direction[] sides = alongX
                ? new Direction[] {Direction.NORTH, Direction.SOUTH}
                : new Direction[] {Direction.EAST, Direction.WEST};
        for (BlockPos log : logs) {
            for (Direction side : sides) {
                if (random.nextFloat() > PER_SIDE_CHANCE) {
                    continue;
                }
                BlockPos target = log.relative(side);
                if (canReplace(context, target)
                        && !hasAdjacentShelfMushroom(context, target)
                        && !hasAdjacentShelfMushroom(context, log)) {
                    place(context, target, side, random);
                }
            }
        }
    }

    private static void place(Context context, BlockPos pos, Direction facing, RandomSource random) {
        context.setBlock(pos, ModBlocks.SHELF_MUSHROOM.get().defaultBlockState()
                .setValue(ShelfMushroomBlock.AGE, random.nextInt(2))
                .setValue(ShelfMushroomBlock.FACING, facing));
    }

    /** Air or anything trees may overwrite (grass, flowers...), and no water there or beside it. */
    private static boolean canReplace(Context context, BlockPos pos) {
        if (!TreeFeature.validTreePos(context.level(), pos)) {
            return false;
        }
        if (isWater(context, pos)) {
            return false;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isWater(context, pos.relative(direction))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isWater(Context context, BlockPos pos) {
        return context.level().isStateAtPosition(pos, state -> state.is(Blocks.WATER));
    }

    private static boolean isShelfMushroom(Context context, BlockPos pos) {
        return context.level().isStateAtPosition(pos, state -> state.is(ModBlocks.SHELF_MUSHROOM.get()));
    }

    private static boolean hasAdjacentShelfMushroom(Context context, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isShelfMushroom(context, pos.relative(direction))) {
                return true;
            }
        }
        return false;
    }
}
