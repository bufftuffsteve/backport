package com.buffsteve.autumnbackport.registry;

import java.util.List;
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
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.insertAfter(new ItemStack(Items.CHERRY_LOG), new ItemStack(ModItems.POPLAR_LOG.get()), BOTH);
        }
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
