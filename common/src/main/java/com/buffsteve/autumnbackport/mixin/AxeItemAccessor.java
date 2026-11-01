package com.buffsteve.autumnbackport.mixin;

import java.util.Map;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Vanilla's log -> stripped log table, which every loader's axe stripping reads. */
@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Accessor("STRIPPABLES")
    static Map<Block, Block> autumnbackport$getStrippables() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("STRIPPABLES")
    static void autumnbackport$setStrippables(Map<Block, Block> strippables) {
        throw new AssertionError();
    }
}
