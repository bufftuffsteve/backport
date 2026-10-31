package com.buffsteve.autumnbackport.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Falling-leaf particles, one per leaf color. Same IDs as 26.3. */
public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, ModBlocks.VANILLA);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RED_POPLAR_LEAVES =
            PARTICLES.register("red_poplar_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ORANGE_POPLAR_LEAVES =
            PARTICLES.register("orange_poplar_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_POPLAR_LEAVES =
            PARTICLES.register("yellow_poplar_leaves", () -> new SimpleParticleType(false));

    private ModParticles() {}
}
