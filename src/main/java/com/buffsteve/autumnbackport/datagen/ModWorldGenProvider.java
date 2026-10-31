package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.world.ModDappledForest;
import com.buffsteve.autumnbackport.world.ModTreeFeatures;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

/**
 * Writes world generation files: the poplar trees, the Dappled Forest biome and its features
 * (data/minecraft/worldgen/...).
 * Only the entries listed here are written, never vanilla's own.
 */
public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder ENTRIES = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> {
                ModTreeFeatures.bootstrap(context);
                ModDappledForest.bootstrapConfigured(context);
            })
            .add(Registries.PLACED_FEATURE, ModDappledForest::bootstrapPlaced)
            .add(Registries.NOISE, ModDappledForest::bootstrapNoise)
            .add(Registries.BIOME, ModDappledForest::bootstrapBiome);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, ENTRIES, Set.of(ModBlocks.VANILLA));
    }
}
