package com.buffsteve.autumnbackport.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;

/** New statistics from 26.3, shown on the Statistics screen. */
public final class ModStats {
    /** "Times Slept in a Straw Bed" */
    public static final RegistryEntry<ResourceLocation> SLEEP_IN_STRAW_BED =
            ModRegistry.register(BuiltInRegistries.CUSTOM_STAT, "sleep_in_straw_bed", () -> new ResourceLocation("sleep_in_straw_bed"));

    /** Vanilla also needs each custom stat added to the stats list once. Call during common setup. */
    public static void register() {
        Stats.CUSTOM.get(SLEEP_IN_STRAW_BED.get(), StatFormatter.DEFAULT);
    }

    private ModStats() {}
}
