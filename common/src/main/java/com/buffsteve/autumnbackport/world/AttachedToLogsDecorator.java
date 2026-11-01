package com.buffsteve.autumnbackport.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

/**
 * Port of 1.21.5's minecraft:attached_to_logs: puts a block next to some logs
 * (26.3 uses it for brown mushrooms on top of fallen poplar logs).
 */
public class AttachedToLogsDecorator extends TreeDecorator {
    public static final Codec<AttachedToLogsDecorator> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(d -> d.probability),
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(d -> d.provider),
            Direction.CODEC.listOf().fieldOf("directions").forGetter(d -> d.directions)
    ).apply(i, AttachedToLogsDecorator::new));

    private final float probability;
    private final BlockStateProvider provider;
    private final List<Direction> directions;

    public AttachedToLogsDecorator(float probability, BlockStateProvider provider, List<Direction> directions) {
        this.probability = probability;
        this.provider = provider;
        this.directions = directions;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreePlacers.ATTACHED_TO_LOGS.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        for (BlockPos log : Util.shuffledCopy(context.logs(), random)) {
            BlockPos target = log.relative(Util.getRandom(directions, random));
            if (random.nextFloat() <= probability && context.isAir(target)) {
                context.setBlock(target, provider.getState(random, target));
            }
        }
    }
}
