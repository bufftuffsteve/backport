package com.buffsteve.autumnbackport.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

/** Settings for a fallen tree: the log block, how long the fallen part is, and decorations. Same fields as 1.21.5. */
public record FallenTreeConfiguration(BlockStateProvider trunkProvider, IntProvider logLength,
                                      List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators)
        implements FeatureConfiguration {
    public static final Codec<FallenTreeConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
            BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(FallenTreeConfiguration::trunkProvider),
            IntProvider.codec(0, 16).fieldOf("log_length").forGetter(FallenTreeConfiguration::logLength),
            TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(FallenTreeConfiguration::stumpDecorators),
            TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(FallenTreeConfiguration::logDecorators)
    ).apply(i, FallenTreeConfiguration::new));
}
