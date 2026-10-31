package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Blockstates plus block and item models.
 * Uses the "minecraft" namespace so models land next to the blockstates
 * (assets/minecraft/...) and point at textures in assets/minecraft/textures/.
 * Model shapes match vanilla 26.3's own poplar models.
 */
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper files) {
        super(output, ModBlocks.VANILLA, files);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation planks = blockTexture(ModBlocks.POPLAR_PLANKS.get());

        // Planks: same texture on every side.
        simpleBlockWithItem(ModBlocks.POPLAR_PLANKS.get(), cubeAll(ModBlocks.POPLAR_PLANKS.get()));

        // Logs: bark on the sides, rings on the ends (<name>_top.png). Rotates with placement.
        logWithItem(ModBlocks.POPLAR_LOG.get());
        logWithItem(ModBlocks.STRIPPED_POPLAR_LOG.get());

        // Wood: the log's side texture on all six faces.
        woodWithItem(ModBlocks.POPLAR_WOOD.get(), blockTexture(ModBlocks.POPLAR_LOG.get()));
        woodWithItem(ModBlocks.STRIPPED_POPLAR_WOOD.get(), blockTexture(ModBlocks.STRIPPED_POPLAR_LOG.get()));

        // Stairs and slab use the planks texture. A double slab looks like a full planks block.
        stairsBlock(ModBlocks.POPLAR_STAIRS.get(), planks);
        simpleBlockItem(ModBlocks.POPLAR_STAIRS.get(), blockModel("poplar_stairs"));
        slabBlock(ModBlocks.POPLAR_SLAB.get(), planks, planks);
        simpleBlockItem(ModBlocks.POPLAR_SLAB.get(), blockModel("poplar_slab"));

        // Fences and buttons have a separate model for how they look in the inventory.
        fenceBlock(ModBlocks.POPLAR_FENCE.get(), planks);
        simpleBlockItem(ModBlocks.POPLAR_FENCE.get(), models().fenceInventory("poplar_fence_inventory", planks));
        fenceGateBlock(ModBlocks.POPLAR_FENCE_GATE.get(), planks);
        simpleBlockItem(ModBlocks.POPLAR_FENCE_GATE.get(), blockModel("poplar_fence_gate"));
        buttonBlock(ModBlocks.POPLAR_BUTTON.get(), planks);
        simpleBlockItem(ModBlocks.POPLAR_BUTTON.get(), models().buttonInventory("poplar_button_inventory", planks));
        pressurePlateBlock(ModBlocks.POPLAR_PRESSURE_PLATE.get(), planks);
        simpleBlockItem(ModBlocks.POPLAR_PRESSURE_PLATE.get(), blockModel("poplar_pressure_plate"));

        // Doors and trapdoors have see-through pixels, so they render as "cutout".
        doorBlockWithRenderType(ModBlocks.POPLAR_DOOR.get(),
                modLoc("block/poplar_door_bottom"), modLoc("block/poplar_door_top"), "cutout");
        itemModels().basicItem(ModItems.POPLAR_DOOR.get()); // flat icon from item/poplar_door.png
        trapdoorBlockWithRenderType(ModBlocks.POPLAR_TRAPDOOR.get(), blockTexture(ModBlocks.POPLAR_TRAPDOOR.get()), true, "cutout");
        simpleBlockItem(ModBlocks.POPLAR_TRAPDOOR.get(), blockModel("poplar_trapdoor_bottom"));
    }

    private void logWithItem(RotatedPillarBlock block) {
        logBlock(block);
        simpleBlockItem(block, blockModel(block));
    }

    private void woodWithItem(RotatedPillarBlock block, ResourceLocation sideTexture) {
        axisBlock(block, sideTexture, sideTexture);
        simpleBlockItem(block, blockModel(block));
    }

    /** Points at a block model generated in this same run (so it can't be checked for existence yet). */
    private ModelFile blockModel(Block block) {
        return blockModel(BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    private ModelFile blockModel(String name) {
        return new ModelFile.UncheckedModelFile(modLoc("block/" + name));
    }
}
