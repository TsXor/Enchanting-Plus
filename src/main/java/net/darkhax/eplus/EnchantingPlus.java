package net.darkhax.eplus;

import net.darkhax.eplus.creativetab.EPItemGroup;
import net.darkhax.eplus.registry.*;
import net.darkhax.eplus.setup.CommonEventHandler;
import net.darkhax.eplus.setup.ConfigurationHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(EnchantingPlus.ID)
@Mod.EventBusSubscriber(modid = EnchantingPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EnchantingPlus {
    public static final String ID = "eplus";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final EPItemGroup ITEM_GROUP = new EPItemGroup(EnchantingPlus.ID);

    public EnchantingPlus() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigurationHandler.SERVER_CONFIG);

        modEventBus.addListener(CommonEventHandler::init);

        ModBookTextures.BOOK_TEXTURES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}