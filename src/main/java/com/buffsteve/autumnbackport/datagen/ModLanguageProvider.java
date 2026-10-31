package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.registry.ModItems;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** English names. Use the same names as vanilla 26.3. */
public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AutumnBackport.MODID, "en_us");
    }

    /** "light_blue" -> "Light Blue" */
    private static String displayName(DyeColor color) {
        return Arrays.stream(color.getName().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.POPLAR_LOG, "Poplar Log");
        addBlock(ModBlocks.POPLAR_WOOD, "Poplar Wood");
        addBlock(ModBlocks.STRIPPED_POPLAR_LOG, "Stripped Poplar Log");
        addBlock(ModBlocks.STRIPPED_POPLAR_WOOD, "Stripped Poplar Wood");
        addBlock(ModBlocks.POPLAR_PLANKS, "Poplar Planks");
        addBlock(ModBlocks.POPLAR_STAIRS, "Poplar Stairs");
        addBlock(ModBlocks.POPLAR_SLAB, "Poplar Slab");
        addBlock(ModBlocks.POPLAR_FENCE, "Poplar Fence");
        addBlock(ModBlocks.POPLAR_FENCE_GATE, "Poplar Fence Gate");
        addBlock(ModBlocks.POPLAR_DOOR, "Poplar Door");
        addBlock(ModBlocks.POPLAR_TRAPDOOR, "Poplar Trapdoor");
        addBlock(ModBlocks.POPLAR_PRESSURE_PLATE, "Poplar Pressure Plate");
        addBlock(ModBlocks.POPLAR_BUTTON, "Poplar Button");
        // Wall signs share the standing sign's name (same as vanilla), so they don't get their own line.
        addBlock(ModBlocks.POPLAR_SIGN, "Poplar Sign");
        addBlock(ModBlocks.POPLAR_HANGING_SIGN, "Poplar Hanging Sign");
        addItem(ModItems.POPLAR_BOAT, "Poplar Boat");
        addItem(ModItems.POPLAR_CHEST_BOAT, "Poplar Boat with Chest");
        addBlock(ModBlocks.RED_POPLAR_LEAVES, "Red Poplar Leaves");
        addBlock(ModBlocks.ORANGE_POPLAR_LEAVES, "Orange Poplar Leaves");
        addBlock(ModBlocks.YELLOW_POPLAR_LEAVES, "Yellow Poplar Leaves");
        addBlock(ModBlocks.POPLAR_SAPLING, "Poplar Sapling");
        addBlock(ModBlocks.POTTED_POPLAR_SAPLING, "Potted Poplar Sapling");
        addBlock(ModBlocks.SHELF_MUSHROOM, "Shelf Mushroom");
        addBlock(ModBlocks.RED_SHRUB, "Red Shrub");
        addBlock(ModBlocks.LEAF_LITTER, "Leaf Litter");
        add("biome.minecraft.dappled_forest", "Dappled Forest");
        // Treasure maps found in camp chests that lead to camps in other biomes (26.3 names)
        add("filled_map.bamboo_camp_map", "Bamboo Camp Map");
        add("filled_map.birch_forest_camp_map", "Birch Forest Camp Map");
        add("filled_map.cherry_grove_camp_map", "Cherry Grove Camp Map");
        add("filled_map.dappled_forest_camp_map", "Dappled Forest Camp Map");
        add("filled_map.flower_forest_camp_map", "Flower Forest Camp Map");
        add("filled_map.swamp_camp_map", "Swamp Camp Map");
        add("filled_map.windswept_forest_camp_map", "Windswept Forest Camp Map");
        addBlock(ModBlocks.STRAW_BED, "Straw Bed");
        add("stat.minecraft.sleep_in_straw_bed", "Times Slept in a Straw Bed");
        addEntityType(ModEntities.CUSHION, "Cushion");

        for (DyeColor color : DyeColor.values()) {
            String name = displayName(color);
            addBlock(ModBlocks.WOOL_STAIRS.get(color), name + " Wool Stairs");
            addBlock(ModBlocks.WOOL_SLABS.get(color), name + " Wool Slab");
            addBlock(ModBlocks.CONCRETE_STAIRS.get(color), name + " Concrete Stairs");
            addBlock(ModBlocks.CONCRETE_SLABS.get(color), name + " Concrete Slab");
            addItem(ModItems.CUSHIONS.get(color), name + " Cushion");
        }
    }
}
