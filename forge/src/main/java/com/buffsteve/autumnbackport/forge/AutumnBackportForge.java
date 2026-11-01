package com.buffsteve.autumnbackport.forge;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModBoats;
import com.buffsteve.autumnbackport.registry.ModCompostAndFuel;
import com.buffsteve.autumnbackport.registry.ModCreativeTabs;
import com.buffsteve.autumnbackport.registry.ModEvents;
import com.buffsteve.autumnbackport.registry.ModTrades;
import com.buffsteve.autumnbackport.world.DappledForestRegion;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/** Forge entry point. The shared logic lives in the common module. */
@Mod(AutumnBackport.MODID)
public class AutumnBackportForge {
    /** Burn times by item, filled in once items exist. */
    private static Map<Item, Integer> burnTimes;

    public AutumnBackportForge(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        AutumnBackport.init();
        ForgePlatform.attach(modBus);

        modBus.addListener(AutumnBackportForge::commonSetup);
        modBus.addListener(AutumnBackportForge::addToTabs);
        MinecraftForge.EVENT_BUS.addListener((WandererTradesEvent event) -> {
            event.getGenericTrades().addAll(ModTrades.genericTrades());
            event.getRareTrades().addAll(ModTrades.rareTrades());
        });
        MinecraftForge.EVENT_BUS.addListener((EntityJoinLevelEvent event) -> ModTrades.addGuaranteedSapling(event.getEntity()));
        MinecraftForge.EVENT_BUS.addListener((PlayerSetSpawnEvent event) -> {
            if (ModEvents.shouldBlockSpawnPoint(event.getEntity(), event.getNewSpawn(), event.isForced())) {
                event.setCanceled(true);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                ModEvents.lowerStrawBedSleeper(event.player);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((FurnaceFuelBurnTimeEvent event) -> {
            Integer ticks = burnTimes == null ? null : burnTimes.get(event.getItemStack().getItem());
            if (ticks != null) {
                event.setBurnTime(ticks);
            }
        });
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AutumnBackport.setup();
            ModBoats.setPlanks();
            ModCompostAndFuel.registerCompostables();
            Map<Item, Integer> times = new HashMap<>();
            ModCompostAndFuel.burnTimes().forEach((item, ticks) -> times.put(item.get().asItem(), ticks));
            burnTimes = times;
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
                event.getEntries().putAfter(previous, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                previous = stack;
            }
        }
    }
}
