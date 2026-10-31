package com.buffsteve.autumnbackport.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items, including the item form of every block. Same "minecraft" namespace as ModBlocks. */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModBlocks.VANILLA);

    public static final DeferredItem<BlockItem> POPLAR_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_LOG);
    public static final DeferredItem<BlockItem> POPLAR_WOOD = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_WOOD);
    public static final DeferredItem<BlockItem> STRIPPED_POPLAR_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.STRIPPED_POPLAR_LOG);
    public static final DeferredItem<BlockItem> STRIPPED_POPLAR_WOOD = ITEMS.registerSimpleBlockItem(ModBlocks.STRIPPED_POPLAR_WOOD);
    public static final DeferredItem<BlockItem> POPLAR_PLANKS = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_PLANKS);
    public static final DeferredItem<BlockItem> POPLAR_STAIRS = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_STAIRS);
    public static final DeferredItem<BlockItem> POPLAR_SLAB = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_SLAB);
    public static final DeferredItem<BlockItem> POPLAR_FENCE = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_FENCE);
    public static final DeferredItem<BlockItem> POPLAR_FENCE_GATE = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_FENCE_GATE);
    // Doors take up two blocks, so they use the two-block item that places both halves.
    public static final DeferredItem<DoubleHighBlockItem> POPLAR_DOOR = ITEMS.register("poplar_door",
            () -> new DoubleHighBlockItem(ModBlocks.POPLAR_DOOR.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POPLAR_TRAPDOOR = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_TRAPDOOR);
    public static final DeferredItem<BlockItem> POPLAR_PRESSURE_PLATE = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_PRESSURE_PLATE);
    public static final DeferredItem<BlockItem> POPLAR_BUTTON = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_BUTTON);

    private ModItems() {}
}
