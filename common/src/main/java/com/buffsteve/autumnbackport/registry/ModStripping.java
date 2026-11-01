package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.mixin.AxeItemAccessor;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.Block;

/**
 * Poplar logs and wood turn into their stripped versions when right-clicked with an axe,
 * keeping the same axis. Adds them to vanilla's table, which every loader's axe reads.
 */
public final class ModStripping {
    public static void register() {
        Map<Block, Block> strippables = new HashMap<>(AxeItemAccessor.autumnbackport$getStrippables());
        strippables.put(ModBlocks.POPLAR_LOG.get(), ModBlocks.STRIPPED_POPLAR_LOG.get());
        strippables.put(ModBlocks.POPLAR_WOOD.get(), ModBlocks.STRIPPED_POPLAR_WOOD.get());
        AxeItemAccessor.autumnbackport$setStrippables(strippables);
    }

    private ModStripping() {}
}
