package com.buffsteve.autumnbackport.registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

/**
 * Composter chances and furnace burn times, from 26.3. The table lives here so every loader
 * uses the same numbers (NeoForge writes them out as data maps during data generation).
 */
public final class ModCompostAndFuel {
    /** Chance (0-1) that composting one item raises the composter's level. */
    public static Map<Supplier<? extends ItemLike>, Float> compostChances() {
        Map<Supplier<? extends ItemLike>, Float> map = new LinkedHashMap<>();
        map.put(ModItems.RED_POPLAR_LEAVES, 0.3F);
        map.put(ModItems.ORANGE_POPLAR_LEAVES, 0.3F);
        map.put(ModItems.YELLOW_POPLAR_LEAVES, 0.3F);
        map.put(ModItems.POPLAR_SAPLING, 0.3F);
        map.put(ModItems.RED_SHRUB, 0.3F);
        map.put(ModItems.LEAF_LITTER, 0.3F);
        map.put(ModItems.SHELF_MUSHROOM, 0.65F);
        map.put(ModItems.STRAW_BED, 0.85F);
        return map;
    }

    /** Ticks of smelting one item gives as fuel (200 = one item smelted). */
    public static Map<Supplier<? extends ItemLike>, Integer> burnTimes() {
        Map<Supplier<? extends ItemLike>, Integer> map = new LinkedHashMap<>();
        for (DyeColor color : DyeColor.values()) {
            map.put(ModItems.WOOL_STAIRS.get(color), 100);
            map.put(ModItems.WOOL_SLABS.get(color), 50);
            map.put(ModItems.CUSHIONS.get(color), 50);
        }
        map.put(ModItems.LEAF_LITTER, 100);
        return map;
    }

    /** Adds the compost chances to vanilla's composter table (used by Fabric and Forge). */
    public static void registerCompostables() {
        compostChances().forEach((item, chance) -> ComposterBlock.COMPOSTABLES.put(item.get().asItem(), chance.floatValue()));
    }

    private ModCompostAndFuel() {}
}
