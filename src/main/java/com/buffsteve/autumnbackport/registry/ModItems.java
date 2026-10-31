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

    // Signs stack to 16. One item places either the standing or the wall version.
    public static final DeferredItem<SignItem> POPLAR_SIGN = ITEMS.register("poplar_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get()));
    public static final DeferredItem<HangingSignItem> POPLAR_HANGING_SIGN = ITEMS.register("poplar_hanging_sign",
            () -> new HangingSignItem(ModBlocks.POPLAR_HANGING_SIGN.get(), ModBlocks.POPLAR_WALL_HANGING_SIGN.get(),
                    new Item.Properties().stacksTo(16)));

    // Boats (the boat type itself is in ModBoats).
    public static final DeferredItem<BoatItem> POPLAR_BOAT = ITEMS.register("poplar_boat",
            () -> new BoatItem(false, ModBoats.POPLAR.getValue(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BoatItem> POPLAR_CHEST_BOAT = ITEMS.register("poplar_chest_boat",
            () -> new BoatItem(true, ModBoats.POPLAR.getValue(), new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BlockItem> RED_POPLAR_LEAVES = ITEMS.registerSimpleBlockItem(ModBlocks.RED_POPLAR_LEAVES);
    public static final DeferredItem<BlockItem> ORANGE_POPLAR_LEAVES = ITEMS.registerSimpleBlockItem(ModBlocks.ORANGE_POPLAR_LEAVES);
    public static final DeferredItem<BlockItem> YELLOW_POPLAR_LEAVES = ITEMS.registerSimpleBlockItem(ModBlocks.YELLOW_POPLAR_LEAVES);
    public static final DeferredItem<BlockItem> POPLAR_SAPLING = ITEMS.registerSimpleBlockItem(ModBlocks.POPLAR_SAPLING);

    public static final DeferredItem<BlockItem> SHELF_MUSHROOM = ITEMS.registerSimpleBlockItem(ModBlocks.SHELF_MUSHROOM);
    public static final DeferredItem<BlockItem> RED_SHRUB = ITEMS.registerSimpleBlockItem(ModBlocks.RED_SHRUB);
    public static final DeferredItem<BlockItem> LEAF_LITTER = ITEMS.registerSimpleBlockItem(ModBlocks.LEAF_LITTER);

    // Bed item: places both halves. Stacks to 16 like 26.3 (normal beds stack to 1).
    public static final DeferredItem<BedItem> STRAW_BED = ITEMS.register("straw_bed",
            () -> new BedItem(ModBlocks.STRAW_BED.get(), new Item.Properties().stacksTo(16)));

    /** One cushion item per color, e.g. minecraft:red_cushion. Stacks to 16. */
    public static final Map<DyeColor, DeferredItem<CushionItem>> CUSHIONS = new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, DeferredItem<BlockItem>> WOOL_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredItem<BlockItem>> WOOL_SLABS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredItem<BlockItem>> CONCRETE_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredItem<BlockItem>> CONCRETE_SLABS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            WOOL_STAIRS.put(color, ITEMS.registerSimpleBlockItem(ModBlocks.WOOL_STAIRS.get(color)));
            WOOL_SLABS.put(color, ITEMS.registerSimpleBlockItem(ModBlocks.WOOL_SLABS.get(color)));
            CONCRETE_STAIRS.put(color, ITEMS.registerSimpleBlockItem(ModBlocks.CONCRETE_STAIRS.get(color)));
            CONCRETE_SLABS.put(color, ITEMS.registerSimpleBlockItem(ModBlocks.CONCRETE_SLABS.get(color)));
            CUSHIONS.put(color, ITEMS.register(color.getName() + "_cushion",
                    () -> new CushionItem(color, new Item.Properties().stacksTo(16))));
        }
    }

    private ModItems() {}
}
