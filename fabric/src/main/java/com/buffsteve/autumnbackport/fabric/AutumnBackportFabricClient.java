package com.buffsteve.autumnbackport.fabric;

import com.buffsteve.autumnbackport.client.AutumnBackportClientCommon;
import com.buffsteve.autumnbackport.client.CushionModel;
import com.buffsteve.autumnbackport.client.CushionRenderer;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.registry.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

/**
 * Client entry point. Renderers, particles and colors get hooked up here.
 * The leaf litter color cache is added by ClientLevelMixin.
 */
public class AutumnBackportFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
        particles.register(ModParticles.RED_POPLAR_LEAVES.get(), AutumnBackportClientCommon::leafParticle);
        particles.register(ModParticles.ORANGE_POPLAR_LEAVES.get(), AutumnBackportClientCommon::leafParticle);
        particles.register(ModParticles.YELLOW_POPLAR_LEAVES.get(), AutumnBackportClientCommon::leafParticle);

        AutumnBackportClientCommon.registerSignMaterials();
        ColorProviderRegistry.BLOCK.register(AutumnBackportClientCommon.LEAF_LITTER_COLOR, ModBlocks.LEAF_LITTER.get());

        EntityModelLayerRegistry.registerModelLayer(CushionModel.LAYER, CushionModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.CUSHION.get(), CushionRenderer::new);

        // Fabric ignores the "render_type" in block model files, so see-through blocks are listed here.
        BlockRenderLayerMap layers = BlockRenderLayerMap.INSTANCE;
        layers.putBlocks(RenderType.cutoutMipped(),
                ModBlocks.RED_POPLAR_LEAVES.get(), ModBlocks.ORANGE_POPLAR_LEAVES.get(), ModBlocks.YELLOW_POPLAR_LEAVES.get());
        layers.putBlocks(RenderType.cutout(),
                ModBlocks.POPLAR_DOOR.get(), ModBlocks.POPLAR_TRAPDOOR.get(),
                ModBlocks.POPLAR_SAPLING.get(), ModBlocks.POTTED_POPLAR_SAPLING.get(),
                ModBlocks.SHELF_MUSHROOM.get(), ModBlocks.RED_SHRUB.get(), ModBlocks.LEAF_LITTER.get(),
                ModBlocks.STRAW_BED.get());
    }
}
