package com.buffsteve.autumnbackport.fabric;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBoats;
import com.buffsteve.autumnbackport.registry.ModCompostAndFuel;
import com.buffsteve.autumnbackport.registry.ModCreativeTabs;
import com.buffsteve.autumnbackport.registry.ModTrades;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.level.ItemLike;

/**
 * Fabric entry point. The shared logic lives in the common module.
 * The Dappled Forest is added from {@link AutumnBackportTerraBlender}, which TerraBlender calls.
 * Straw bed spawn and sleeping height are handled by this module's mixins.
 */
public class AutumnBackportFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutumnBackport.init();
        // Fabric registers everything immediately, so setup can run right away.
        AutumnBackport.setup();
        ModBoats.setPlanks();

        ModCompostAndFuel.compostChances().forEach((item, chance) -> CompostingChanceRegistry.INSTANCE.add(item.get(), chance));
        ModCompostAndFuel.burnTimes().forEach((item, ticks) -> FuelRegistry.INSTANCE.add(item.get(), ticks));

        for (var tab : ModCreativeTabs.TABS) {
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                for (ModCreativeTabs.Insertion insertion : ModCreativeTabs.insertionsFor(tab)) {
                    entries.addAfter(insertion.anchor(), insertion.items().stream().map(item -> (ItemLike) item.get()).toArray(ItemLike[]::new));
                }
            });
        }

        // Level 1 is the wandering trader's common pool, level 2 the uncommon one.
        TradeOfferHelper.registerWanderingTraderOffers(1, trades -> trades.addAll(ModTrades.genericTrades()));
        TradeOfferHelper.registerWanderingTraderOffers(2, trades -> trades.addAll(ModTrades.rareTrades()));
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> ModTrades.addGuaranteedSapling(entity));
    }
}
