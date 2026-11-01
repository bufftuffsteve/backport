package com.buffsteve.autumnbackport.neoforge.datagen;

import com.buffsteve.autumnbackport.registry.ModCompostAndFuel;
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
        // Same table the Fabric and Forge versions use (see ModCompostAndFuel).
        var compost = builder(NeoForgeDataMaps.COMPOSTABLES);
        ModCompostAndFuel.compostChances().forEach((item, chance) ->
                compost.add(item.get().asItem().builtInRegistryHolder(), new Compostable(chance), false));

        // Burn times from 26.3, in ticks (20 ticks = 1 second; 200 smelts one item).
        var fuel = builder(NeoForgeDataMaps.FURNACE_FUELS);
        ModCompostAndFuel.burnTimes().forEach((item, ticks) ->
                fuel.add(item.get().asItem().builtInRegistryHolder(), new FurnaceFuel(ticks), false));
    }
}
