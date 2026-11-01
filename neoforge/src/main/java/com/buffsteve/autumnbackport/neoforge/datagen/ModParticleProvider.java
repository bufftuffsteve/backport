package com.buffsteve.autumnbackport.neoforge.datagen;

import com.buffsteve.autumnbackport.registry.ModParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

/**
 * Tells each falling-leaf particle which pictures to use: textures/particle/<color>_poplar_1..4.png.
 * One of the four is picked at random for each leaf, like vanilla's 26.3 particles.
 */
public class ModParticleProvider extends ParticleDescriptionProvider {
    public ModParticleProvider(PackOutput output, ExistingFileHelper files) {
        super(output, files);
    }

    @Override
    protected void addDescriptions() {
        leaf(ModParticles.RED_POPLAR_LEAVES.get(), "red_poplar");
        leaf(ModParticles.ORANGE_POPLAR_LEAVES.get(), "orange_poplar");
        leaf(ModParticles.YELLOW_POPLAR_LEAVES.get(), "yellow_poplar");
    }

    private void leaf(ParticleType<?> type, String textureBase) {
        spriteSet(type,
                ResourceLocation.withDefaultNamespace(textureBase + "_1"),
                ResourceLocation.withDefaultNamespace(textureBase + "_2"),
                ResourceLocation.withDefaultNamespace(textureBase + "_3"),
                ResourceLocation.withDefaultNamespace(textureBase + "_4"));
    }
}
