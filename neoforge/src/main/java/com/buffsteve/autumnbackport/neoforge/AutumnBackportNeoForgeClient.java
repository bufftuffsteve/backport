package com.buffsteve.autumnbackport.neoforge;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.client.AutumnBackportClientCommon;
import com.buffsteve.autumnbackport.client.CushionModel;
import com.buffsteve.autumnbackport.client.CushionRenderer;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.registry.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

/**
 * Client-only entry point. Never loads on a dedicated server.
 * Renderers, particles and colors get hooked up here.
 */
@Mod(value = AutumnBackport.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AutumnBackport.MODID, value = Dist.CLIENT)
public class AutumnBackportNeoForgeClient {
    public AutumnBackportNeoForgeClient(ModContainer container) {
    }

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
}
