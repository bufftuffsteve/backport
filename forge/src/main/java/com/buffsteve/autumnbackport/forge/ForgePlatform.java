package com.buffsteve.autumnbackport.forge;

import com.buffsteve.autumnbackport.platform.Platform;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import com.buffsteve.autumnbackport.registry.ModBoats;
import com.buffsteve.autumnbackport.registry.ModRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** Forge side of {@link Platform}: everything goes through DeferredRegisters on the mod event bus. */
public final class ForgePlatform implements Platform {
    private static final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> REGISTERS = new LinkedHashMap<>();
    private static boolean attached;

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends R> RegistryEntry<T> register(Registry<R> registry, String name, Supplier<T> factory) {
        if (attached) {
            throw new IllegalStateException("Too late to register " + name + "; add its class to ModRegistry.init()");
        }
        ResourceKey<Registry<R>> key = (ResourceKey<Registry<R>>) registry.key();
        DeferredRegister<R> register = (DeferredRegister<R>) REGISTERS.computeIfAbsent(key,
                k -> DeferredRegister.create(key, ModRegistry.VANILLA));
        RegistryObject<T> object = register.register(name, factory);
        return new RegistryEntry<>() {
            @Override
            public T get() {
                return object.get();
            }

            @Override
            public ResourceLocation getId() {
                return object.getId();
            }

            @Override
            public ResourceKey<? super T> getKey() {
                return object.getKey();
            }
        };
    }

    /** Hooks every queued register up to the mod event bus. Call once, after AutumnBackport.init(). */
    static void attach(IEventBus modBus) {
        REGISTERS.values().forEach(register -> register.register(modBus));
        attached = true;
    }

    @Override
    public FlowerPotBlock pottedPlant(Supplier<? extends Block> plant, BlockBehaviour.Properties properties) {
        return new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, plant, properties);
    }

    @Override
    public Boat.Type poplarBoatType() {
        return ModBoats.poplar();
    }
}
