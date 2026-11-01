package com.buffsteve.autumnbackport.neoforge;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModItems;

import java.util.function.Supplier;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

/**
 * Poplar as a boat type. On 1.21.1 boat types are a fixed list (an enum), so NeoForge adds
 * this one at startup from META-INF/enumextensions.json, which points at this field.
 * NeoForge requires the name to start with this mod's ID, so the textures live in this mod's
 * own folder: assets/autumnbackport/textures/entity/boat/poplar.png and .../chest_boat/poplar.png.
 */
public final class ModBoatsNeoForge {
    public static final EnumProxy<Boat.Type> POPLAR = new EnumProxy<>(Boat.Type.class,
            (Supplier<Block>) () -> ModBlocks.POPLAR_PLANKS.get(),  // planks
            "autumnbackport:poplar",                                 // name used for textures and saving
            (Supplier<Item>) () -> ModItems.POPLAR_BOAT.get(),       // dropped when broken
            (Supplier<Item>) () -> ModItems.POPLAR_CHEST_BOAT.get(),
            (Supplier<Item>) () -> Items.STICK,
            false);                                                  // not a raft

    private ModBoatsNeoForge() {}
}
