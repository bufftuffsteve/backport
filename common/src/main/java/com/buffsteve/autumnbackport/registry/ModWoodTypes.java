package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.mixin.BlockSetTypeInvoker;
import com.buffsteve.autumnbackport.mixin.WoodTypeInvoker;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Poplar as a wood type, the same way vanilla defines oak or cherry.
 * BlockSetType: door, trapdoor, button and pressure plate sounds and behavior.
 * WoodType: fence gate sounds, and later the sign and hanging sign textures.
 */
public final class ModWoodTypes {
    public static final BlockSetType POPLAR_SET = BlockSetTypeInvoker.autumnbackport$register(new BlockSetType("poplar"));
    public static final WoodType POPLAR = WoodTypeInvoker.autumnbackport$register(new WoodType("poplar", POPLAR_SET));

    private ModWoodTypes() {}
}
