package com.buffsteve.autumnbackport.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

/** Fire spread values, copied from 26.3's FireBlock. Higher = catches and burns faster. */
public final class ModFlammability {
    public static void register() {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        // Planks, slabs, stairs, fences, fence gates: 5 / 20 (same as oak)
        fire.setFlammable(ModBlocks.POPLAR_PLANKS.get(), 5, 20);
        fire.setFlammable(ModBlocks.POPLAR_STAIRS.get(), 5, 20);
        fire.setFlammable(ModBlocks.POPLAR_SLAB.get(), 5, 20);
        fire.setFlammable(ModBlocks.POPLAR_FENCE.get(), 5, 20);
        fire.setFlammable(ModBlocks.POPLAR_FENCE_GATE.get(), 5, 20);
        // Doors, trapdoors, buttons and pressure plates don't catch fire in vanilla either.
        // Logs and wood, stripped or not: 5 / 5
        fire.setFlammable(ModBlocks.POPLAR_LOG.get(), 5, 5);
        fire.setFlammable(ModBlocks.POPLAR_WOOD.get(), 5, 5);
        fire.setFlammable(ModBlocks.STRIPPED_POPLAR_LOG.get(), 5, 5);
        fire.setFlammable(ModBlocks.STRIPPED_POPLAR_WOOD.get(), 5, 5);
        // Leaves: 30 / 60
        fire.setFlammable(ModBlocks.RED_POPLAR_LEAVES.get(), 30, 60);
        fire.setFlammable(ModBlocks.ORANGE_POPLAR_LEAVES.get(), 30, 60);
        fire.setFlammable(ModBlocks.YELLOW_POPLAR_LEAVES.get(), 30, 60);
        // Red shrub: 60 / 100, same as grass and bushes. (Shelf mushrooms don't burn.)
        fire.setFlammable(ModBlocks.RED_SHRUB.get(), 60, 100);
        fire.setFlammable(ModBlocks.LEAF_LITTER.get(), 60, 100);
        // Straw bed: 30 / 60
        fire.setFlammable(ModBlocks.STRAW_BED.get(), 30, 60);
        // Wool stairs and slabs: 30 / 60, same as wool. (Concrete doesn't burn.)
        ModBlocks.WOOL_STAIRS.values().forEach(block -> fire.setFlammable(block.get(), 30, 60));
        ModBlocks.WOOL_SLABS.values().forEach(block -> fire.setFlammable(block.get(), 30, 60));
    }

    private ModFlammability() {}
}
