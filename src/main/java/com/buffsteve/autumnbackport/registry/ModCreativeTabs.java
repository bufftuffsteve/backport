package com.buffsteve.autumnbackport.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.world.item.BlockItem;
import java.util.function.Supplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/** Slots the new items into the vanilla creative tabs, next to the matching vanilla items. */
public final class ModCreativeTabs {
    private static final CreativeModeTab.TabVisibility BOTH = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

    public static void addToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // Poplar set goes after cherry (the last overworld wood set on 1.21.1),
            // in vanilla's order. Add new poplar blocks to this list in that order.
            insertChain(event, Items.CHERRY_BUTTON, List.of(
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
                    ModItems.POPLAR_BUTTON));
        }
        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
            // Right after the last full block of each kind (pink is last in vanilla's order).
            insertChain(event, Items.PINK_WOOL, stairsThenSlabs(ModItems.WOOL_STAIRS, ModItems.WOOL_SLABS));
            insertChain(event, Items.PINK_CONCRETE, stairsThenSlabs(ModItems.CONCRETE_STAIRS, ModItems.CONCRETE_SLABS));
            // Cushions right after the beds, as in 26.3.
            insertChain(event, Items.PINK_BED, CREATIVE_COLOR_ORDER.stream().map(ModItems.CUSHIONS::get).toList());
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            // After cherry, like the rest of the poplar set.
            insertChain(event, Items.CHERRY_HANGING_SIGN, List.of(ModItems.POPLAR_SIGN, ModItems.POPLAR_HANGING_SIGN));
            // After the last colored bed.
            event.insertAfter(new ItemStack(Items.PINK_BED), new ItemStack(ModItems.STRAW_BED.get()), BOTH);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            insertChain(event, Items.CHERRY_CHEST_BOAT, List.of(ModItems.POPLAR_BOAT, ModItems.POPLAR_CHEST_BOAT));
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.insertAfter(new ItemStack(Items.CHERRY_LOG), new ItemStack(ModItems.POPLAR_LOG.get()), BOTH);
            insertChain(event, Items.CHERRY_LEAVES, List.of(
                    ModItems.RED_POPLAR_LEAVES,
                    ModItems.ORANGE_POPLAR_LEAVES,
                    ModItems.YELLOW_POPLAR_LEAVES));
            event.insertAfter(new ItemStack(Items.CHERRY_SAPLING), new ItemStack(ModItems.POPLAR_SAPLING.get()), BOTH);
            event.insertAfter(new ItemStack(Items.RED_MUSHROOM), new ItemStack(ModItems.SHELF_MUSHROOM.get()), BOTH);
            // 26.3 puts it between bush and dead bush; 1.21.1 has no bush, so it follows fern.
            event.insertAfter(new ItemStack(Items.FERN), new ItemStack(ModItems.RED_SHRUB.get()), BOTH);
            // 1.21.5 puts leaf litter after pink petals and wildflowers.
            event.insertAfter(new ItemStack(Items.PINK_PETALS), new ItemStack(ModItems.LEAF_LITTER.get()), BOTH);
        }
    }

    /** Vanilla's creative-menu color order (white, light gray, gray, black, brown, red, ...). */
    private static final List<DyeColor> CREATIVE_COLOR_ORDER = List.of(
            DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN, DyeColor.RED,
            DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
            DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK);

    /** All stairs in color order, then all slabs in color order. */
    private static List<DeferredItem<BlockItem>> stairsThenSlabs(Map<DyeColor, DeferredItem<BlockItem>> stairs,
                                                                Map<DyeColor, DeferredItem<BlockItem>> slabs) {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        CREATIVE_COLOR_ORDER.forEach(color -> items.add(stairs.get(color)));
        CREATIVE_COLOR_ORDER.forEach(color -> items.add(slabs.get(color)));
        return items;
    }

    /** Inserts each item right after the previous one, starting after {@code anchor}. */
    private static void insertChain(BuildCreativeModeTabContentsEvent event, ItemLike anchor, List<? extends Supplier<? extends ItemLike>> items) {
        ItemStack previous = new ItemStack(anchor);
        for (Supplier<? extends ItemLike> item : items) {
            ItemStack stack = new ItemStack(item.get());
            event.insertAfter(previous, stack, BOTH);
            previous = stack;
        }
    }

    private ModCreativeTabs() {}
}
