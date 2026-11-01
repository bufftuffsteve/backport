package com.buffsteve.autumnbackport.neoforge.datagen;

import com.buffsteve.autumnbackport.AutumnBackport;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Runs when you launch the "data" Gradle task (runData).
 * Writes JSON files into src/generated/resources, which the build already includes.
 */
// NeoForge detects that GatherDataEvent belongs on the mod event bus, so no "bus =" setting is needed.
@EventBusSubscriber(modid = AutumnBackport.MODID)
public final class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper files = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        // Client side: models, blockstates, names
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, files));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
        generator.addProvider(event.includeClient(), new ModParticleProvider(output, files));

        // Server side: loot, tags, recipes
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)),
                lookup));
        BlockTagsProvider blockTags = generator.addProvider(event.includeServer(),
                new ModBlockTagsProvider(output, lookup, files));
        generator.addProvider(event.includeServer(),
                new ModItemTagsProvider(output, lookup, blockTags.contentsGetter(), files));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookup));
        generator.addProvider(event.includeServer(), new ModDataMapProvider(output, lookup));
        generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookup));
    }

    private DataGenerators() {}
}
