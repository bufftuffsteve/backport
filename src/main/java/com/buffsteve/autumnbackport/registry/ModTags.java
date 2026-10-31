package com.buffsteve.autumnbackport.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/** Tags that are new in 26.3, so 1.21.1 doesn't have constants for them. Same IDs as vanilla. */
public final class ModTags {
    public static final class Blocks {
        public static final TagKey<Block> POPLAR_LOGS = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("poplar_logs"));
    }

    public static final class Items {
        public static final TagKey<Item> POPLAR_LOGS = TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("poplar_logs"));
    }

    private ModTags() {}
}
