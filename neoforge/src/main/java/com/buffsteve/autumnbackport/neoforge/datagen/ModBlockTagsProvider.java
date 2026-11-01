package com.buffsteve.autumnbackport.neoforge.datagen;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Block tags. These add to vanilla's tags rather than replacing them,
 * and mirror where 26.3 puts each block. The wooden_* tags feed into the general
 * ones (#stairs, #slabs, #fences, #doors...), which many mods rely on.
 */
public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper files) {
        super(output, lookup, AutumnBackport.MODID, files);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // #poplar_logs is new in 26.3: every log and wood variant.
        tag(ModTags.Blocks.POPLAR_LOGS).add(
                ModBlocks.POPLAR_LOG.get(),
                ModBlocks.POPLAR_WOOD.get(),
                ModBlocks.STRIPPED_POPLAR_LOG.get(),
                ModBlocks.STRIPPED_POPLAR_WOOD.get());
        // #logs_that_burn feeds #logs, which other mods and vanilla recipes use.
        tag(BlockTags.LOGS_THAT_BURN).addTag(ModTags.Blocks.POPLAR_LOGS);
        tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(ModBlocks.POPLAR_LOG.get());

        tag(BlockTags.PLANKS).add(ModBlocks.POPLAR_PLANKS.get());
        tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.POPLAR_STAIRS.get());
        tag(BlockTags.WOODEN_SLABS).add(ModBlocks.POPLAR_SLAB.get());
        tag(BlockTags.WOODEN_FENCES).add(ModBlocks.POPLAR_FENCE.get());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.POPLAR_FENCE_GATE.get());
        tag(BlockTags.WOODEN_DOORS).add(ModBlocks.POPLAR_DOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.POPLAR_TRAPDOOR.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.POPLAR_PRESSURE_PLATE.get());
        tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.POPLAR_BUTTON.get());

        tag(BlockTags.STANDING_SIGNS).add(ModBlocks.POPLAR_SIGN.get());
        tag(BlockTags.WALL_SIGNS).add(ModBlocks.POPLAR_WALL_SIGN.get());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.POPLAR_HANGING_SIGN.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.POPLAR_WALL_HANGING_SIGN.get());

        // #leaves also makes hoes and shears break them quickly.
        tag(BlockTags.LEAVES).add(
                ModBlocks.RED_POPLAR_LEAVES.get(),
                ModBlocks.ORANGE_POPLAR_LEAVES.get(),
                ModBlocks.YELLOW_POPLAR_LEAVES.get());
        tag(BlockTags.SAPLINGS).add(ModBlocks.POPLAR_SAPLING.get());
        tag(BlockTags.FLOWER_POTS).add(ModBlocks.POTTED_POPLAR_SAPLING.get());

        // Growing trees can overwrite shelf mushrooms; placing any block or a huge mushroom overwrites shrubs.
        tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.SHELF_MUSHROOM.get());
        tag(BlockTags.REPLACEABLE).add(ModBlocks.RED_SHRUB.get(), ModBlocks.LEAF_LITTER.get());
        tag(ModTags.Blocks.REPLACEABLE_BY_MUSHROOMS).add(ModBlocks.RED_SHRUB.get(), ModBlocks.LEAF_LITTER.get());
        tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.LEAF_LITTER.get());

        // Straw bed: a hoe breaks it fastest (like hay). Not in #beds, so villagers and
        // other mods' bed checks leave it alone, same as 26.3.
        tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.STRAW_BED.get());

        // Cushions placed on these land inside, on the real collision shape (26.3 tag).
        tag(ModTags.Blocks.CUSHION_USES_COLLISION_SHAPE).addTag(BlockTags.CAULDRONS).add(Blocks.HOPPER, Blocks.COMPOSTER);

        // Wool and concrete cuts, mirroring 26.3. The new tags feed into #stairs and #slabs.
        ModBlocks.WOOL_STAIRS.values().forEach(b -> tag(ModTags.Blocks.WOOL_STAIRS).add(b.get()));
        ModBlocks.WOOL_SLABS.values().forEach(b -> tag(ModTags.Blocks.WOOL_SLABS).add(b.get()));
        ModBlocks.CONCRETE_STAIRS.values().forEach(b -> tag(ModTags.Blocks.CONCRETE_STAIRS).add(b.get()));
        ModBlocks.CONCRETE_SLABS.values().forEach(b -> tag(ModTags.Blocks.CONCRETE_SLABS).add(b.get()));
        tag(BlockTags.STAIRS).addTag(ModTags.Blocks.WOOL_STAIRS).addTag(ModTags.Blocks.CONCRETE_STAIRS);
        tag(BlockTags.SLABS).addTag(ModTags.Blocks.WOOL_SLABS).addTag(ModTags.Blocks.CONCRETE_SLABS);
        // Wool muffles sculk sensors, and so do its stairs and slabs.
        tag(BlockTags.DAMPENS_VIBRATIONS).addTag(ModTags.Blocks.WOOL_STAIRS).addTag(ModTags.Blocks.WOOL_SLABS);
        // Concrete needs a pickaxe.
        tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(ModTags.Blocks.CONCRETE_STAIRS).addTag(ModTags.Blocks.CONCRETE_SLABS);

        // Wooden blocks: an axe mines these fastest.
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                ModBlocks.POPLAR_PLANKS.get(),
                ModBlocks.POPLAR_LOG.get(),
                ModBlocks.POPLAR_WOOD.get(),
                ModBlocks.STRIPPED_POPLAR_LOG.get(),
                ModBlocks.STRIPPED_POPLAR_WOOD.get(),
                ModBlocks.POPLAR_STAIRS.get(),
                ModBlocks.POPLAR_SLAB.get(),
                ModBlocks.POPLAR_FENCE.get(),
                ModBlocks.POPLAR_FENCE_GATE.get(),
                ModBlocks.POPLAR_DOOR.get(),
                ModBlocks.POPLAR_TRAPDOOR.get(),
                ModBlocks.POPLAR_PRESSURE_PLATE.get(),
                ModBlocks.POPLAR_BUTTON.get(),
                ModBlocks.POPLAR_SIGN.get(),
                ModBlocks.POPLAR_WALL_SIGN.get(),
                ModBlocks.POPLAR_HANGING_SIGN.get(),
                ModBlocks.POPLAR_WALL_HANGING_SIGN.get());
    }
}
