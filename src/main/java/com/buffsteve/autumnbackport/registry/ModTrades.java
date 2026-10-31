package com.buffsteve.autumnbackport.registry;

import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

/**
 * Wandering Trader offers, so survival players can get poplar and shelf mushrooms without the biome.
 */
public final class ModTrades {
    /** Random pool trades, rolled like vanilla's. */
    public static void addWandererTrades(WandererTradesEvent event) {
        // Common pool (same as 26.3): 1 emerald -> 3 shelf mushrooms, 12 times.
        event.getGenericTrades().add((trader, random) ->
                new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.SHELF_MUSHROOM.get(), 3), 12, 1, 0.05F));
        // Uncommon pool (same as 26.3): 1 emerald -> 8 poplar logs.
        event.getRareTrades().add((trader, random) ->
                new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.POPLAR_LOG.get(), 8), 4, 1, 0.05F));
    }

    /**
     * Guaranteed trade: every wandering trader sells poplar saplings, on top of its normal offers.
     * (In vanilla 26.3 it's only a random common trade; this mod makes it reliable.)
     * Runs when a trader spawns or is loaded from a save, and skips traders that already have it.
     */
    public static void addGuaranteedSapling(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof WanderingTrader trader)) {
            return;
        }
        MerchantOffers offers = trader.getOffers(); // rolls the normal offers first if it hasn't yet
        boolean hasSapling = offers.stream().anyMatch(offer -> offer.getResult().is(ModItems.POPLAR_SAPLING.get()));
        if (!hasSapling) {
            // 1 emerald -> 1 poplar sapling, up to 8 times. (26.3 charges 5; this mod makes it 1.)
            offers.add(new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.POPLAR_SAPLING.get()), 8, 1, 0.05F));
        }
    }

    private ModTrades() {}
}
