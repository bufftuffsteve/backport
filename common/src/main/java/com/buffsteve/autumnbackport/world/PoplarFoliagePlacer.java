package com.buffsteve.autumnbackport.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

/**
 * The poplar canopy, ported from 26.3's PoplarFoliagePlacer.
 *
 * Each layer is a diamond (rhombus) of leaves. The layers taper toward the top,
 * the top two layers are trimmed, two opposite corners of each diamond are a bit
 * fuller (picked at random per tree, so trees lean one of two ways), and edge
 * blocks have a random chance (side_hole_chance) of being left out.
 * Inside the widest canopies, sideways logs run through the leaves as inner branches.
 */
public class PoplarFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<PoplarFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            foliagePlacerParts(instance).and(instance.group(
                    IntProvider.codec(5, 16).fieldOf("height").forGetter(p -> p.height),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("side_hole_chance").forGetter(p -> p.sideHoleChance)
            )).apply(instance, PoplarFoliagePlacer::new));

    private final IntProvider height;
    private final float sideHoleChance;

    public PoplarFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height, float sideHoleChance) {
        super(radius, offset);
        this.height = height;
        this.sideHoleChance = sideHoleChance;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacers.POPLAR_FOLIAGE_PLACER.get();
    }

    @Override
    public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
        return height.sample(random);
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config,
                                 int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        boolean doubleTrunk = attachment.doubleTrunk();
        BlockPos center = attachment.pos().above(offset);
        int radius = foliageRadius + attachment.radiusOffset() - 1;
        boolean flip = random.nextBoolean(); // which pair of opposite corners gets the fuller shape
        int h = foliageHeight;

        // Top three layers taper in.
        placeRow(level, setter, random, config, center, radius - 2, h - 1, doubleTrunk, h, flip);
        placeRow(level, setter, random, config, center, radius - 1, h - 2, doubleTrunk, h, flip);
        placeRow(level, setter, random, config, center, radius - 1, h - 3, doubleTrunk, h, flip);
        // Full-width layers down to just above the branch point.
        for (int y = h - 4; y >= 1; y--) {
            placeRow(level, setter, random, config, center, radius, y, doubleTrunk, h, flip);
        }
        // Inner branch logs through the widest layer.
        replaceLeavesWithLogs(level, setter, config, random, center, radius, h - 4, doubleTrunk, h, flip);
        // Bottom two layers taper back in, under the branches.
        placeRow(level, setter, random, config, center, radius - 1, 0, doubleTrunk, h, flip);
        placeRow(level, setter, random, config, center, Mth.clamp(radius - 2, 1, 2), -1, doubleTrunk, h, flip);
    }

    private void placeRow(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config,
                          BlockPos center, int radius, int y, boolean doubleTrunk, int h, boolean flip) {
        int extra = doubleTrunk ? 1 : 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius + extra; dx++) {
            for (int dz = -radius; dz <= radius + extra; dz++) {
                if (!shouldSkip(random, dx, y, dz, radius, doubleTrunk, h, flip)) {
                    pos.setWithOffset(center, dx, y, dz);
                    tryPlaceLeaf(level, setter, random, config, pos);
                }
            }
        }
    }

    private boolean shouldSkip(RandomSource random, int dx, int y, int dz, int radius, boolean doubleTrunk, int h, boolean flip) {
        boolean partialRow = isPartialRow(h, y);
        int cornerCut = cornerCut(dx, dz, radius, partialRow, flip);
        int adx = Mth.abs(dx);
        int adz = Mth.abs(dz);
        boolean onEdge = adx == radius || adz == radius;
        if (partialRow && onEdge) {
            return true;
        }
        int hole = random.nextFloat() <= sideHoleChance ? 1 : 0;
        return !isWithinRhombus(radius, adx, adz, cornerCut, hole);
    }

    private void replaceLeavesWithLogs(LevelSimulatedReader level, FoliageSetter setter, TreeConfiguration config, RandomSource random,
                                       BlockPos center, int radius, int y, boolean doubleTrunk, int h, boolean flip) {
        int extra = doubleTrunk ? 1 : 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius + extra; dx++) {
            for (int dz = -radius; dz <= radius + extra; dz++) {
                int adz = Mth.abs(dz);
                int adx = Mth.abs(dx);
                if (!isWithinRhombus(radius, adx, adz, cornerCut(dx, dz, radius, isPartialRow(h, y), flip), 2)) {
                    continue;
                }
                // Only along the two centre lines, and only at least 4 blocks in from the edge.
                boolean onXLine = adz == 0 && radius - adx >= 4;
                boolean onZLine = adx == 0 && radius - adz >= 4;
                if (onXLine || onZLine) {
                    pos.setWithOffset(center, dx, y, dz);
                    Direction along = Direction.fromAxisAndDirection(adz == 0 ? Direction.Axis.X : Direction.Axis.Z, Direction.AxisDirection.POSITIVE);
                    tryPlaceLog(level, setter, random, config, pos, along);
                }
            }
        }
    }

    /** Swaps a leaf for a sideways log, but only if that spot actually holds this tree's leaf. */
    private static void tryPlaceLog(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config,
                                    BlockPos pos, Direction along) {
        if (level.isStateAtPosition(pos, state -> state.equals(config.foliageProvider.getState(random, pos)))) {
            setter.set(pos, PoplarTrunkPlacer.sideways(along).apply(config.trunkProvider.getState(random, pos)));
        }
    }

    /** The second and third layers from the top are trimmed. */
    private static boolean isPartialRow(int h, int y) {
        return h - 1 == y || h - 2 == y;
    }

    private static int cornerCut(int dx, int dz, int radius, boolean partialRow, boolean flip) {
        boolean fullerCorner = flip ? isTopLeftOrBottomRight(dx, dz) : isBottomLeftOrTopRight(dx, dz);
        if (fullerCorner) {
            return radius - 1;
        }
        return partialRow ? radius + 1 : radius;
    }

    private static boolean isWithinRhombus(int radius, int adx, int adz, int cornerCut, int hole) {
        return adx + adz <= radius * 2 - (cornerCut + hole);
    }

    private static boolean isBottomLeftOrTopRight(int dx, int dz) {
        return (dx > 0 && dz < 0) || (dz > 0 && dx < 0);
    }

    private static boolean isTopLeftOrBottomRight(int dx, int dz) {
        return (dx > 0 && dz > 0) || (dz < 0 && dx < 0);
    }

    // Required by FoliagePlacer, but this placer decides everything in shouldSkip() above.
    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        throw new IllegalStateException("PoplarFoliagePlacer uses its own row logic");
    }
}
