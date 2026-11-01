package com.buffsteve.autumnbackport.platform;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Something registered through {@link Platform#register}. Works the same on every loader:
 * {@link #get()} returns the registered object once registration has happened.
 */
public interface RegistryEntry<T> extends Supplier<T> {
    ResourceLocation getId();

    ResourceKey<? super T> getKey();
}
