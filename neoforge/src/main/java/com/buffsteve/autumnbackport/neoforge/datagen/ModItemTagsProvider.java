package com.buffsteve.autumnbackport.neoforge.datagen;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModItems;
import com.buffsteve.autumnbackport.registry.ModTags;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Item tags, copied from the matching block tags.
 * #planks and #logs give furnace fuel (300 ticks) and work in vanilla recipes;
 * #logs_that_burn also makes logs smelt into charcoal.
 */
public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper files) {
        super(output, lookup, blockTags, AutumnBackport.MODID, files);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(ModTags.Blocks.POPLAR_LOGS, ModTags.Items.POPLAR_LOGS);
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        tag(ItemTags.SIGNS).add(ModItems.POPLAR_SIGN.get());
        tag(ItemTags.HANGING_SIGNS).add(ModItems.POPLAR_HANGING_SIGN.get());
        tag(ItemTags.BOATS).add(ModItems.POPLAR_BOAT.get());
        tag(ItemTags.CHEST_BOATS).add(ModItems.POPLAR_CHEST_BOAT.get());
        copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        copy(ModTags.Blocks.WOOL_STAIRS, ModTags.Items.WOOL_STAIRS);
        copy(ModTags.Blocks.WOOL_SLABS, ModTags.Items.WOOL_SLABS);
        copy(ModTags.Blocks.CONCRETE_STAIRS, ModTags.Items.CONCRETE_STAIRS);
        copy(ModTags.Blocks.CONCRETE_SLABS, ModTags.Items.CONCRETE_SLABS);
        // #mushrooms (new in 26.3): any two of these make mushroom stew or suspicious stew.
        tag(ModTags.Items.MUSHROOMS).add(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, ModItems.SHELF_MUSHROOM.get());
        ModItems.CUSHIONS.values().forEach(item -> tag(ModTags.Items.CUSHIONS).add(item.get()));
        tag(ItemTags.STAIRS).addTag(ModTags.Items.WOOL_STAIRS).addTag(ModTags.Items.CONCRETE_STAIRS);
        tag(ItemTags.SLABS).addTag(ModTags.Items.WOOL_SLABS).addTag(ModTags.Items.CONCRETE_SLABS);
        tag(ItemTags.DAMPENS_VIBRATIONS).addTag(ModTags.Items.WOOL_STAIRS).addTag(ModTags.Items.WOOL_SLABS);
    }
}
