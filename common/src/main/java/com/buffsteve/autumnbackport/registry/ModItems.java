package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.entity.CushionItem;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import com.buffsteve.autumnbackport.platform.Platform;
import com.buffsteve.autumnbackport.platform.ItemLikeEntry;

/** Items, including the item form of every block. Same "minecraft" namespace as ModBlocks. */
public final class ModItems {
    public static final ItemLikeEntry<BlockItem> POPLAR_LOG = ModRegistry.blockItem(ModBlocks.POPLAR_LOG);
    public static final ItemLikeEntry<BlockItem> POPLAR_WOOD = ModRegistry.blockItem(ModBlocks.POPLAR_WOOD);
    public static final ItemLikeEntry<BlockItem> STRIPPED_POPLAR_LOG = ModRegistry.blockItem(ModBlocks.STRIPPED_POPLAR_LOG);
    public static final ItemLikeEntry<BlockItem> STRIPPED_POPLAR_WOOD = ModRegistry.blockItem(ModBlocks.STRIPPED_POPLAR_WOOD);
    public static final ItemLikeEntry<BlockItem> POPLAR_PLANKS = ModRegistry.blockItem(ModBlocks.POPLAR_PLANKS);
    public static final ItemLikeEntry<BlockItem> POPLAR_STAIRS = ModRegistry.blockItem(ModBlocks.POPLAR_STAIRS);
    public static final ItemLikeEntry<BlockItem> POPLAR_SLAB = ModRegistry.blockItem(ModBlocks.POPLAR_SLAB);
    public static final ItemLikeEntry<BlockItem> POPLAR_FENCE = ModRegistry.blockItem(ModBlocks.POPLAR_FENCE);
    public static final ItemLikeEntry<BlockItem> POPLAR_FENCE_GATE = ModRegistry.blockItem(ModBlocks.POPLAR_FENCE_GATE);
    // Doors take up two blocks, so they use the two-block item that places both halves.
    public static final ItemLikeEntry<DoubleHighBlockItem> POPLAR_DOOR = ModRegistry.item("poplar_door",
            () -> new DoubleHighBlockItem(ModBlocks.POPLAR_DOOR.get(), new Item.Properties()));
    public static final ItemLikeEntry<BlockItem> POPLAR_TRAPDOOR = ModRegistry.blockItem(ModBlocks.POPLAR_TRAPDOOR);
    public static final ItemLikeEntry<BlockItem> POPLAR_PRESSURE_PLATE = ModRegistry.blockItem(ModBlocks.POPLAR_PRESSURE_PLATE);
    public static final ItemLikeEntry<BlockItem> POPLAR_BUTTON = ModRegistry.blockItem(ModBlocks.POPLAR_BUTTON);

    // Signs stack to 16. One item places either the standing or the wall version.
    public static final ItemLikeEntry<SignItem> POPLAR_SIGN = ModRegistry.item("poplar_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get()));
    public static final ItemLikeEntry<HangingSignItem> POPLAR_HANGING_SIGN = ModRegistry.item("poplar_hanging_sign",
            () -> new HangingSignItem(ModBlocks.POPLAR_HANGING_SIGN.get(), ModBlocks.POPLAR_WALL_HANGING_SIGN.get(),
                    new Item.Properties().stacksTo(16)));

    // Boats. Boat types are a fixed list on 1.21.1, so each loader adds poplar its own way.
    public static final ItemLikeEntry<BoatItem> POPLAR_BOAT = ModRegistry.item("poplar_boat",
            () -> new BoatItem(false, Platform.INSTANCE.poplarBoatType(), new Item.Properties().stacksTo(1)));
    public static final ItemLikeEntry<BoatItem> POPLAR_CHEST_BOAT = ModRegistry.item("poplar_chest_boat",
            () -> new BoatItem(true, Platform.INSTANCE.poplarBoatType(), new Item.Properties().stacksTo(1)));

    public static final ItemLikeEntry<BlockItem> RED_POPLAR_LEAVES = ModRegistry.blockItem(ModBlocks.RED_POPLAR_LEAVES);
    public static final ItemLikeEntry<BlockItem> ORANGE_POPLAR_LEAVES = ModRegistry.blockItem(ModBlocks.ORANGE_POPLAR_LEAVES);
    public static final ItemLikeEntry<BlockItem> YELLOW_POPLAR_LEAVES = ModRegistry.blockItem(ModBlocks.YELLOW_POPLAR_LEAVES);
    public static final ItemLikeEntry<BlockItem> POPLAR_SAPLING = ModRegistry.blockItem(ModBlocks.POPLAR_SAPLING);

    public static final ItemLikeEntry<BlockItem> SHELF_MUSHROOM = ModRegistry.blockItem(ModBlocks.SHELF_MUSHROOM);
    public static final ItemLikeEntry<BlockItem> RED_SHRUB = ModRegistry.blockItem(ModBlocks.RED_SHRUB);
    public static final ItemLikeEntry<BlockItem> LEAF_LITTER = ModRegistry.blockItem(ModBlocks.LEAF_LITTER);

    // Bed item: places both halves. Stacks to 16 like 26.3 (normal beds stack to 1).
    public static final ItemLikeEntry<BedItem> STRAW_BED = ModRegistry.item("straw_bed",
            () -> new BedItem(ModBlocks.STRAW_BED.get(), new Item.Properties().stacksTo(16)));

    /** One cushion item per color, e.g. minecraft:red_cushion. Stacks to 16. */
    public static final Map<DyeColor, ItemLikeEntry<CushionItem>> CUSHIONS = new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, ItemLikeEntry<BlockItem>> WOOL_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, ItemLikeEntry<BlockItem>> WOOL_SLABS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, ItemLikeEntry<BlockItem>> CONCRETE_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, ItemLikeEntry<BlockItem>> CONCRETE_SLABS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            WOOL_STAIRS.put(color, ModRegistry.blockItem(ModBlocks.WOOL_STAIRS.get(color)));
            WOOL_SLABS.put(color, ModRegistry.blockItem(ModBlocks.WOOL_SLABS.get(color)));
            CONCRETE_STAIRS.put(color, ModRegistry.blockItem(ModBlocks.CONCRETE_STAIRS.get(color)));
            CONCRETE_SLABS.put(color, ModRegistry.blockItem(ModBlocks.CONCRETE_SLABS.get(color)));
            CUSHIONS.put(color, ModRegistry.item(color.getName() + "_cushion",
                    () -> new CushionItem(color, new Item.Properties().stacksTo(16))));
        }
    }

    private ModItems() {}
}
