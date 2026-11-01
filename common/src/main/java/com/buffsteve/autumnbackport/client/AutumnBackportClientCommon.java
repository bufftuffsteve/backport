package com.buffsteve.autumnbackport.client;

import com.buffsteve.autumnbackport.registry.ModWoodTypes;
import com.buffsteve.autumnbackport.world.ModDappledForest;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ColorResolver;

/** Client pieces every loader hooks up the same way. */
public final class AutumnBackportClientCommon {
    /**
     * Leaf litter is tinted by the biome's "dry foliage" color, a 1.21.5 feature 1.21.1 doesn't have.
     * In the Dappled Forest it uses 26.3's color for that biome; everywhere else, 26.3's default.
     * The game only keeps color caches for resolvers it knows about; each loader registers this one
     * (an unregistered resolver crashes chunk rendering).
     */
    public static final ColorResolver DRY_FOLIAGE = (biome, x, z) -> biome.getFoliageColor() == ModDappledForest.FOLIAGE_COLOR
            ? ModDappledForest.DRY_FOLIAGE_COLOR : ModDappledForest.DEFAULT_DRY_FOLIAGE_COLOR;

    /** Leaf litter's block color. */
    public static final BlockColor LEAF_LITTER_COLOR = (state, level, pos, tint) -> level != null && pos != null
            ? level.getBlockTint(pos, DRY_FOLIAGE) : ModDappledForest.DEFAULT_DRY_FOLIAGE_COLOR;

    /** Makes sure the sign renderers know where poplar's sign textures are. */
    public static void registerSignMaterials() {
        Sheets.SIGN_MATERIALS.computeIfAbsent(ModWoodTypes.POPLAR, type ->
                new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/poplar")));
        Sheets.HANGING_SIGN_MATERIALS.computeIfAbsent(ModWoodTypes.POPLAR, type ->
                new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/hanging/poplar")));
    }

    /** Falling poplar leaves, one sprite set per color. */
    public static ParticleProvider<SimpleParticleType> leafParticle(SpriteSet sprites) {
        return (options, level, x, y, z, dx, dy, dz) -> new PoplarLeafParticle(level, x, y, z, sprites);
    }

    private AutumnBackportClientCommon() {}
}
