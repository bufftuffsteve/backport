package com.buffsteve.autumnbackport.world;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import com.buffsteve.autumnbackport.block.LeafLitterBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * The three poplar trees, one per leaf color, using 26.3's IDs (minecraft:red_poplar etc.)
 * and the exact numbers from 26.3's tree definitions.
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
    public static final TreeGrower RED = grower("autumnbackport_red_poplar", RED_POPLAR);
    public static final TreeGrower ORANGE = grower("autumnbackport_orange_poplar", ORANGE_POPLAR);
    public static final TreeGrower YELLOW = grower("autumnbackport_yellow_poplar", YELLOW_POPLAR);
    private static final List<TreeGrower> ALL_COLORS = List.of(RED, ORANGE, YELLOW);

    public static TreeGrower randomColor(RandomSource random) {
        return ALL_COLORS.get(random.nextInt(ALL_COLORS.size()));
    }

    /** Called by data generation to write the three tree definitions. */
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(RED_POPLAR, poplar(ModBlocks.RED_POPLAR_LEAVES.get()));
        context.register(ORANGE_POPLAR, poplar(ModBlocks.ORANGE_POPLAR_LEAVES.get()));
        context.register(YELLOW_POPLAR, poplar(ModBlocks.YELLOW_POPLAR_LEAVES.get()));
        context.register(RED_POPLAR_LEAF_LITTER, poplar(ModBlocks.RED_POPLAR_LEAVES.get(), leafLitter()));
        context.register(ORANGE_POPLAR_LEAF_LITTER, poplar(ModBlocks.ORANGE_POPLAR_LEAVES.get(), leafLitter()));
        context.register(YELLOW_POPLAR_LEAF_LITTER, poplar(ModBlocks.YELLOW_POPLAR_LEAVES.get(), leafLitter()));

        // Fallen poplar: a stump and a 2-5 block log on its side, with brown mushrooms (10% per log)
        // on top and shelf mushrooms (80%) on the sides.
        context.register(FALLEN_POPLAR_TREE, new ConfiguredFeature<>(ModTreePlacers.FALLEN_TREE.get(), new FallenTreeConfiguration(
                BlockStateProvider.simple(ModBlocks.POPLAR_LOG.get()),
                UniformInt.of(4, 7),
                List.of(),
                List.of(new AttachedToLogsDecorator(0.1F, BlockStateProvider.simple(Blocks.BROWN_MUSHROOM), List.of(Direction.UP)),
                        new ShelfMushroomDecorator(0.8F)))));
    }

    /**
     * 26.3's leaf litter: two passes around the trunk base. The first (96 tries, up to 4 blocks out)
     * places 1-3 pieces; the second (150 tries, up to 2 blocks out) places 1-4, so it's thickest near the trunk.
     */
    private static List<TreeDecorator> leafLitter() {
        return List.of(
                new PlaceOnGroundDecorator(96, 4, 2, litter(3)),
                new PlaceOnGroundDecorator(150, 2, 2, litter(4)));
    }

    /** Leaf litter in every direction with 1 to {@code maxPieces} pieces, all equally likely. */
    private static BlockStateProvider litter(int maxPieces) {
        SimpleWeightedRandomList.Builder<BlockState> states = SimpleWeightedRandomList.builder();
        for (int pieces = 1; pieces <= maxPieces; pieces++) {
            for (Direction facing : List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
                states.add(ModBlocks.LEAF_LITTER.get().defaultBlockState()
                        .setValue(LeafLitterBlock.FACING, facing).setValue(LeafLitterBlock.AMOUNT, pieces), 1);
            }
        }
        return new WeightedStateProvider(states);
    }

    private static ConfiguredFeature<?, ?> poplar(Block leaves) {
        return poplar(leaves, List.of());
    }

    private static ConfiguredFeature<?, ?> poplar(Block leaves, List<TreeDecorator> extraDecorators) {
        // Canopy width: usually 5 or 6, sometimes 7 or 8.
        IntProvider canopyRadius = new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                .add(ConstantInt.of(5), 5)
                .add(ConstantInt.of(6), 5)
                .add(ConstantInt.of(7), 1)
                .add(ConstantInt.of(8), 1)
                .build());
        TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.POPLAR_LOG.get()),
                // Trunk 7-11 tall; canopy centred 4 blocks below the top, with 1-4 side branches.
                new PoplarTrunkPlacer(7, 4, 0, ConstantInt.of(4), UniformInt.of(1, 4)),
                BlockStateProvider.simple(leaves),
                // Canopy 5-6 layers tall, 15% chance of a gap on each edge block.
                new PoplarFoliagePlacer(canopyRadius, ConstantInt.of(0), UniformInt.of(5, 6), 0.15F),
                new TwoLayersFeatureSize(1, 0, 2))
                .ignoreVines()
                // 40% of poplars get shelf mushrooms on the lower trunk, same as 26.3.
                .decorators(withShelfMushrooms(extraDecorators))
                .build();
        return new ConfiguredFeature<>(Feature.TREE, config);
    }

    private static List<TreeDecorator> withShelfMushrooms(List<TreeDecorator> extra) {
        List<TreeDecorator> all = new java.util.ArrayList<>(extra);
        all.add(new ShelfMushroomDecorator(0.4F));
        return all;
    }

    private static TreeGrower grower(String name, ResourceKey<ConfiguredFeature<?, ?>> tree) {
        return new TreeGrower(name, Optional.empty(), Optional.of(tree), Optional.empty());
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace(name));
    }

    private ModTreeFeatures() {}
}
