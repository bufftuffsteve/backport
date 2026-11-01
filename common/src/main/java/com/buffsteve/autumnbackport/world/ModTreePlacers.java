package com.buffsteve.autumnbackport.world;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import com.buffsteve.autumnbackport.registry.ModRegistry;
import com.buffsteve.autumnbackport.mixin.FoliagePlacerTypeInvoker;
import com.buffsteve.autumnbackport.mixin.TreeDecoratorTypeInvoker;
import com.buffsteve.autumnbackport.mixin.TrunkPlacerTypeInvoker;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

/** Registers the poplar trunk and canopy shapes, and the shelf mushroom tree decoration, under 26.3's IDs. */
public final class ModTreePlacers {

    public static final RegistryEntry<TrunkPlacerType<PoplarTrunkPlacer>> POPLAR_TRUNK_PLACER =
            ModRegistry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, "poplar_trunk_placer", () -> TrunkPlacerTypeInvoker.autumnbackport$create(PoplarTrunkPlacer.CODEC));
    public static final RegistryEntry<FoliagePlacerType<PoplarFoliagePlacer>> POPLAR_FOLIAGE_PLACER =
            ModRegistry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, "poplar_foliage_placer", () -> FoliagePlacerTypeInvoker.autumnbackport$create(PoplarFoliagePlacer.CODEC));

    public static final RegistryEntry<TreeDecoratorType<ShelfMushroomDecorator>> SHELF_MUSHROOM_DECORATOR =
            ModRegistry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, "shelf_mushroom", () -> TreeDecoratorTypeInvoker.autumnbackport$create(ShelfMushroomDecorator.CODEC));

    public static final RegistryEntry<TreeDecoratorType<PlaceOnGroundDecorator>> PLACE_ON_GROUND =
            ModRegistry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, "place_on_ground", () -> TreeDecoratorTypeInvoker.autumnbackport$create(PlaceOnGroundDecorator.CODEC));
    public static final RegistryEntry<TreeDecoratorType<AttachedToLogsDecorator>> ATTACHED_TO_LOGS =
            ModRegistry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, "attached_to_logs", () -> TreeDecoratorTypeInvoker.autumnbackport$create(AttachedToLogsDecorator.CODEC));

    // Worldgen feature types from 1.21.5+ that 26.3's Dappled Forest uses.
    public static final RegistryEntry<FallenTreeFeature> FALLEN_TREE = ModRegistry.register(BuiltInRegistries.FEATURE, "fallen_tree", FallenTreeFeature::new);

    private ModTreePlacers() {}
}
