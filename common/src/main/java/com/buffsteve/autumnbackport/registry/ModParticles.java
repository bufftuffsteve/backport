package com.buffsteve.autumnbackport.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;

/** Falling-leaf particles, one per leaf color. Same IDs as 26.3. */
// (The "{}" makes a tiny subclass: the constructor is protected in vanilla.)
public final class ModParticles {
    public static final RegistryEntry<SimpleParticleType> RED_POPLAR_LEAVES =
            ModRegistry.register(BuiltInRegistries.PARTICLE_TYPE, "red_poplar_leaves", () -> new SimpleParticleType(false) {});
    public static final RegistryEntry<SimpleParticleType> ORANGE_POPLAR_LEAVES =
            ModRegistry.register(BuiltInRegistries.PARTICLE_TYPE, "orange_poplar_leaves", () -> new SimpleParticleType(false) {});
    public static final RegistryEntry<SimpleParticleType> YELLOW_POPLAR_LEAVES =
            ModRegistry.register(BuiltInRegistries.PARTICLE_TYPE, "yellow_poplar_leaves", () -> new SimpleParticleType(false) {});

    private ModParticles() {}
}
