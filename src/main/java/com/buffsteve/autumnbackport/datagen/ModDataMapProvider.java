package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.registry.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

/**
 * NeoForge "data maps": per-item values like compost chance.
 * Furnace fuel for wood comes from tags instead (#planks, #logs, #saplings).
 */
public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        // 30% chance to add a compost layer, same as other leaves and saplings.
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(ModItems.RED_POPLAR_LEAVES, new Compostable(0.3F), false)
                .add(ModItems.ORANGE_POPLAR_LEAVES, new Compostable(0.3F), false)
                .add(ModItems.YELLOW_POPLAR_LEAVES, new Compostable(0.3F), false)
                .add(ModItems.POPLAR_SAPLING, new Compostable(0.3F), false)
                .add(ModItems.RED_SHRUB, new Compostable(0.3F), false)
                .add(ModItems.LEAF_LITTER, new Compostable(0.3F), false)
                // 65%, same as the other mushrooms.
                .add(ModItems.SHELF_MUSHROOM, new Compostable(0.65F), false)
                // 85%, same as a hay bale.
                .add(ModItems.STRAW_BED, new Compostable(0.85F), false);

        // Burn times from 26.3, in ticks (20 ticks = 1 second; 200 smelts one item).
        var fuel = builder(NeoForgeDataMaps.FURNACE_FUELS);
        ModItems.WOOL_STAIRS.values().forEach(item -> fuel.add(item, new FurnaceFuel(100), false)); // same as a wool block
        ModItems.WOOL_SLABS.values().forEach(item -> fuel.add(item, new FurnaceFuel(50), false));
        fuel.add(ModItems.LEAF_LITTER, new FurnaceFuel(100), false);
        ModItems.CUSHIONS.values().forEach(item -> fuel.add(item, new FurnaceFuel(50), false));
    }
}
