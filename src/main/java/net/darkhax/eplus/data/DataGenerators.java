package net.darkhax.eplus.data;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = EnchantingPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            BlockTags blockTags = new BlockTags(generator, EnchantingPlus.ID, helper);
            generator.addProvider(blockTags);
            generator.addProvider(new ItemTags(generator, blockTags, EnchantingPlus.ID, helper));
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new LootTables(generator));
        }
    }
}
