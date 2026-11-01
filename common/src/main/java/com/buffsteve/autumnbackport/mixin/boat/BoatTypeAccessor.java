package com.buffsteve.autumnbackport.mixin.boat;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Sets which planks a boat type drops when it breaks from a fall. */
@Mixin(Boat.Type.class)
public interface BoatTypeAccessor {
    @Mutable
    @Accessor("planks")
    void autumnbackport$setPlanks(Block planks);
}
