package com.buffsteve.autumnbackport.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/** Tags that are new in 26.3, so 1.21.1 doesn't have constants for them. Same IDs as vanilla. */
public final class ModTags {
    public static final class Blocks {
        public static final TagKey<Block> POPLAR_LOGS = block("poplar_logs");
        public static final TagKey<Block> WOOL_STAIRS = block("wool_stairs");
        public static final TagKey<Block> WOOL_SLABS = block("wool_slabs");
        public static final TagKey<Block> CONCRETE_STAIRS = block("concrete_stairs");
        public static final TagKey<Block> CONCRETE_SLABS = block("concrete_slabs");
        /** Plants a huge mushroom can grow over. New in 1.21.5, so 1.21.1 doesn't use it yet. */
        /** Blocks with a hollow top where a cushion drops inside instead of sitting on the rim. */
        public static final TagKey<Block> CUSHION_USES_COLLISION_SHAPE = block("cushion_uses_collision_shape");
        public static final TagKey<Block> REPLACEABLE_BY_MUSHROOMS = block("replaceable_by_mushrooms");

        private static TagKey<Block> block(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> POPLAR_LOGS = item("poplar_logs");
        public static final TagKey<Item> WOOL_STAIRS = item("wool_stairs");
        public static final TagKey<Item> WOOL_SLABS = item("wool_slabs");
        public static final TagKey<Item> CONCRETE_STAIRS = item("concrete_stairs");
        public static final TagKey<Item> CONCRETE_SLABS = item("concrete_slabs");
        /** Every mushroom that works in stews (brown, red, shelf). */
        public static final TagKey<Item> MUSHROOMS = item("mushrooms");
        public static final TagKey<Item> CUSHIONS = item("cushions");

        private static TagKey<Item> item(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name));
        }
    }

    private ModTags() {}
}
