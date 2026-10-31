package com.buffsteve.autumnbackport.world;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import java.util.List;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

/**
 * The Dappled Forest biome from 26.3 ("Wilderness Bound"), with every number taken from
 * 26.3's own files: data/minecraft/worldgen/biome/dappled_forest.json and the features it uses.
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

    // ---- Configured features ----

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);

        // 26.3 picks one of these by weight: red 200, orange 240, yellow 90, spruce 27, fallen 120.
        // 1.21.1's selector works with chances in order instead, so the weights are converted
        // into the same odds (each chance is that weight out of what's left).
        int[] weights = {200, 240, 90, 27, 120};
        float[] chances = sequentialChances(weights);
        FeatureUtils.register(context, TREES_DAPPLED_FOREST_CF, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(
                new WeightedPlacedFeature(placed.getOrThrow(RED_POPLAR_LEAF_LITTER), chances[0]),
                new WeightedPlacedFeature(placed.getOrThrow(ORANGE_POPLAR_LEAF_LITTER), chances[1]),
                new WeightedPlacedFeature(placed.getOrThrow(YELLOW_POPLAR_LEAF_LITTER), chances[2]),
                new WeightedPlacedFeature(placed.getOrThrow(TreePlacements.SPRUCE_CHECKED), chances[3])),
                placed.getOrThrow(FALLEN_POPLAR_TREE)));

        FeatureUtils.register(context, RED_SHRUB_CF, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.RED_SHRUB.get())));
        // 8 tries in a 15x7x15 area around one spot per chunk, only into air.
        FeatureUtils.register(context, PATCH_RED_SHRUB_CF, Feature.RANDOM_PATCH, new RandomPatchConfiguration(8, 7, 3,
                PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.RED_SHRUB.get())))));
    }

    /** Converts "pick by weight" into 1.21.1's "try each in order with this chance". */
    private static float[] sequentialChances(int[] weights) {
        float[] chances = new float[weights.length - 1];
        int remaining = 0;
        for (int w : weights) {
            remaining += w;
        }
        for (int i = 0; i < chances.length; i++) {
            chances[i] = (float) weights[i] / remaining;
            remaining -= weights[i];
        }
        return chances;
    }

    // ---- Placed features ----

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        // Single trees: only where a poplar sapling could grow.
        onSaplingGround(context, RED_POPLAR, configured, ModTreeFeatures.RED_POPLAR);
        onSaplingGround(context, ORANGE_POPLAR, configured, ModTreeFeatures.ORANGE_POPLAR);
        onSaplingGround(context, YELLOW_POPLAR, configured, ModTreeFeatures.YELLOW_POPLAR);
        onSaplingGround(context, RED_POPLAR_LEAF_LITTER, configured, ModTreeFeatures.RED_POPLAR_LEAF_LITTER);
        onSaplingGround(context, ORANGE_POPLAR_LEAF_LITTER, configured, ModTreeFeatures.ORANGE_POPLAR_LEAF_LITTER);
        onSaplingGround(context, YELLOW_POPLAR_LEAF_LITTER, configured, ModTreeFeatures.YELLOW_POPLAR_LEAF_LITTER);
        onSaplingGround(context, FALLEN_POPLAR_TREE, configured, ModTreeFeatures.FALLEN_POPLAR_TREE);

        // 6 tree attempts per chunk, not underwater.
        PlacementUtils.register(context, TREES_DAPPLED_FOREST, configured.getOrThrow(TREES_DAPPLED_FOREST_CF),
                CountPlacement.of(6), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        // A patch of brown mushrooms in every other chunk.
        PlacementUtils.register(context, BROWN_MUSHROOM_DAPPLED_FOREST, configured.getOrThrow(VegetationFeatures.PATCH_BROWN_MUSHROOM),
                RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
        // A small cluster of red shrubs in every chunk.
        PlacementUtils.register(context, PATCH_RED_SHRUB, configured.getOrThrow(PATCH_RED_SHRUB_CF),
                InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }

    private static void onSaplingGround(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                        HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> tree) {
        PlacementUtils.register(context, key, configured.getOrThrow(tree),
                PlacementUtils.filteredByBlockSurvival(ModBlocks.POPLAR_SAPLING.get()));
    }

    // ---- Noise ----

    public static void bootstrapNoise(BootstrapContext<NormalNoise.NoiseParameters> context) {
        // 26.3: one octave at scale -3 with amplitude 3.
        context.register(SMALL_PATCH, new NormalNoise.NoiseParameters(-3, 3.0));
    }

    // ---- The biome ----

    public static void bootstrapBiome(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PIG, 10, 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 8, 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 4, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 95, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));
        spawns.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);
        gen.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        gen.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        gen.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        add(gen, GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND, MiscOverworldPlacements.LAKE_LAVA_SURFACE);
        add(gen, GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE);
        add(gen, GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM, CavePlacements.MONSTER_ROOM_DEEP);
        add(gen, GenerationStep.Decoration.UNDERGROUND_ORES,
                OrePlacements.ORE_DIRT, OrePlacements.ORE_GRAVEL,
                OrePlacements.ORE_GRANITE_UPPER, OrePlacements.ORE_GRANITE_LOWER,
                OrePlacements.ORE_DIORITE_UPPER, OrePlacements.ORE_DIORITE_LOWER,
                OrePlacements.ORE_ANDESITE_UPPER, OrePlacements.ORE_ANDESITE_LOWER, OrePlacements.ORE_TUFF,
                OrePlacements.ORE_COAL_UPPER, OrePlacements.ORE_COAL_LOWER,
                OrePlacements.ORE_IRON_UPPER, OrePlacements.ORE_IRON_MIDDLE, OrePlacements.ORE_IRON_SMALL,
                OrePlacements.ORE_GOLD, OrePlacements.ORE_GOLD_LOWER,
                OrePlacements.ORE_REDSTONE, OrePlacements.ORE_REDSTONE_LOWER,
                OrePlacements.ORE_DIAMOND, OrePlacements.ORE_DIAMOND_MEDIUM, OrePlacements.ORE_DIAMOND_LARGE, OrePlacements.ORE_DIAMOND_BURIED,
                OrePlacements.ORE_LAPIS, OrePlacements.ORE_LAPIS_BURIED, OrePlacements.ORE_COPPER,
                CavePlacements.UNDERWATER_MAGMA,
                MiscOverworldPlacements.DISK_SAND, MiscOverworldPlacements.DISK_CLAY, MiscOverworldPlacements.DISK_GRAVEL);
        add(gen, GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_WATER, MiscOverworldPlacements.SPRING_LAVA);
        add(gen, GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN, TREES_DAPPLED_FOREST,
                BROWN_MUSHROOM_DAPPLED_FOREST, PATCH_RED_SHRUB, VegetationPlacements.PATCH_GRASS_FOREST);
        add(gen, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER);

        BiomeSpecialEffects effects = new BiomeSpecialEffects.Builder()
                .fogColor(0xCCD8E2)
                .skyColor(0x7CA3FF)
                .waterColor(0x375154)
                .waterFogColor(0x375154)
                .grassColorOverride(0xDF6827)    // orange grass
                .foliageColorOverride(0xE68E30)  // tint for vanilla leaves, vines etc.
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
                .build();

        context.register(DAPPLED_FOREST, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.6F)
                .downfall(0.6F)
                .specialEffects(effects)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .build());
    }

    @SafeVarargs
    private static void add(BiomeGenerationSettings.Builder gen, GenerationStep.Decoration step, ResourceKey<PlacedFeature>... features) {
        for (ResourceKey<PlacedFeature> feature : features) {
            gen.addFeature(step, feature);
        }
    }

    /** 26.3's dry foliage tint for this biome (used by leaf litter). */
    public static final int DRY_FOLIAGE_COLOR = 0x8C3A04;
    /** 26.3's default dry foliage tint for everywhere else. */
    public static final int DEFAULT_DRY_FOLIAGE_COLOR = 0x5C3C32;
    /** How the client recognizes a Dappled Forest biome from its colors (biomes don't know their own ID). */
    public static final int FOLIAGE_COLOR = 0xE68E30;

    private static ResourceLocation id(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(name));
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(name));
    }

    private ModDappledForest() {}
}
