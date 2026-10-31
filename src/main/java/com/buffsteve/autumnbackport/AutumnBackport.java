package com.buffsteve.autumnbackport;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModCreativeTabs;
import com.buffsteve.autumnbackport.registry.ModFlammability;
import com.buffsteve.autumnbackport.registry.ModItems;
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
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(ModCreativeTabs::addToTabs);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Vanilla's flammability map isn't thread-safe, so run this on the main thread.
        event.enqueueWork(ModFlammability::register);
    }
}
