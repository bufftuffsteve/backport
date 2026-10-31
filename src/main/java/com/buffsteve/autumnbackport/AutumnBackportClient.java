package com.buffsteve.autumnbackport;

import com.buffsteve.autumnbackport.client.CushionModel;
import com.buffsteve.autumnbackport.client.CushionRenderer;
import com.buffsteve.autumnbackport.client.PoplarLeafParticle;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import com.buffsteve.autumnbackport.registry.ModEntities;
import com.buffsteve.autumnbackport.world.ModDappledForest;
import net.minecraft.world.level.ColorResolver;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import com.buffsteve.autumnbackport.registry.ModWoodTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.buffsteve.autumnbackport.registry.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

/**
 * Client-only entry point. Never loads on a dedicated server.
 * Renderers and particles get hooked up here.
 */
@Mod(value = AutumnBackport.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AutumnBackport.MODID, value = Dist.CLIENT)
public class AutumnBackportClient {
    public AutumnBackportClient(ModContainer container) {
    }

    @SubscribeEvent
    static void registerParticles(RegisterParticleProvidersEvent event) {
        leafParticle(event, ModParticles.RED_POPLAR_LEAVES.get());
        leafParticle(event, ModParticles.ORANGE_POPLAR_LEAVES.get());
        leafParticle(event, ModParticles.YELLOW_POPLAR_LEAVES.get());
    }

    /** Makes sure the sign renderers know where poplar's sign textures are. */
    @SubscribeEvent
    static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.SIGN_MATERIALS.computeIfAbsent(ModWoodTypes.POPLAR, type ->
                    new Material(Sheets.SIGN_SHEET, ResourceLocation.withDefaultNamespace("entity/signs/poplar")));
            Sheets.HANGING_SIGN_MATERIALS.computeIfAbsent(ModWoodTypes.POPLAR, type ->
                    new Material(Sheets.SIGN_SHEET, ResourceLocation.withDefaultNamespace("entity/signs/hanging/poplar")));
        });
    }

    /**
     * Leaf litter is tinted by the biome's "dry foliage" color, a 1.21.5 feature 1.21.1 doesn't have.
     * In the Dappled Forest it uses 26.3's color for that biome; everywhere else, 26.3's default.
     */
    private static final ColorResolver DRY_FOLIAGE = (biome, x, z) -> biome.getFoliageColor() == ModDappledForest.FOLIAGE_COLOR
            ? ModDappledForest.DRY_FOLIAGE_COLOR : ModDappledForest.DEFAULT_DRY_FOLIAGE_COLOR;

    /** The game only keeps color caches for resolvers registered here; an unregistered one crashes chunk rendering. */
    @SubscribeEvent
    static void registerColorResolvers(RegisterColorHandlersEvent.ColorResolvers event) {
        event.register(DRY_FOLIAGE);
    }

    @SubscribeEvent
    static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> level != null && pos != null
                ? level.getBlockTint(pos, DRY_FOLIAGE) : ModDappledForest.DEFAULT_DRY_FOLIAGE_COLOR,
                ModBlocks.LEAF_LITTER.get());
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CushionModel.LAYER, CushionModel::createBodyLayer);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSHION.get(), CushionRenderer::new);
    }

    private static void leafParticle(RegisterParticleProvidersEvent event, ParticleType<SimpleParticleType> type) {
        event.registerSpriteSet(type, sprites -> (options, level, x, y, z, dx, dy, dz) ->
                new PoplarLeafParticle(level, x, y, z, sprites));
    }
}
