package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

/** What each block drops when broken. */
public class ModBlockLootProvider extends BlockLootSubProvider {
    public ModBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.POPLAR_PLANKS.get());
        dropSelf(ModBlocks.POPLAR_LOG.get());
        dropSelf(ModBlocks.POPLAR_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_POPLAR_LOG.get());
        dropSelf(ModBlocks.STRIPPED_POPLAR_WOOD.get());
        dropSelf(ModBlocks.POPLAR_STAIRS.get());
        dropSelf(ModBlocks.POPLAR_FENCE.get());
        dropSelf(ModBlocks.POPLAR_FENCE_GATE.get());
        dropSelf(ModBlocks.POPLAR_TRAPDOOR.get());
        dropSelf(ModBlocks.POPLAR_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.POPLAR_BUTTON.get());
        // A double slab drops 2; a door drops 1 (only from its bottom half).
        add(ModBlocks.POPLAR_SLAB.get(), createSlabItemTable(ModBlocks.POPLAR_SLAB.get()));
        add(ModBlocks.POPLAR_DOOR.get(), createDoorTable(ModBlocks.POPLAR_DOOR.get()));
    }

    // Datagen fails if any registered block has no loot table, which catches forgotten blocks.
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
