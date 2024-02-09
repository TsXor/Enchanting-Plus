package net.darkhax.eplus.setup;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityAdvancedTableRenderer;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityDecorationRenderer;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.registry.ModBookTextures;
import net.darkhax.eplus.registry.ModContainerTypes;
import net.darkhax.eplus.registry.ModTileEntityTypes;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = EnchantingPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onStitchEvent(TextureStitchEvent.Pre event) {
        ResourceLocation stitching = event.getMap().location();
        if (stitching.equals(AtlasTexture.LOCATION_BLOCKS)) {
            // register books
            ModBookTextures.BOOK_TEXTURES.getEntries().stream().map(RegistryObject::get).forEach(
                    bookTexture -> event.addSprite(bookTexture.material.texture())
            );
        }
    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.ADVANCED_ENCHANTING_TABLE.get(),
                TileEntityAdvancedTableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.DECORATIVE_BOOK.get(),
                TileEntityDecorationRenderer::new);

        ScreenManager.register(ModContainerTypes.ADVANCED_ENCHANTING_TABLE.get(), GuiAdvancedTable::new);
    }
}
