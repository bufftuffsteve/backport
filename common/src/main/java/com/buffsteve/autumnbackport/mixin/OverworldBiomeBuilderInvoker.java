package com.buffsteve.autumnbackport.mixin;

import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Vanilla's (private) method that lists the overworld biome layout. Used by DappledForestRegion. */
@Mixin(OverworldBiomeBuilder.class)
public interface OverworldBiomeBuilderInvoker {
    @Invoker("addBiomes")
    void autumnbackport$addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper);
}
