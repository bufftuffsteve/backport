package com.buffsteve.autumnbackport.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

/**
 * The poplar trunk, ported from 26.3's PoplarTrunkPlacer.
 *
 * A straight trunk. A few blocks below the top (trunk_height_above_branches) it sprouts
 * 1-4 short sideways branches in random directions, and the canopy is centred there.
 */
public class PoplarTrunkPlacer extends TrunkPlacer {
    public static final Codec<PoplarTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            trunkPlacerParts(instance).and(instance.group(
                    IntProvider.codec(0, 8).fieldOf("trunk_height_above_branches").forGetter(p -> p.trunkHeightAboveBranches),
                    IntProvider.codec(1, 4).fieldOf("branch_amount").forGetter(p -> p.branchAmount)
            )).apply(instance, PoplarTrunkPlacer::new));

    private final IntProvider trunkHeightAboveBranches;
    private final IntProvider branchAmount;

    public PoplarTrunkPlacer(int baseHeight, int heightRandA, int heightRandB,
                             IntProvider trunkHeightAboveBranches, IntProvider branchAmount) {
        super(baseHeight, heightRandA, heightRandB);
        this.trunkHeightAboveBranches = trunkHeightAboveBranches;
        this.branchAmount = branchAmount;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacers.POPLAR_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                                            RandomSource random, int height, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);
        int branchY = height - trunkHeightAboveBranches.sample(random);

        for (int y = 0; y < height; y++) {
            placeLog(level, blockSetter, random, pos.above(y), config);
            // Shuffled every layer, like 26.3, so the same seed grows the same tree.
            List<Direction> directions = shuffledHorizontalDirections(random);
            if (y == branchY - 1) {
                int branches = branchAmount.sample(random);
                for (int i = 0; i < branches; i++) {
                    Direction direction = directions.get(i);
                    placeLog(level, blockSetter, random, pos.above(y).relative(direction, 1), config, sideways(direction));
                }
            }
        }
        return List.of(new FoliagePlacer.FoliageAttachment(pos.above(branchY), 0, false));
    }

    /** Turns a log so it lies along the given direction. */
    static Function<BlockState, BlockState> sideways(Direction direction) {
        return state -> state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis());
    }

    private static List<Direction> shuffledHorizontalDirections(RandomSource random) {
        return Direction.allShuffled(random).stream().filter(d -> !d.getAxis().isVertical()).toList();
    }
}
