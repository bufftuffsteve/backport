package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.block.LeafLitterBlock;
import com.buffsteve.autumnbackport.block.PoplarLeavesBlock;
import com.buffsteve.autumnbackport.block.PoplarSaplingBlock;
import com.buffsteve.autumnbackport.block.RedShrubBlock;
import com.buffsteve.autumnbackport.block.ShelfMushroomBlock;
import com.buffsteve.autumnbackport.block.StrawBedBlock;
import com.buffsteve.autumnbackport.block.StrippableLogBlock;
import net.minecraft.world.entity.EntityType;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
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

    // ---- Signs ----
    // Text and look come from the vanilla sign renderers, using
    // textures/entity/signs/poplar.png and textures/entity/signs/hanging/poplar.png.

    public static final DeferredBlock<StandingSignBlock> POPLAR_SIGN = BLOCKS.register(
            "poplar_sign", () -> new StandingSignBlock(ModWoodTypes.POPLAR, sign(POPLAR_COLOR)));
    // Wall versions drop the same item as the standing ones.
    public static final DeferredBlock<WallSignBlock> POPLAR_WALL_SIGN = BLOCKS.register(
            "poplar_wall_sign", () -> new WallSignBlock(ModWoodTypes.POPLAR, sign(POPLAR_COLOR).lootFrom(POPLAR_SIGN)));
    public static final DeferredBlock<CeilingHangingSignBlock> POPLAR_HANGING_SIGN = BLOCKS.register(
            "poplar_hanging_sign", () -> new CeilingHangingSignBlock(ModWoodTypes.POPLAR, sign(POPLAR_COLOR)));
    public static final DeferredBlock<WallHangingSignBlock> POPLAR_WALL_HANGING_SIGN = BLOCKS.register(
            "poplar_wall_hanging_sign", () -> new WallHangingSignBlock(ModWoodTypes.POPLAR,
                    sign(POPLAR_COLOR).lootFrom(POPLAR_HANGING_SIGN)));

    // ---- Leaves and sapling ----

    // Three separate leaf blocks with fixed colors (no biome tint), each with its own falling leaf.
    public static final DeferredBlock<PoplarLeavesBlock> RED_POPLAR_LEAVES = BLOCKS.register(
            "red_poplar_leaves", () -> new PoplarLeavesBlock(ModParticles.RED_POPLAR_LEAVES, leaves(MapColor.COLOR_RED)));
    public static final DeferredBlock<PoplarLeavesBlock> ORANGE_POPLAR_LEAVES = BLOCKS.register(
            "orange_poplar_leaves", () -> new PoplarLeavesBlock(ModParticles.ORANGE_POPLAR_LEAVES, leaves(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<PoplarLeavesBlock> YELLOW_POPLAR_LEAVES = BLOCKS.register(
            "yellow_poplar_leaves", () -> new PoplarLeavesBlock(ModParticles.YELLOW_POPLAR_LEAVES, leaves(MapColor.COLOR_YELLOW)));

    // 26.3 really does give the sapling the light gray "metal" map color.
    // Grows into a red, orange or yellow poplar at random.
    public static final DeferredBlock<PoplarSaplingBlock> POPLAR_SAPLING = BLOCKS.register(
            "poplar_sapling", () -> new PoplarSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).noCollission().randomTicks().instabreak()
                    .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<FlowerPotBlock> POTTED_POPLAR_SAPLING = BLOCKS.register(
            "potted_poplar_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) net.minecraft.world.level.block.Blocks.FLOWER_POT,
                    POPLAR_SAPLING, BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));

    // ---- Forest floor plants ----

    // Grows sideways out of tree trunks (and any solid wall). Bouncy, like a small trampoline.
    public static final DeferredBlock<ShelfMushroomBlock> SHELF_MUSHROOM = BLOCKS.register(
            "shelf_mushroom", () -> new ShelfMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW).instabreak().noOcclusion()
                    .sound(SoundType.FUNGUS) // 26.3 has its own shelf mushroom sounds that 1.21.1 doesn't have
                    .pushReaction(PushReaction.DESTROY)));

    // Low red bush for the forest floor. Walk-through, burns easily, replaced when you place a block in it.
    public static final DeferredBlock<RedShrubBlock> RED_SHRUB = BLOCKS.register(
            "red_shrub", () -> new RedShrubBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CRIMSON_NYLIUM).replaceable().noCollission().instabreak()
                    .sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    // Fallen leaves (from 1.21.5). Walk-through, replaced when you build over it, burns.
    public static final DeferredBlock<LeafLitterBlock> LEAF_LITTER = BLOCKS.register(
            "leaf_litter", () -> new LeafLitterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).replaceable().noCollission()
                    .sound(SoundType.PINK_PETALS) // 1.21.5 has its own leaf litter sounds
                    .ignitedByLava().pushReaction(PushReaction.DESTROY)));

    // ---- Straw bed ----

    // Single-use bed that doesn't set your spawn. Hay sounds; pistons break it.
    public static final DeferredBlock<StrawBedBlock> STRAW_BED = BLOCKS.register(
            "straw_bed", () -> new StrawBedBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).sound(SoundType.GRASS) // 26.3 has its own straw bed sounds
                    .strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY))); // 26.3 "POPPED"; DESTROY is the 1.21.1 equivalent

    // ---- Wool and concrete stairs and slabs (16 colors each) ----
    // Same properties as the full block: wool is soft and burns, concrete needs a pickaxe.

    public static final Map<DyeColor, DeferredBlock<StairBlock>> WOOL_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredBlock<SlabBlock>> WOOL_SLABS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredBlock<StairBlock>> CONCRETE_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredBlock<SlabBlock>> CONCRETE_SLABS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String wool = color.getName() + "_wool";
            String concrete = color.getName() + "_concrete";
            WOOL_STAIRS.put(color, BLOCKS.register(wool + "_stairs",
                    () -> new StairBlock(vanillaBlock(wool).defaultBlockState(), wool(color))));
            WOOL_SLABS.put(color, BLOCKS.register(wool + "_slab", () -> new SlabBlock(wool(color))));
            CONCRETE_STAIRS.put(color, BLOCKS.register(concrete + "_stairs",
                    () -> new StairBlock(vanillaBlock(concrete).defaultBlockState(), concrete(color))));
            CONCRETE_SLABS.put(color, BLOCKS.register(concrete + "_slab", () -> new SlabBlock(concrete(color))));
        }
    }

    /** The vanilla full block these stairs or slabs are cut from, e.g. "red_wool" -> minecraft:red_wool. */
    public static Block vanillaBlock(String name) {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(name));
    }

    /** Same as vanilla wool: guitar note block, hardness 0.8, wool sounds, burns. */
    private static BlockBehaviour.Properties wool(DyeColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color.getMapColor())
                .instrument(NoteBlockInstrument.GUITAR)
                .strength(0.8F)
                .sound(SoundType.WOOL)
                .ignitedByLava();
    }

    /** Same as vanilla concrete: bass drum note block, hardness 1.8, drops only with a pickaxe. */
    private static BlockBehaviour.Properties concrete(DyeColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.8F);
    }

    // ---- Shared property builders (match 26.3's Blocks.logProperties) ----

    /** Wooden block basics: bass note block, wood sounds, burns in lava. Add strength separately. */
    private static BlockBehaviour.Properties wood(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .instrument(NoteBlockInstrument.BASS)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    /** Signs of every kind: walk-through, hardness 1, burn in lava (vanilla's sign properties). */
    private static BlockBehaviour.Properties sign(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .ignitedByLava();
    }

    /** Leaves: same as every vanilla leaves block, only the map color differs. */
    private static BlockBehaviour.Properties leaves(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS) // 26.3 has new leaf sounds that 1.21.1 doesn't have
                .noOcclusion()
                .isValidSpawn((state, level, pos, type) -> type == EntityType.OCELOT || type == EntityType.PARROT)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state, level, pos) -> false);
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
