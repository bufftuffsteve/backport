package com.buffsteve.autumnbackport.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Scatters blocks on the ground around the base of a tree (26.3 uses it for leaf litter).
 * Port of 1.21.5's minecraft:place_on_ground. Tries {@code tries} random spots within
 * {@code radius} blocks sideways and {@code height} up/down of the trunk base.
 */
public class PlaceOnGroundDecorator extends TreeDecorator {
    public static final MapCodec<PlaceOnGroundDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(d -> d.tries),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(2).forGetter(d -> d.radius),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(1).forGetter(d -> d.height),
            BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(d -> d.provider)
    ).apply(i, PlaceOnGroundDecorator::new));

    private final int tries;
    private final int radius;
    private final int height;
    private final BlockStateProvider provider;

    public PlaceOnGroundDecorator(int tries, int radius, int height, BlockStateProvider provider) {
        this.tries = tries;
        this.radius = radius;
        this.height = height;
        this.provider = provider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreePlacers.PLACE_ON_GROUND.get();
    }

    @Override
    public void place(Context context) {
        List<BlockPos> base = lowestTrunkOrRoots(context);
        if (base.isEmpty()) {
            return;
        }
        BlockPos first = base.getFirst();
        int y = first.getY();
        int minX = first.getX(), maxX = first.getX(), minZ = first.getZ(), maxZ = first.getZ();
        for (BlockPos pos : base) {
            if (pos.getY() == y) {
                minX = Math.min(minX, pos.getX());
                maxX = Math.max(maxX, pos.getX());
                minZ = Math.min(minZ, pos.getZ());
                maxZ = Math.max(maxZ, pos.getZ());
            }
        }
        RandomSource random = context.random();
        BoundingBox box = new BoundingBox(minX, y, minZ, maxX, y, maxZ).inflatedBy(radius, height, radius);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < tries; i++) {
            pos.set(random.nextIntBetweenInclusive(box.minX(), box.maxX()),
                    random.nextIntBetweenInclusive(box.minY(), box.maxY()),
                    random.nextIntBetweenInclusive(box.minZ(), box.maxZ()));
            placeAbove(context, pos);
        }
    }

    /** Puts a block on top of {@code ground} if that spot is open sky-wise and the ground is solid. */
    private void placeAbove(Context context, BlockPos ground) {
        BlockPos above = ground.above();
        if (context.level().isStateAtPosition(above, s -> s.isAir() || s.is(Blocks.VINE))
                && context.level().isStateAtPosition(ground, BlockStateBase::canOcclude)
                && context.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ground).getY() <= above.getY()) {
            context.setBlock(above, provider.getState(context.random(), above));
        }
    }

    /** The bottom of the trunk (or roots, for trees that have them), like 1.21.5's TreeFeature helper. */
    private static List<BlockPos> lowestTrunkOrRoots(Context context) {
        List<BlockPos> result = new ArrayList<>();
        List<BlockPos> roots = context.roots();
        List<BlockPos> logs = context.logs();
        if (roots.isEmpty()) {
            result.addAll(logs);
        } else if (!logs.isEmpty() && roots.getFirst().getY() == logs.getFirst().getY()) {
            result.addAll(logs);
            result.addAll(roots);
        } else {
            result.addAll(roots);
        }
        return result;
    }
}
