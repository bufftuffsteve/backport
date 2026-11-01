package com.buffsteve.autumnbackport.neoforge;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModCreativeTabs;
import com.buffsteve.autumnbackport.registry.ModEvents;
import com.buffsteve.autumnbackport.registry.ModTrades;
import com.buffsteve.autumnbackport.world.DappledForestRegion;
import java.util.function.Supplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

/** NeoForge entry point. The shared logic lives in the common module. */
@Mod(AutumnBackport.MODID)
public class AutumnBackportNeoForge {
    public AutumnBackportNeoForge(IEventBus modBus, ModContainer container) {
        AutumnBackport.init();
        NeoForgePlatform.attach(modBus);

        modBus.addListener(AutumnBackportNeoForge::commonSetup);
        modBus.addListener(AutumnBackportNeoForge::addToTabs);
        NeoForge.EVENT_BUS.addListener((WandererTradesEvent event) -> {
            event.getGenericTrades().addAll(ModTrades.genericTrades());
            event.getRareTrades().addAll(ModTrades.rareTrades());
        });
        NeoForge.EVENT_BUS.addListener((EntityJoinLevelEvent event) -> ModTrades.addGuaranteedSapling(event.getEntity()));
        NeoForge.EVENT_BUS.addListener((PlayerSetSpawnEvent event) -> {
            if (ModEvents.shouldBlockSpawnPoint(event.getEntity(), event.getNewSpawn(), event.isForced())) {
                event.setCanceled(true);
            }
        });
        NeoForge.EVENT_BUS.addListener((PlayerTickEvent.Post event) -> ModEvents.lowerStrawBedSleeper(event.getEntity()));
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AutumnBackport.setup();
            // Dappled Forest world generation (needs TerraBlender).
            DappledForestRegion.register();
            // Lets players put a poplar sapling into an empty flower pot.
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.POPLAR_SAPLING.getId(), ModBlocks.POTTED_POPLAR_SAPLING);
        });
    }

    private static void addToTabs(BuildCreativeModeTabContentsEvent event) {
        for (ModCreativeTabs.Insertion insertion : ModCreativeTabs.insertionsFor(event.getTabKey())) {
            ItemStack previous = new ItemStack(insertion.anchor());
            for (Supplier<? extends ItemLike> item : insertion.items()) {
                ItemStack stack = new ItemStack(item.get());
                event.insertAfter(previous, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                previous = stack;
            }
        }
    }
}
