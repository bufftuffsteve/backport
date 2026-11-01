package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.block.LeafLitterBlock;
import com.buffsteve.autumnbackport.block.PoplarLeavesBlock;
import com.buffsteve.autumnbackport.block.PoplarSaplingBlock;
import com.buffsteve.autumnbackport.block.RedShrubBlock;
import com.buffsteve.autumnbackport.block.ShelfMushroomBlock;
import com.buffsteve.autumnbackport.block.StrawBedBlock;
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
import com.buffsteve.autumnbackport.platform.Platform;
import com.buffsteve.autumnbackport.platform.ItemLikeEntry;

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
 *
 * A few vanilla block classes (stairs, doors, trapdoors, pressure plates, buttons) have
 * protected constructors on 1.21.1; "new X(...) {}" makes a tiny subclass to get around that.
 */
public final class ModBlocks {
    public static final String VANILLA = ModRegistry.VANILLA;

    // ---- Poplar wood set ----

    private static final MapColor POPLAR_COLOR = MapColor.COLOR_LIGHT_GRAY;

    public static final ItemLikeEntry<Block> POPLAR_PLANKS = ModRegistry.simpleBlock(
            "poplar_planks", wood(POPLAR_COLOR).strength(2.0F, 3.0F));

    // Stripped versions are registered first so the unstripped ones can point at them.
    public static final ItemLikeEntry<RotatedPillarBlock> STRIPPED_POPLAR_LOG = ModRegistry.block(
            "stripped_poplar_log", () -> new RotatedPillarBlock(log(POPLAR_COLOR, POPLAR_COLOR)));

    public static final ItemLikeEntry<RotatedPillarBlock> STRIPPED_POPLAR_WOOD = ModRegistry.block(
            "stripped_poplar_wood", () -> new RotatedPillarBlock(wood(POPLAR_COLOR).strength(2.0F)));

    // Logs turn into their stripped versions with an axe; see ModStripping.
    // Log: light gray rings on the cut ends, podzol-brown bark on the sides.
    public static final ItemLikeEntry<RotatedPillarBlock> POPLAR_LOG = ModRegistry.block(
            "poplar_log", () -> new RotatedPillarBlock(log(POPLAR_COLOR, MapColor.PODZOL)));

    public static final ItemLikeEntry<RotatedPillarBlock> POPLAR_WOOD = ModRegistry.block(
            "poplar_wood", () -> new RotatedPillarBlock(wood(MapColor.PODZOL).strength(2.0F)));

    public static final ItemLikeEntry<StairBlock> POPLAR_STAIRS = ModRegistry.block(
            "poplar_stairs", () -> new StairBlock(POPLAR_PLANKS.get().defaultBlockState(), wood(POPLAR_COLOR).strength(2.0F, 3.0F)) {});

    public static final ItemLikeEntry<SlabBlock> POPLAR_SLAB = ModRegistry.block(
            "poplar_slab", () -> new SlabBlock(wood(POPLAR_COLOR).strength(2.0F, 3.0F)));

    public static final ItemLikeEntry<FenceBlock> POPLAR_FENCE = ModRegistry.block(
            "poplar_fence", () -> new FenceBlock(wood(POPLAR_COLOR).strength(2.0F, 3.0F)));

    public static final ItemLikeEntry<FenceGateBlock> POPLAR_FENCE_GATE = ModRegistry.block(
            "poplar_fence_gate", () -> new FenceGateBlock(wood(POPLAR_COLOR).forceSolidOn().strength(2.0F, 3.0F), ModWoodTypes.POPLAR));

    public static final ItemLikeEntry<DoorBlock> POPLAR_DOOR = ModRegistry.block(
            "poplar_door", () -> new DoorBlock(wood(POPLAR_COLOR).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY), ModWoodTypes.POPLAR_SET) {});

    public static final ItemLikeEntry<TrapDoorBlock> POPLAR_TRAPDOOR = ModRegistry.block(
            "poplar_trapdoor", () -> new TrapDoorBlock(wood(POPLAR_COLOR).strength(3.0F).noOcclusion().isValidSpawn((state, level, pos, type) -> false), ModWoodTypes.POPLAR_SET) {});

    public static final ItemLikeEntry<PressurePlateBlock> POPLAR_PRESSURE_PLATE = ModRegistry.block(
            "poplar_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    wood(POPLAR_COLOR).forceSolidOn().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), ModWoodTypes.POPLAR_SET) {});

    // Wooden buttons stay pressed for 30 ticks (1.5 seconds), and arrows can press them.
    public static final ItemLikeEntry<ButtonBlock> POPLAR_BUTTON = ModRegistry.block(
            "poplar_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY),
                    ModWoodTypes.POPLAR_SET, 30, true) {});

    // ---- Signs ----
    // Text and look come from the vanilla sign renderers, using
    // textures/entity/signs/poplar.png and textures/entity/signs/hanging/poplar.png.

    public static final ItemLikeEntry<StandingSignBlock> POPLAR_SIGN = ModRegistry.block(
            "poplar_sign", () -> new StandingSignBlock(sign(POPLAR_COLOR), ModWoodTypes.POPLAR));
    // Wall versions drop the same item as the standing ones.
    public static final ItemLikeEntry<WallSignBlock> POPLAR_WALL_SIGN = ModRegistry.block(
            "poplar_wall_sign", () -> new WallSignBlock(sign(POPLAR_COLOR).dropsLike(POPLAR_SIGN.get()), ModWoodTypes.POPLAR));
    public static final ItemLikeEntry<CeilingHangingSignBlock> POPLAR_HANGING_SIGN = ModRegistry.block(
            "poplar_hanging_sign", () -> new CeilingHangingSignBlock(sign(POPLAR_COLOR), ModWoodTypes.POPLAR));
    public static final ItemLikeEntry<WallHangingSignBlock> POPLAR_WALL_HANGING_SIGN = ModRegistry.block(
            "poplar_wall_hanging_sign", () -> new WallHangingSignBlock(sign(POPLAR_COLOR).dropsLike(POPLAR_HANGING_SIGN.get()), ModWoodTypes.POPLAR));

    // ---- Leaves and sapling ----

    // Three separate leaf blocks with fixed colors (no biome tint), each with its own falling leaf.
    public static final ItemLikeEntry<PoplarLeavesBlock> RED_POPLAR_LEAVES = ModRegistry.block(
            "red_poplar_leaves", () -> new PoplarLeavesBlock(ModParticles.RED_POPLAR_LEAVES, leaves(MapColor.COLOR_RED)));
    public static final ItemLikeEntry<PoplarLeavesBlock> ORANGE_POPLAR_LEAVES = ModRegistry.block(
            "orange_poplar_leaves", () -> new PoplarLeavesBlock(ModParticles.ORANGE_POPLAR_LEAVES, leaves(MapColor.COLOR_ORANGE)));
    public static final ItemLikeEntry<PoplarLeavesBlock> YELLOW_POPLAR_LEAVES = ModRegistry.block(
            "yellow_poplar_leaves", () -> new PoplarLeavesBlock(ModParticles.YELLOW_POPLAR_LEAVES, leaves(MapColor.COLOR_YELLOW)));

    // 26.3 really does give the sapling the light gray "metal" map color.
    // Grows into a red, orange or yellow poplar at random.
    public static final ItemLikeEntry<PoplarSaplingBlock> POPLAR_SAPLING = ModRegistry.block(
            "poplar_sapling", () -> new PoplarSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).noCollission().randomTicks().instabreak()
                    .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));

    public static final ItemLikeEntry<FlowerPotBlock> POTTED_POPLAR_SAPLING = ModRegistry.block(
            "potted_poplar_sapling", () -> Platform.INSTANCE.pottedPlant(POPLAR_SAPLING,
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));

    // ---- Forest floor plants ----

    // Grows sideways out of tree trunks (and any solid wall). Bouncy, like a small trampoline.
    public static final ItemLikeEntry<ShelfMushroomBlock> SHELF_MUSHROOM = ModRegistry.block(
            "shelf_mushroom", () -> new ShelfMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW).instabreak().noOcclusion()
                    .sound(SoundType.FUNGUS) // 26.3 has its own shelf mushroom sounds that 1.21.1 doesn't have
                    .pushReaction(PushReaction.DESTROY)));

    // Low red bush for the forest floor. Walk-through, burns easily, replaced when you place a block in it.
    public static final ItemLikeEntry<RedShrubBlock> RED_SHRUB = ModRegistry.block(
            "red_shrub", () -> new RedShrubBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CRIMSON_NYLIUM).replaceable().noCollission().instabreak()
                    .sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    // Fallen leaves (from 1.21.5). Walk-through, replaced when you build over it, burns.
    public static final ItemLikeEntry<LeafLitterBlock> LEAF_LITTER = ModRegistry.block(
            "leaf_litter", () -> new LeafLitterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).replaceable().noCollission()
                    .sound(SoundType.PINK_PETALS) // 1.21.5 has its own leaf litter sounds
                    .ignitedByLava().pushReaction(PushReaction.DESTROY)));

    // ---- Straw bed ----

    // Single-use bed that doesn't set your spawn. Hay sounds; pistons break it.
    public static final ItemLikeEntry<StrawBedBlock> STRAW_BED = ModRegistry.block(
            "straw_bed", () -> new StrawBedBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).sound(SoundType.GRASS) // 26.3 has its own straw bed sounds
                    .strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY))); // 26.3 "POPPED"; DESTROY is the 1.21.1 equivalent

    // ---- Wool and concrete stairs and slabs (16 colors each) ----
    // Same properties as the full block: wool is soft and burns, concrete needs a pickaxe.

    public static final Map<DyeColor, ItemLikeEntry<StairBlock>> WOOL_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, ItemLikeEntry<SlabBlock>> WOOL_SLABS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, ItemLikeEntry<StairBlock>> CONCRETE_STAIRS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, ItemLikeEntry<SlabBlock>> CONCRETE_SLABS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String wool = color.getName() + "_wool";
            String concrete = color.getName() + "_concrete";
            WOOL_STAIRS.put(color, ModRegistry.block(wool + "_stairs",
                    () -> new StairBlock(vanillaBlock(wool).defaultBlockState(), wool(color)) {}));
            WOOL_SLABS.put(color, ModRegistry.block(wool + "_slab", () -> new SlabBlock(wool(color))));
            CONCRETE_STAIRS.put(color, ModRegistry.block(concrete + "_stairs",
                    () -> new StairBlock(vanillaBlock(concrete).defaultBlockState(), concrete(color)) {}));
            CONCRETE_SLABS.put(color, ModRegistry.block(concrete + "_slab", () -> new SlabBlock(concrete(color))));
        }
    }

    /** The vanilla full block these stairs or slabs are cut from, e.g. "red_wool" -> minecraft:red_wool. */
    public static Block vanillaBlock(String name) {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation(name));
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
