package com.buffsteve.autumnbackport.world;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

/**
 * The three poplar trees, one per leaf color, using 26.3's IDs (minecraft:red_poplar etc.).
 * Their definitions are in data/minecraft/worldgen/configured_feature (generated on the
 * 1.21.1 branch and converted to 1.20.1 with tools/convert_data_to_1.20.1.py).
 */
public final class ModTreeFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_POPLAR = key("red_poplar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_POPLAR = key("orange_poplar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_POPLAR = key("yellow_poplar");
    // Dappled Forest versions: same trees, plus leaf litter scattered around the base.
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_POPLAR_LEAF_LITTER = key("red_poplar_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_POPLAR_LEAF_LITTER = key("orange_poplar_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_POPLAR_LEAF_LITTER = key("yellow_poplar_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_POPLAR_TREE = key("fallen_poplar_tree");

    // One grower per color. The sapling picks one at random each time it grows.
    public static final AbstractTreeGrower RED = grower(RED_POPLAR);
    public static final AbstractTreeGrower ORANGE = grower(ORANGE_POPLAR);
    public static final AbstractTreeGrower YELLOW = grower(YELLOW_POPLAR);
    private static final List<AbstractTreeGrower> ALL_COLORS = List.of(RED, ORANGE, YELLOW);

    public static AbstractTreeGrower randomColor(RandomSource random) {
        return ALL_COLORS.get(random.nextInt(ALL_COLORS.size()));
    }

    private static AbstractTreeGrower grower(ResourceKey<ConfiguredFeature<?, ?>> tree) {
        return new AbstractTreeGrower() {
            @Override
            protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
                return tree;
            }
        };
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(name));
    }

    private ModTreeFeatures() {}
}
