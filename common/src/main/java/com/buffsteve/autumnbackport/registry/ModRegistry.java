package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.platform.ItemLikeEntry;
import com.buffsteve.autumnbackport.platform.Platform;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Small helpers over {@link Platform#register}. Everything is registered under the
 * "minecraft" namespace (see {@link ModBlocks} for why).
 */
public final class ModRegistry {
    public static final String VANILLA = "minecraft";

    /**
     * Loads every registry class so all entries are queued, in an order where nothing is
     * looked up before it exists. Call once, early in mod startup, on every loader.
     */
    public static void init() {
        touch(ModParticles.class);
        touch(ModBlocks.class);
        touch(ModItems.class);
        touch(ModEntities.class);
        touch(ModStats.class);
        touch(com.buffsteve.autumnbackport.world.ModTreePlacers.class);
    }

    private static void touch(Class<?> type) {
        try {
            Class.forName(type.getName(), true, type.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <R, T extends R> RegistryEntry<T> register(Registry<R> registry, String name, Supplier<T> factory) {
        return Platform.INSTANCE.register(registry, name, factory);
    }

    public static <T extends Block> ItemLikeEntry<T> block(String name, Supplier<T> factory) {
        return new ItemLikeEntry<>(register(BuiltInRegistries.BLOCK, name, factory));
    }

    public static ItemLikeEntry<Block> simpleBlock(String name, Block.Properties properties) {
        return block(name, () -> new Block(properties));
    }

    public static <T extends Item> ItemLikeEntry<T> item(String name, Supplier<T> factory) {
        return new ItemLikeEntry<>(register(BuiltInRegistries.ITEM, name, factory));
    }

    /** The plain item form of a block, with the same ID. */
    public static ItemLikeEntry<BlockItem> blockItem(RegistryEntry<? extends Block> block) {
        return item(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private ModRegistry() {}
}
