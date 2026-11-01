package com.buffsteve.autumnbackport.forge;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.client.AutumnBackportClientCommon;
import com.buffsteve.autumnbackport.client.CushionModel;
import com.buffsteve.autumnbackport.client.CushionRenderer;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.registry.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only setup. Never loads on a dedicated server.
 * Renderers, particles and colors get hooked up here.
 */
@Mod.EventBusSubscriber(modid = AutumnBackport.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class AutumnBackportForgeClient {
    @SubscribeEvent
    static void registerParticles(RegisterParticleProvidersEvent event) {
        for (var type : new SimpleParticleType[] {ModParticles.RED_POPLAR_LEAVES.get(),
                ModParticles.ORANGE_POPLAR_LEAVES.get(), ModParticles.YELLOW_POPLAR_LEAVES.get()}) {
            event.registerSpriteSet(type, AutumnBackportClientCommon::leafParticle);
        }
    }

    @SubscribeEvent
    static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(AutumnBackportClientCommon::registerSignMaterials);
    }

    @SubscribeEvent
    static void registerColorResolvers(RegisterColorHandlersEvent.ColorResolvers event) {
        event.register(AutumnBackportClientCommon.DRY_FOLIAGE);
    }

    @SubscribeEvent
    static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(AutumnBackportClientCommon.LEAF_LITTER_COLOR, ModBlocks.LEAF_LITTER.get());
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CushionModel.LAYER, CushionModel::createBodyLayer);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSHION.get(), CushionRenderer::new);
    }

    private AutumnBackportForgeClient() {}
}
