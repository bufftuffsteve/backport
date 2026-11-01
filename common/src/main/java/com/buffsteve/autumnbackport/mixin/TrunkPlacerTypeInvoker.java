package com.buffsteve.autumnbackport.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** TrunkPlacerType's constructor is private in vanilla. */
@Mixin(TrunkPlacerType.class)
public interface TrunkPlacerTypeInvoker {
    @Invoker("<init>")
    static <P extends TrunkPlacer> TrunkPlacerType<P> autumnbackport$create(MapCodec<P> codec) {
        throw new AssertionError();
    }
}
