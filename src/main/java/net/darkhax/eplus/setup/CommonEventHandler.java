package net.darkhax.eplus.setup;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.ParametersAreNonnullByDefault;


@Mod.EventBusSubscriber(modid = EnchantingPlus.ID)
@ParametersAreNonnullByDefault
public class CommonEventHandler {
    public static void init(final FMLCommonSetupEvent event) {
        NetworkHandler.setup();
    }
}
