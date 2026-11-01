package com.buffsteve.autumnbackport.mixin;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Vanilla's (private) list of wood types, which sign textures come from. */
@Mixin(WoodType.class)
public interface WoodTypeInvoker {
    @Invoker("register")
    static WoodType autumnbackport$register(WoodType type) {
        throw new AssertionError();
    }
}
