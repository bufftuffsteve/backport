package com.buffsteve.autumnbackport.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import com.buffsteve.autumnbackport.platform.ItemLikeEntry;

/**
 * Where the new items go in the vanilla creative tabs, next to the matching vanilla items.
 * Each loader applies these with its own tab API.
 */
public final class ModCreativeTabs {
    /** Put {@code items} right after {@code anchor}, one after another, in this order. */
    public record Insertion(ItemLike anchor, List<? extends Supplier<? extends ItemLike>> items) {}

    // Vanilla keeps these tab IDs private, so they're rebuilt here.
    private static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = tab("building_blocks");
    private static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = tab("colored_blocks");
    private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = tab("functional_blocks");
    private static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = tab("tools_and_utilities");
    private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS = tab("natural_blocks");

    private static ResourceKey<CreativeModeTab> tab(String name) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(name));
    }

    /** Every tab this mod adds to. */
    public static final List<ResourceKey<CreativeModeTab>> TABS = List.of(
            BUILDING_BLOCKS, COLORED_BLOCKS, FUNCTIONAL_BLOCKS,
            TOOLS_AND_UTILITIES, NATURAL_BLOCKS);

    public static List<Insertion> insertionsFor(ResourceKey<CreativeModeTab> tab) {
        List<Insertion> list = new ArrayList<>();
        if (tab.equals(BUILDING_BLOCKS)) {
            // Poplar set goes after cherry (the last overworld wood set on 1.21.1),
            // in vanilla's order. Add new poplar blocks to this list in that order.
            list.add(new Insertion(Items.CHERRY_BUTTON, List.of(
                    ModItems.POPLAR_LOG,
                    ModItems.POPLAR_WOOD,
                    ModItems.STRIPPED_POPLAR_LOG,
                    ModItems.STRIPPED_POPLAR_WOOD,
                    ModItems.POPLAR_PLANKS,
                    ModItems.POPLAR_STAIRS,
                    ModItems.POPLAR_SLAB,
                    ModItems.POPLAR_FENCE,
                    ModItems.POPLAR_FENCE_GATE,
                    ModItems.POPLAR_DOOR,
                    ModItems.POPLAR_TRAPDOOR,
                    ModItems.POPLAR_PRESSURE_PLATE,
                    ModItems.POPLAR_BUTTON)));
        }
        if (tab.equals(COLORED_BLOCKS)) {
            // Right after the last full block of each kind (pink is last in vanilla's order).
            list.add(new Insertion(Items.PINK_WOOL, stairsThenSlabs(ModItems.WOOL_STAIRS, ModItems.WOOL_SLABS)));
            list.add(new Insertion(Items.PINK_CONCRETE, stairsThenSlabs(ModItems.CONCRETE_STAIRS, ModItems.CONCRETE_SLABS)));
            // Cushions right after the beds, as in 26.3.
            list.add(new Insertion(Items.PINK_BED, CREATIVE_COLOR_ORDER.stream().map(ModItems.CUSHIONS::get).toList()));
        }
        if (tab.equals(FUNCTIONAL_BLOCKS)) {
            // After cherry, like the rest of the poplar set.
            list.add(new Insertion(Items.CHERRY_HANGING_SIGN, List.of(ModItems.POPLAR_SIGN, ModItems.POPLAR_HANGING_SIGN)));
            // After the last colored bed.
            list.add(new Insertion(Items.PINK_BED, List.of(ModItems.STRAW_BED)));
        }
        if (tab.equals(TOOLS_AND_UTILITIES)) {
            list.add(new Insertion(Items.CHERRY_CHEST_BOAT, List.of(ModItems.POPLAR_BOAT, ModItems.POPLAR_CHEST_BOAT)));
        }
        if (tab.equals(NATURAL_BLOCKS)) {
            list.add(new Insertion(Items.CHERRY_LOG, List.of(ModItems.POPLAR_LOG)));
            list.add(new Insertion(Items.CHERRY_LEAVES, List.of(
                    ModItems.RED_POPLAR_LEAVES,
                    ModItems.ORANGE_POPLAR_LEAVES,
                    ModItems.YELLOW_POPLAR_LEAVES)));
            list.add(new Insertion(Items.CHERRY_SAPLING, List.of(ModItems.POPLAR_SAPLING)));
            list.add(new Insertion(Items.RED_MUSHROOM, List.of(ModItems.SHELF_MUSHROOM)));
            // 26.3 puts it between bush and dead bush; 1.21.1 has no bush, so it follows fern.
            list.add(new Insertion(Items.FERN, List.of(ModItems.RED_SHRUB)));
            // 1.21.5 puts leaf litter after pink petals and wildflowers.
            list.add(new Insertion(Items.PINK_PETALS, List.of(ModItems.LEAF_LITTER)));
        }
        return list;
    }

    /** Vanilla's creative-menu color order (white, light gray, gray, black, brown, red, ...). */
    private static final List<DyeColor> CREATIVE_COLOR_ORDER = List.of(
            DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN, DyeColor.RED,
            DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
            DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK);

    /** All stairs in color order, then all slabs in color order. */
    private static List<ItemLikeEntry<BlockItem>> stairsThenSlabs(Map<DyeColor, ItemLikeEntry<BlockItem>> stairs,
                                                                 Map<DyeColor, ItemLikeEntry<BlockItem>> slabs) {
        List<ItemLikeEntry<BlockItem>> items = new ArrayList<>();
        CREATIVE_COLOR_ORDER.forEach(color -> items.add(stairs.get(color)));
        CREATIVE_COLOR_ORDER.forEach(color -> items.add(slabs.get(color)));
        return items;
    }

    private ModCreativeTabs() {}
}
