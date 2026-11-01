package com.buffsteve.autumnbackport.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Vanilla's (private) way to set how easily a block catches fire and burns away. */
@Mixin(FireBlock.class)
public interface FireBlockInvoker {
    @Invoker("setFlammable")
    void autumnbackport$setFlammable(Block block, int encouragement, int flammability);
}
