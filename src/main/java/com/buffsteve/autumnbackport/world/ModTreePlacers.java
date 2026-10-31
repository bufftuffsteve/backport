package com.buffsteve.autumnbackport.world;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the poplar trunk and canopy shapes, and the shelf mushroom tree decoration, under 26.3's IDs. */
public final class ModTreePlacers {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ModBlocks.VANILLA);
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, ModBlocks.VANILLA);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, ModBlocks.VANILLA);

    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<PoplarTrunkPlacer>> POPLAR_TRUNK_PLACER =
            TRUNK_PLACERS.register("poplar_trunk_placer", () -> new TrunkPlacerType<>(PoplarTrunkPlacer.CODEC));
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<PoplarFoliagePlacer>> POPLAR_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("poplar_foliage_placer", () -> new FoliagePlacerType<>(PoplarFoliagePlacer.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<ShelfMushroomDecorator>> SHELF_MUSHROOM_DECORATOR =
            TREE_DECORATORS.register("shelf_mushroom", () -> new TreeDecoratorType<>(ShelfMushroomDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<PlaceOnGroundDecorator>> PLACE_ON_GROUND =
            TREE_DECORATORS.register("place_on_ground", () -> new TreeDecoratorType<>(PlaceOnGroundDecorator.CODEC));
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<AttachedToLogsDecorator>> ATTACHED_TO_LOGS =
            TREE_DECORATORS.register("attached_to_logs", () -> new TreeDecoratorType<>(AttachedToLogsDecorator.CODEC));

    // Worldgen feature types from 1.21.5+ that 26.3's Dappled Forest uses.
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, ModBlocks.VANILLA);
    public static final DeferredHolder<Feature<?>, FallenTreeFeature> FALLEN_TREE = FEATURES.register("fallen_tree", FallenTreeFeature::new);

    private ModTreePlacers() {}
}
