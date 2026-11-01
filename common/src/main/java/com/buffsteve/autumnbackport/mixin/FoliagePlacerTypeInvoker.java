package com.buffsteve.autumnbackport.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** FoliagePlacerType's constructor is private in vanilla. */
@Mixin(FoliagePlacerType.class)
public interface FoliagePlacerTypeInvoker {
    @Invoker("<init>")
    static <P extends FoliagePlacer> FoliagePlacerType<P> autumnbackport$create(Codec<P> codec) {
        throw new AssertionError();
    }
}
