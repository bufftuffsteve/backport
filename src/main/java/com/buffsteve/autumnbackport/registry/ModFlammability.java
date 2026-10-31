package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.AutumnBackport;
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

        // Temporary check: compare with oak planks in the log. Remove once fire is confirmed.
        AutumnBackport.LOGGER.info("Fire check: poplar planks catch={} burn={}, oak planks catch={} burn={}",
                fire.getIgniteOdds(ModBlocks.POPLAR_PLANKS.get().defaultBlockState()),
                fire.getBurnOdds(ModBlocks.POPLAR_PLANKS.get().defaultBlockState()),
                fire.getIgniteOdds(Blocks.OAK_PLANKS.defaultBlockState()),
                fire.getBurnOdds(Blocks.OAK_PLANKS.defaultBlockState()));
    }

    private ModFlammability() {}
}
