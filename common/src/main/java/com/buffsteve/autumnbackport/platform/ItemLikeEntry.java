package com.buffsteve.autumnbackport.platform;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/** A registered block or item that can be used anywhere vanilla expects an item (recipes, item stacks...). */
public final class ItemLikeEntry<T extends ItemLike> implements RegistryEntry<T>, ItemLike {
    private final RegistryEntry<T> entry;

    public ItemLikeEntry(RegistryEntry<T> entry) {
        this.entry = entry;
    }

    @Override
    public T get() {
        return entry.get();
    }

    @Override
    public ResourceLocation getId() {
        return entry.getId();
    }

    @Override
    public ResourceKey<? super T> getKey() {
        return entry.getKey();
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
