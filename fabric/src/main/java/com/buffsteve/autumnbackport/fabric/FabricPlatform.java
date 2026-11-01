package com.buffsteve.autumnbackport.fabric;

import com.buffsteve.autumnbackport.platform.Platform;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import com.buffsteve.autumnbackport.registry.ModBoats;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Fabric side of {@link Platform}: objects are registered straight away, in the order they're declared. */
public final class FabricPlatform implements Platform {
    @Override
    public <R, T extends R> RegistryEntry<T> register(Registry<R> registry, String name, Supplier<T> factory) {
        ResourceLocation id = ResourceLocation.withDefaultNamespace(name);
        T value = Registry.register(registry, id, factory.get());
        ResourceKey<R> key = ResourceKey.create(registry.key(), id);
        return new RegistryEntry<>() {
            @Override
            public T get() {
                return value;
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public ResourceKey<? super T> getKey() {
                return key;
            }
        };
    }

    /** Vanilla's own constructor; it also tells the empty flower pot about this plant. */
    @Override
    public FlowerPotBlock pottedPlant(Supplier<? extends Block> plant, BlockBehaviour.Properties properties) {
        return new FlowerPotBlock(plant.get(), properties);
    }

    @Override
    public Boat.Type poplarBoatType() {
        return ModBoats.poplar();
    }
}
