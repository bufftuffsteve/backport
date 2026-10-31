package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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

        // Every block in this mod so far is a wood block, so an axe is the right tool for all of them.
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
                ModBlocks.POPLAR_BUTTON.get());
    }
}
