package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.block.StrippableLogBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Every backported block lives here.
 *
 * Registered under the "minecraft" namespace on purpose: a world built with
 * this mod on 1.21.1 opens in vanilla 26.3 with these blocks intact.
 * Only use IDs that exist in vanilla 26.3. Mod-only extras get their own register.
 *
 * All property values are copied from 26.3's own block definitions.
 * Don't use Properties.ofFullCopy(...): on 1.21.1 it also copies the source block's
 * loot table, so the new block would drop the wrong item.
 */
public final class ModBlocks {
    public static final String VANILLA = "minecraft";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(VANILLA);

    // ---- Poplar wood set ----

    private static final MapColor POPLAR_COLOR = MapColor.COLOR_LIGHT_GRAY;

    public static final DeferredBlock<Block> POPLAR_PLANKS = BLOCKS.registerSimpleBlock(
            "poplar_planks", wood(POPLAR_COLOR).strength(2.0F, 3.0F));

    // Stripped versions are registered first so the unstripped ones can point at them.
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_POPLAR_LOG = BLOCKS.register(
            "stripped_poplar_log", () -> new RotatedPillarBlock(log(POPLAR_COLOR, POPLAR_COLOR)));

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_POPLAR_WOOD = BLOCKS.register(
            "stripped_poplar_wood", () -> new RotatedPillarBlock(wood(POPLAR_COLOR).strength(2.0F)));

    // Log: light gray rings on the cut ends, podzol-brown bark on the sides.
    public static final DeferredBlock<StrippableLogBlock> POPLAR_LOG = BLOCKS.register(
            "poplar_log", () -> new StrippableLogBlock(STRIPPED_POPLAR_LOG, log(POPLAR_COLOR, MapColor.PODZOL)));

    public static final DeferredBlock<StrippableLogBlock> POPLAR_WOOD = BLOCKS.register(
            "poplar_wood", () -> new StrippableLogBlock(STRIPPED_POPLAR_WOOD, wood(MapColor.PODZOL).strength(2.0F)));

    public static final DeferredBlock<StairBlock> POPLAR_STAIRS = BLOCKS.register(
            "poplar_stairs", () -> new StairBlock(POPLAR_PLANKS.get().defaultBlockState(), wood(POPLAR_COLOR).strength(2.0F, 3.0F)));

    public static final DeferredBlock<SlabBlock> POPLAR_SLAB = BLOCKS.register(
            "poplar_slab", () -> new SlabBlock(wood(POPLAR_COLOR).strength(2.0F, 3.0F)));

    public static final DeferredBlock<FenceBlock> POPLAR_FENCE = BLOCKS.register(
            "poplar_fence", () -> new FenceBlock(wood(POPLAR_COLOR).strength(2.0F, 3.0F)));

    public static final DeferredBlock<FenceGateBlock> POPLAR_FENCE_GATE = BLOCKS.register(
            "poplar_fence_gate", () -> new FenceGateBlock(ModWoodTypes.POPLAR,
                    wood(POPLAR_COLOR).forceSolidOn().strength(2.0F, 3.0F)));

    public static final DeferredBlock<DoorBlock> POPLAR_DOOR = BLOCKS.register(
            "poplar_door", () -> new DoorBlock(ModWoodTypes.POPLAR_SET,
                    wood(POPLAR_COLOR).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<TrapDoorBlock> POPLAR_TRAPDOOR = BLOCKS.register(
            "poplar_trapdoor", () -> new TrapDoorBlock(ModWoodTypes.POPLAR_SET,
                    wood(POPLAR_COLOR).strength(3.0F).noOcclusion().isValidSpawn((state, level, pos, type) -> false)));

    public static final DeferredBlock<PressurePlateBlock> POPLAR_PRESSURE_PLATE = BLOCKS.register(
            "poplar_pressure_plate", () -> new PressurePlateBlock(ModWoodTypes.POPLAR_SET,
                    wood(POPLAR_COLOR).forceSolidOn().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));

    // Wooden buttons stay pressed for 30 ticks (1.5 seconds).
    public static final DeferredBlock<ButtonBlock> POPLAR_BUTTON = BLOCKS.register(
            "poplar_button", () -> new ButtonBlock(ModWoodTypes.POPLAR_SET, 30,
                    BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));

    // ---- Shared property builders (match 26.3's Blocks.logProperties) ----

    /** Wooden block basics: bass note block, wood sounds, burns in lava. Add strength separately. */
    private static BlockBehaviour.Properties wood(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .instrument(NoteBlockInstrument.BASS)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    /** A log whose map color depends on which way it faces: end grain vs. bark. */
    private static BlockBehaviour.Properties log(MapColor topColor, MapColor sideColor) {
        return BlockBehaviour.Properties.of()
                .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : sideColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    private ModBlocks() {}
}
