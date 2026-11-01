package com.buffsteve.autumnbackport.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

/**
 * The Dappled Forest biome from 26.3 ("Wilderness Bound"). The biome and its features are defined in
 * data/minecraft/worldgen (generated on the 1.21.1 branch, converted with tools/convert_data_to_1.20.1.py).
 * Where it generates is decided in DappledForestRegion.
 */
public final class ModDappledForest {
    public static final ResourceKey<Biome> DAPPLED_FOREST = ResourceKey.create(Registries.BIOME, id("dappled_forest"));

    // Noise for the coarse dirt patches on the forest floor (new in 26.3).
    public static final ResourceKey<NormalNoise.NoiseParameters> SMALL_PATCH = ResourceKey.create(Registries.NOISE, id("small_patch"));

    // Configured features (what to place)
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_DAPPLED_FOREST_CF = configured("trees_dappled_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_SHRUB_CF = configured("red_shrub");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_SHRUB_CF = configured("patch_red_shrub");

    // Placed features (where and how often)
    public static final ResourceKey<PlacedFeature> RED_POPLAR = placed("red_poplar");
    public static final ResourceKey<PlacedFeature> ORANGE_POPLAR = placed("orange_poplar");
    public static final ResourceKey<PlacedFeature> YELLOW_POPLAR = placed("yellow_poplar");
    public static final ResourceKey<PlacedFeature> RED_POPLAR_LEAF_LITTER = placed("red_poplar_leaf_litter");
    public static final ResourceKey<PlacedFeature> ORANGE_POPLAR_LEAF_LITTER = placed("orange_poplar_leaf_litter");
    public static final ResourceKey<PlacedFeature> YELLOW_POPLAR_LEAF_LITTER = placed("yellow_poplar_leaf_litter");
    public static final ResourceKey<PlacedFeature> FALLEN_POPLAR_TREE = placed("fallen_poplar_tree");
    public static final ResourceKey<PlacedFeature> TREES_DAPPLED_FOREST = placed("trees_dappled_forest");
    public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_DAPPLED_FOREST = placed("brown_mushroom_dappled_forest");
    public static final ResourceKey<PlacedFeature> PATCH_RED_SHRUB = placed("patch_red_shrub");

    /** 26.3's dry foliage tint for this biome (used by leaf litter). */
    public static final int DRY_FOLIAGE_COLOR = 0x8C3A04;
    /** 26.3's default dry foliage tint for everywhere else. */
    public static final int DEFAULT_DRY_FOLIAGE_COLOR = 0x5C3C32;
    /** How the client recognizes a Dappled Forest biome from its colors (biomes don't know their own ID). */
    public static final int FOLIAGE_COLOR = 0xE68E30;

    private static ResourceLocation id(String name) {
        return new ResourceLocation(name);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(name));
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(name));
    }

    private ModDappledForest() {}
}
