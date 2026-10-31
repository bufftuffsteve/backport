package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModItems;
import com.buffsteve.autumnbackport.registry.ModTags;
import java.util.concurrent.CompletableFuture;
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
     * Signs get added here later.
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
    }
}
