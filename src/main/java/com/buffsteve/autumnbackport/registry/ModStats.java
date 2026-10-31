package com.buffsteve.autumnbackport.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** New statistics from 26.3, shown on the Statistics screen. */
public final class ModStats {
    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, ModBlocks.VANILLA);

    /** "Times Slept in a Straw Bed" */
    public static final DeferredHolder<ResourceLocation, ResourceLocation> SLEEP_IN_STRAW_BED =
            STATS.register("sleep_in_straw_bed", () -> ResourceLocation.withDefaultNamespace("sleep_in_straw_bed"));

    /** Vanilla also needs each custom stat added to the stats list once. Call during common setup. */
    public static void register() {
        Stats.CUSTOM.get(SLEEP_IN_STRAW_BED.get(), StatFormatter.DEFAULT);
    }

    private ModStats() {}
}
