package com.buffsteve.autumnbackport.platform;

import java.util.ServiceLoader;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The few things each mod loader does its own way. Every loader module has one
 * implementation, found at startup through META-INF/services.
 */
public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class, Platform.class.getClassLoader())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No platform implementation for " + Platform.class.getName()));

    /** Registers an object in a vanilla registry under the "minecraft" namespace. */
    <R, T extends R> RegistryEntry<T> register(Registry<R> registry, String name, Supplier<T> factory);

    /** A flower pot holding {@code plant}. */
    FlowerPotBlock pottedPlant(Supplier<? extends Block> plant, BlockBehaviour.Properties properties);

    /** The poplar boat type (boat types are a fixed enum on this version, so each loader adds it its own way). */
    Boat.Type poplarBoatType();
}
