package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.block.ShelfMushroomBlock;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModItems;
import java.util.Map;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
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

        // Signs: the renderer draws them, so the block models only hold the break-particle texture
        // (planks for signs, stripped log for hanging signs, as in 26.3). Flat item icons.
        signBlock(ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get(), planks);
        hangingSignBlock(ModBlocks.POPLAR_HANGING_SIGN.get(), ModBlocks.POPLAR_WALL_HANGING_SIGN.get(),
                blockTexture(ModBlocks.STRIPPED_POPLAR_LOG.get()));
        itemModels().basicItem(ModItems.POPLAR_SIGN.get());
        itemModels().basicItem(ModItems.POPLAR_HANGING_SIGN.get());
        itemModels().basicItem(ModItems.POPLAR_BOAT.get());
        itemModels().basicItem(ModItems.POPLAR_CHEST_BOAT.get());

        // Leaves: one texture on all sides, "cutout_mipped" so the gaps are see-through
        // and stay crisp at a distance. Untinted, so the texture's own color shows.
        leavesWithItem(ModBlocks.RED_POPLAR_LEAVES.get());
        leavesWithItem(ModBlocks.ORANGE_POPLAR_LEAVES.get());
        leavesWithItem(ModBlocks.YELLOW_POPLAR_LEAVES.get());

        // Sapling: two crossed flat planes, like every vanilla sapling. Flat icon in the inventory.
        ResourceLocation sapling = blockTexture(ModBlocks.POPLAR_SAPLING.get());
        simpleBlock(ModBlocks.POPLAR_SAPLING.get(), models().cross("poplar_sapling", sapling).renderType("cutout"));
        itemModels().withExistingParent("poplar_sapling", mcLoc("item/generated")).texture("layer0", sapling);
        simpleBlock(ModBlocks.POTTED_POPLAR_SAPLING.get(), models()
                .withExistingParent("potted_poplar_sapling", mcLoc("block/flower_pot_cross"))
                .texture("plant", sapling).renderType("cutout"));

        // Shelf mushroom: 3D models copied from 26.3 (models/block/shelf_mushroom_stage0/1.json),
        // turned to face away from the block it's attached to. The item shows the small one.
        ModelFile smallShelf = models().getExistingFile(mcLoc("block/shelf_mushroom_stage0"));
        ModelFile largeShelf = models().getExistingFile(mcLoc("block/shelf_mushroom_stage1"));
        horizontalBlock(ModBlocks.SHELF_MUSHROOM.get(),
                // Offset 180: 26.3's models are drawn facing north at rotation 0 (north 0, east 90, south 180, west 270).
                state -> state.getValue(ShelfMushroomBlock.AGE) == 0 ? smallShelf : largeShelf, 180);
        simpleBlockItem(ModBlocks.SHELF_MUSHROOM.get(), smallShelf);

        // Red shrub: two crossed flat planes like grass, flat icon in the inventory.
        ResourceLocation shrub = blockTexture(ModBlocks.RED_SHRUB.get());
        simpleBlock(ModBlocks.RED_SHRUB.get(), models().cross("red_shrub", shrub).renderType("cutout"));
        itemModels().withExistingParent("red_shrub", mcLoc("item/generated")).texture("layer0", shrub);

        // Leaf litter: its blockstate, block models and item model are plain files in src/main/resources
        // (copied from 26.3), since each piece is a separate model added on top of the others.

        // Straw bed: 26.3's head and foot models (models/block/straw_bed_*.json), turned to match
        // vanilla beds. The item model (models/item/straw_bed.json) is a merged copy of both, made by hand.
        ModelFile strawHead = models().getExistingFile(mcLoc("block/straw_bed_head"));
        ModelFile strawFoot = models().getExistingFile(mcLoc("block/straw_bed_foot"));
        getVariantBuilder(ModBlocks.STRAW_BED.get()).forAllStatesExcept(state -> ConfiguredModel.builder()
                .modelFile(state.getValue(BedBlock.PART) == BedPart.HEAD ? strawHead : strawFoot)
                .rotationY(((int) state.getValue(BedBlock.FACING).toYRot()) % 360)
                .build(), BedBlock.OCCUPIED);

        // Cushion items: flat icons from item/<color>_cushion.png.
        ModItems.CUSHIONS.values().forEach(item -> itemModels().basicItem(item.get()));

        // Wool and concrete stairs and slabs use the full block's own vanilla texture.
        // A double slab looks exactly like the full block, so it reuses vanilla's block model.
        coloredCuts(ModBlocks.WOOL_STAIRS, ModBlocks.WOOL_SLABS, "_wool");
        coloredCuts(ModBlocks.CONCRETE_STAIRS, ModBlocks.CONCRETE_SLABS, "_concrete");
    }

    private void coloredCuts(Map<DyeColor, DeferredBlock<StairBlock>> stairs, Map<DyeColor, DeferredBlock<SlabBlock>> slabs, String suffix) {
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation full = mcLoc("block/" + color.getName() + suffix);
            stairsBlock(stairs.get(color).get(), full);
            simpleBlockItem(stairs.get(color).get(), blockModel(stairs.get(color).get()));
            slabBlock(slabs.get(color).get(), full, full);
            simpleBlockItem(slabs.get(color).get(), blockModel(slabs.get(color).get()));
        }
    }

    private void leavesWithItem(Block block) {
        simpleBlockWithItem(block, models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockTexture(block))
                .renderType("cutout_mipped"));
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
