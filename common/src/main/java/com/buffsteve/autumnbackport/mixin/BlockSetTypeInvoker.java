package com.buffsteve.autumnbackport.mixin;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Vanilla's (private) list of block set types, which door and button sounds come from. */
@Mixin(BlockSetType.class)
public interface BlockSetTypeInvoker {
    @Invoker("register")
    static BlockSetType autumnbackport$register(BlockSetType type) {
        throw new AssertionError();
    }
}
