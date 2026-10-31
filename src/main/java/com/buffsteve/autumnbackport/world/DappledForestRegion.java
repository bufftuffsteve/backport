package com.buffsteve.autumnbackport.world;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

/**
 * Adds the Dappled Forest to overworld generation through TerraBlender.
 *
 * TerraBlender splits the world into large "regions". This region is vanilla's own biome
 * layout with one change, the same one 26.3 makes: in cool, dry areas where vanilla would
 * place a plains "variant" (the spots it leaves empty, which fall back to plains), you get
 * a Dappled Forest instead. Inside this region the forest is exactly as common as in 26.3.
 */
public class DappledForestRegion extends Region {
    /**
     * How much of the world uses this region, compared with TerraBlender's vanilla region (10 by default)
     * and other mods' regions. Higher = Dappled Forests in more of the world.
     */
    public static final int WEIGHT = 10;

    // Vanilla 1.21.1's OverworldBiomeBuilder bands: temperature level 1 ("cool") and humidity level 0 ("driest").
    private static final Climate.Parameter COOL = Climate.Parameter.span(-0.45F, -0.15F);
    private static final Climate.Parameter DRIEST = Climate.Parameter.span(-1.0F, -0.35F);

    public DappledForestRegion() {
        super(ResourceLocation.fromNamespaceAndPath(AutumnBackport.MODID, "overworld"), RegionType.OVERWORLD, WEIGHT);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> swapper = entry -> mapper.accept(isPlainsVariantSlot(entry)
                ? Pair.of(entry.getFirst(), ModDappledForest.DAPPLED_FOREST)
                : entry);
        try {
            ADD_VANILLA_BIOMES.invoke(new OverworldBiomeBuilder(), swapper);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Couldn't read vanilla's overworld biome layout", e);
        }
    }

    /** Vanilla's method that lists the overworld biome layout. It isn't public, so it's looked up once here. */
    private static final Method ADD_VANILLA_BIOMES =
            ObfuscationReflectionHelper.findMethod(OverworldBiomeBuilder.class, "addBiomes", Consumer.class);

    /** Plains that vanilla placed only because its variant slot for cool + driest was empty. */
    private static boolean isPlainsVariantSlot(Pair<Climate.ParameterPoint, ResourceKey<Biome>> entry) {
        Climate.ParameterPoint point = entry.getFirst();
        return entry.getSecond() == Biomes.PLAINS
                && point.temperature().equals(COOL)
                && point.humidity().equals(DRIEST)
                && point.weirdness().max() >= 0L;
    }

    /** Call once during common setup. */
    public static void register() {
        Regions.register(new DappledForestRegion());

        // 26.3's ground rule for the biome: patches of coarse dirt on the surface where the
        // "small_patch" noise is above 1.2. Everything else falls through to vanilla's rules.
        SurfaceRules.RuleSource coarseDirtPatches = SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModDappledForest.DAPPLED_FOREST),
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(),
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0),
                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ModDappledForest.SMALL_PATCH, 1.2),
                                                SurfaceRules.state(Blocks.COARSE_DIRT.defaultBlockState()))))));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, AutumnBackport.MODID, coarseDirtPatches);
    }
}
