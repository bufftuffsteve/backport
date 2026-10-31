package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModItems;
import com.buffsteve.autumnbackport.registry.ModTags;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.flag.FeatureFlags;

/**
 * Crafting recipes. Each recipe ID is the result's ID (e.g. minecraft:poplar_planks),
 * the same as vanilla 26.3.
 */
public class ModRecipeProvider extends RecipeProvider {
    /**
     * The poplar "block family": vanilla's own description of a wood set.
     * Vanilla generates every standard recipe from it (stairs, slab, fence, gate, door,
     * trapdoor, button, pressure plate) with the same patterns and recipe-book groups as oak.
     */
    private static BlockFamily poplarFamily() {
        return new BlockFamily.Builder(ModBlocks.POPLAR_PLANKS.get())
                .stairs(ModBlocks.POPLAR_STAIRS.get())
                .slab(ModBlocks.POPLAR_SLAB.get())
                .fence(ModBlocks.POPLAR_FENCE.get())
                .fenceGate(ModBlocks.POPLAR_FENCE_GATE.get())
                .door(ModBlocks.POPLAR_DOOR.get())
                .trapdoor(ModBlocks.POPLAR_TRAPDOOR.get())
                .pressurePlate(ModBlocks.POPLAR_PRESSURE_PLATE.get())
                .button(ModBlocks.POPLAR_BUTTON.get())
                .sign(ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get())
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .getFamily();
    }

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Any poplar log or wood (stripped or not) -> 4 planks.
        planksFromLog(output, ModItems.POPLAR_PLANKS, ModTags.Items.POPLAR_LOGS, 4);
        // 4 logs in a square -> 3 wood.
        woodFromLogs(output, ModItems.POPLAR_WOOD, ModItems.POPLAR_LOG);
        woodFromLogs(output, ModItems.STRIPPED_POPLAR_WOOD, ModItems.STRIPPED_POPLAR_LOG);
        // Stairs, slab, fence, gate, door, trapdoor, button, pressure plate.
        generateRecipes(output, poplarFamily(), FeatureFlags.VANILLA_SET);
        // 2 chains over 6 stripped poplar logs -> 6 hanging signs.
        hangingSign(output, ModItems.POPLAR_HANGING_SIGN.get(), ModItems.STRIPPED_POPLAR_LOG.get());
        // Boat: 5 planks in a U. Chest boat: boat + chest.
        woodenBoat(output, ModItems.POPLAR_BOAT.get(), ModItems.POPLAR_PLANKS.get());
        chestBoat(output, ModItems.POPLAR_CHEST_BOAT.get(), ModItems.POPLAR_BOAT.get());

        // Wool and concrete stairs and slabs, with 26.3's recipe IDs and recipe-book groups.
        for (DyeColor color : DyeColor.values()) {
            ItemLike wool = ModBlocks.vanillaBlock(color.getName() + "_wool");
            ItemLike concrete = ModBlocks.vanillaBlock(color.getName() + "_concrete");
            ItemLike woolStairs = ModItems.WOOL_STAIRS.get(color);
            ItemLike woolSlab = ModItems.WOOL_SLABS.get(color);
            ItemLike concreteStairs = ModItems.CONCRETE_STAIRS.get(color);
            ItemLike concreteSlab = ModItems.CONCRETE_SLABS.get(color);

            stairs(output, woolStairs, wool, "woolen_stairs");
            slab(output, woolSlab, wool, "woolen_slab");
            stairs(output, concreteStairs, concrete, "concrete_stairs");
            slab(output, concreteSlab, concrete, "concrete_slab");

            // Concrete also works in the stonecutter: 1 block -> 1 stair or 2 slabs. (Wool doesn't.)
            stonecut(output, concreteStairs, concrete, 1);
            stonecut(output, concreteSlab, concrete, 2);

            // Re-dye: any dye + any wool stair or slab of another color -> that color.
            redye(output, color, ModItems.WOOL_STAIRS, "wool_stairs");
            redye(output, color, ModItems.WOOL_SLABS, "wool_slabs");

            // Cushion: 3 wool slabs of a color in a row. Any cushion + a dye recolors it.
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CUSHIONS.get(color).get())
                    .define('#', ModItems.WOOL_SLABS.get(color).get())
                    .pattern("###")
                    .group("cushion")
                    .unlockedBy("has_wool_slab", has(ModItems.WOOL_SLABS.get(color).get()))
                    .save(output);
            redye(output, color, ModItems.CUSHIONS, "cushion_dye", RecipeCategory.DECORATIONS);
        }

        // Smelt any leaves into leaf litter (1.21.5 recipe).
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemTags.LEAVES), RecipeCategory.MISC, ModItems.LEAF_LITTER.get(), 0.1F, 200)
                .unlockedBy("has_leaves", has(ItemTags.LEAVES))
                .save(output);

        // 3 hay bales in a row -> 4 straw beds.
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.STRAW_BED.get(), 4)
                .define('X', Items.HAY_BLOCK)
                .pattern("XXX")
                .unlockedBy("has_hay_block", has(Items.HAY_BLOCK))
                .save(output);

        // ---- Stews: any two mushrooms from #mushrooms, so shelf mushrooms work too (26.3 rules) ----
        // These use vanilla's recipe IDs, so they replace vanilla's brown + red only versions.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW)
                .requires(ModTags.Items.MUSHROOMS).requires(ModTags.Items.MUSHROOMS).requires(Items.BOWL)
                .group("mushroom_stew")
                .unlockedBy("has_bowl", has(Items.BOWL))
                .save(output); // ID minecraft:mushroom_stew, replacing vanilla's
        // One suspicious stew per flower, same effects as vanilla.
        for (SuspiciousEffectHolder flower : SuspiciousEffectHolder.getAllEffectHolders()) {
            ItemLike flowerItem = (ItemLike) flower; // every effect holder is a flower block
            ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
            stew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, flower.getSuspiciousEffects());
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, stew)
                    .requires(Items.BOWL).requires(ModTags.Items.MUSHROOMS).requires(ModTags.Items.MUSHROOMS)
                    .requires(flowerItem)
                    .group("suspicious_stew")
                    .unlockedBy(getHasName(flowerItem), has(flowerItem))
                    .save(output, "suspicious_stew_from_" + getItemName(flowerItem));
        }
        // Rabbit stew with a shelf mushroom (vanilla's brown and red versions stay as they are).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.RABBIT_STEW)
                .requires(Items.BAKED_POTATO).requires(Items.COOKED_RABBIT).requires(Items.BOWL)
                .requires(Items.CARROT).requires(ModItems.SHELF_MUSHROOM.get())
                .group("rabbit_stew")
                .unlockedBy("has_cooked_rabbit", has(Items.COOKED_RABBIT))
                .save(output, "rabbit_stew_from_shelf_mushroom");
    }

    /** 6 blocks in a staircase -> 4 stairs. */
    private static void stairs(RecipeOutput output, ItemLike result, ItemLike material, String group) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4)
                .define('#', material)
                .pattern("#  ").pattern("## ").pattern("###")
                .group(group)
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    /** 3 blocks in a row -> 6 slabs. */
    private static void slab(RecipeOutput output, ItemLike result, ItemLike material, String group) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6)
                .define('#', material)
                .pattern("###")
                .group(group)
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    /** Recipe ID like vanilla: minecraft:white_concrete_slab_from_white_concrete_stonecutting */
    private static void stonecut(RecipeOutput output, ItemLike result, ItemLike material, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), RecipeCategory.BUILDING_BLOCKS, result, count)
                .unlockedBy(getHasName(material), has(material))
                .save(output, getItemName(result) + "_from_" + getItemName(material) + "_stonecutting");
    }

    /** Recipe ID like vanilla: minecraft:dye_red_wool_slab */
    private static void redye(RecipeOutput output, DyeColor color, Map<DyeColor, DeferredItem<BlockItem>> items, String group) {
        redye(output, color, items, group, RecipeCategory.BUILDING_BLOCKS);
    }

    private static void redye(RecipeOutput output, DyeColor color, Map<DyeColor, ? extends DeferredItem<?>> items, String group,
                              RecipeCategory category) {
        ItemLike result = items.get(color);
        ItemLike dye = DyeItem.byColor(color);
        ItemLike[] otherColors = Arrays.stream(DyeColor.values())
                .filter(other -> other != color)
                .map(items::get)
                .toArray(ItemLike[]::new);
        ShapelessRecipeBuilder.shapeless(category, result)
                .requires(dye)
                .requires(Ingredient.of(otherColors))
                .group(group)
                .unlockedBy("has_needed_dye", has(dye))
                .save(output, "dye_" + getItemName(result));
    }
}
