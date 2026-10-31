package com.buffsteve.autumnbackport;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModCreativeTabs;
import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.registry.ModEvents;
import com.buffsteve.autumnbackport.registry.ModStats;
import com.buffsteve.autumnbackport.registry.ModFlammability;
import com.buffsteve.autumnbackport.registry.ModItems;
import com.buffsteve.autumnbackport.registry.ModParticles;
import com.buffsteve.autumnbackport.registry.ModTrades;
import com.buffsteve.autumnbackport.world.ModTreePlacers;
import com.buffsteve.autumnbackport.registry.ModWoodTypes;
import com.buffsteve.autumnbackport.world.DappledForestRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.neoforge.common.NeoForge;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * Main class for the Autumn Backport mod.
 * MODID must match mod_id in gradle.properties.
 */
@Mod(AutumnBackport.MODID)
public class AutumnBackport {
    public static final String MODID = "autumnbackport";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AutumnBackport(IEventBus modEventBus, ModContainer modContainer) {
        // Load the poplar wood type before the game builds its list of sign textures.
        LOGGER.debug("Wood type {} ready", ModWoodTypes.POPLAR.name());
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModParticles.PARTICLES.register(modEventBus);
        ModTreePlacers.TRUNK_PLACERS.register(modEventBus);
        ModTreePlacers.FOLIAGE_PLACERS.register(modEventBus);
        ModTreePlacers.TREE_DECORATORS.register(modEventBus);
        ModTreePlacers.FEATURES.register(modEventBus);
        ModStats.STATS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(ModCreativeTabs::addToTabs);
        modEventBus.addListener(AutumnBackport::addSignsToBlockEntities);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(ModTrades::addWandererTrades);
        NeoForge.EVENT_BUS.addListener(ModTrades::addGuaranteedSapling);
        NeoForge.EVENT_BUS.addListener(ModEvents::keepSpawnOffStrawBeds);
        NeoForge.EVENT_BUS.addListener(ModEvents::lowerStrawBedSleepers);
    }

    /** Lets the vanilla sign block entities (which store the text) work in poplar signs. */
    private static void addSignsToBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, ModBlocks.POPLAR_HANGING_SIGN.get(), ModBlocks.POPLAR_WALL_HANGING_SIGN.get());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Vanilla's flammability map isn't thread-safe, so run this on the main thread.
        event.enqueueWork(() -> {
            ModFlammability.register();
            ModStats.register();
            // Dappled Forest world generation (needs TerraBlender).
            DappledForestRegion.register();
            // Lets players put a poplar sapling into an empty flower pot.
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.POPLAR_SAPLING.getId(), ModBlocks.POTTED_POPLAR_SAPLING);
        });
    }
}
