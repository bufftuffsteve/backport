package com.buffsteve.autumnbackport.neoforge.datagen;

import com.buffsteve.autumnbackport.block.LeafLitterBlock;
import com.buffsteve.autumnbackport.block.ShelfMushroomBlock;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import com.buffsteve.autumnbackport.neoforge.NeoForgePlatform;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;

/** What each block drops when broken. */
public class ModBlockLootProvider extends BlockLootSubProvider {
    public ModBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.POPLAR_PLANKS.get());
        dropSelf(ModBlocks.POPLAR_LOG.get());
        dropSelf(ModBlocks.POPLAR_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_POPLAR_LOG.get());
        dropSelf(ModBlocks.STRIPPED_POPLAR_WOOD.get());
        dropSelf(ModBlocks.POPLAR_STAIRS.get());
        dropSelf(ModBlocks.POPLAR_FENCE.get());
        dropSelf(ModBlocks.POPLAR_FENCE_GATE.get());
        dropSelf(ModBlocks.POPLAR_TRAPDOOR.get());
        dropSelf(ModBlocks.POPLAR_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.POPLAR_BUTTON.get());
        // Wall signs use these same tables.
        dropSelf(ModBlocks.POPLAR_SIGN.get());
        dropSelf(ModBlocks.POPLAR_HANGING_SIGN.get());
        // A double slab drops 2; a door drops 1 (only from its bottom half).
        add(ModBlocks.POPLAR_SLAB.get(), createSlabItemTable(ModBlocks.POPLAR_SLAB.get()));
        add(ModBlocks.POPLAR_DOOR.get(), createDoorTable(ModBlocks.POPLAR_DOOR.get()));

        // Leaves: themselves with shears or Silk Touch. Otherwise a 5% sapling chance
        // (higher with Fortune) and a 2% chance of 1-2 sticks. No apples, same as 26.3.
        leaves(ModBlocks.RED_POPLAR_LEAVES.get());
        leaves(ModBlocks.ORANGE_POPLAR_LEAVES.get());
        leaves(ModBlocks.YELLOW_POPLAR_LEAVES.get());
        dropSelf(ModBlocks.POPLAR_SAPLING.get());
        // Breaking a potted sapling drops the pot and the sapling.
        add(ModBlocks.POTTED_POPLAR_SAPLING.get(), createPotFlowerItemTable(ModBlocks.POPLAR_SAPLING.get()));

        // Shelf mushroom: 1 when small, 2 when large (fewer if blown up).
        add(ModBlocks.SHELF_MUSHROOM.get(), shelfMushroomDrops(ModBlocks.SHELF_MUSHROOM.get()));
        dropSelf(ModBlocks.RED_SHRUB.get());
        // Leaf litter: one item per piece (like pink petals).
        add(ModBlocks.LEAF_LITTER.get(), leafLitterDrops(ModBlocks.LEAF_LITTER.get()));
        // Straw bed: one item, from the head half only (like vanilla beds).
        add(ModBlocks.STRAW_BED.get(), createSinglePropConditionTable(ModBlocks.STRAW_BED.get(), BedBlock.PART, BedPart.HEAD));

        // Wool and concrete cuts drop themselves (a double slab drops 2).
        // Concrete still needs a pickaxe to drop anything, like the full block.
        ModBlocks.WOOL_STAIRS.values().forEach(block -> dropSelf(block.get()));
        ModBlocks.CONCRETE_STAIRS.values().forEach(block -> dropSelf(block.get()));
        ModBlocks.WOOL_SLABS.values().forEach(block -> add(block.get(), createSlabItemTable(block.get())));
        ModBlocks.CONCRETE_SLABS.values().forEach(block -> add(block.get(), createSlabItemTable(block.get())));
    }

    private LootTable.Builder shelfMushroomDrops(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(applyExplosionDecay(block, LootItem.lootTableItem(block)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(ShelfMushroomBlock.AGE, 1)))))));
    }

    /** Like vanilla's pink petals drops, but for leaf litter's own "segment_amount" property. */
    private LootTable.Builder leafLitterDrops(Block block) {
        LootPoolSingletonContainer.Builder<?> item = LootItem.lootTableItem(block);
        for (int pieces = 1; pieces <= 4; pieces++) {
            item.apply(SetItemCountFunction.setCount(ConstantValue.exactly(pieces))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LeafLitterBlock.AMOUNT, pieces))));
        }
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(applyExplosionDecay(block, item)));
    }

    private void leaves(Block leaves) {
        add(leaves, createLeavesDrops(leaves, ModBlocks.POPLAR_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
    }

    // Datagen fails if any registered block has no loot table, which catches forgotten blocks.
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return NeoForgePlatform.entries(Registries.BLOCK);
    }
}
