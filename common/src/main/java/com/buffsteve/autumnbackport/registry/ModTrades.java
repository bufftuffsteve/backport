package com.buffsteve.autumnbackport.registry;

import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

/**
 * Wandering Trader offers, so survival players can get poplar and shelf mushrooms without the biome.
 * Each loader adds these through its own trade API.
 */
public final class ModTrades {
    /** Common pool (same as 26.3): 1 emerald -> 3 shelf mushrooms, 12 times. */
    public static List<VillagerTrades.ItemListing> genericTrades() {
        return List.of((trader, random) ->
                new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.SHELF_MUSHROOM.get(), 3), 12, 1, 0.05F));
    }

    /** Uncommon pool (same as 26.3): 1 emerald -> 8 poplar logs. */
    public static List<VillagerTrades.ItemListing> rareTrades() {
        return List.of((trader, random) ->
                new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.POPLAR_LOG.get(), 8), 4, 1, 0.05F));
    }

    /**
     * Guaranteed trade: every wandering trader sells poplar saplings, on top of its normal offers.
     * (In vanilla 26.3 it's only a random common trade; this mod makes it reliable.)
     * Call on the server when an entity spawns or is loaded from a save; skips traders that already have it.
     */
    public static void addGuaranteedSapling(Entity entity) {
        if (entity.level().isClientSide() || !(entity instanceof WanderingTrader trader)) {
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
