package net.darkhax.eplus.registry;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.api.BookTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;


/**
 * This is a custom registry of this mod that holds all available book texture variants.
 * <br/>Referred to <a href="https://github.com/railcraft-reborn/railcraft/blob/1.16.5/src/main/java/mods/railcraft/world/level/block/track/TrackTypes.java">a class from railcraft-reborn</a> on how to make a custom registry.
 * <br/>Design goal of this is to enable other mods to add their book easily.
 */
@SuppressWarnings("unused")
public class ModBookTextures {
    public static BookTexture getValueOfName(String name) {
        return REGISTRY.get().getValue(new ResourceLocation(EnchantingPlus.ID, name));
    }

    public static final DeferredRegister<BookTexture> BOOK_TEXTURES
            = DeferredRegister.create(BookTexture.class, EnchantingPlus.ID);
    public static final Lazy<IForgeRegistry<BookTexture>> REGISTRY =
            Lazy.of(BOOK_TEXTURES.makeRegistry("book_texture", RegistryBuilder::new));

    public static final RegistryObject<BookTexture> DEFAULT
            = BOOK_TEXTURES.register("eplus", BookTexture.builderOfPath("eplus:entity/enchantingplus_book"));
    public static final RegistryObject<BookTexture> VANILLA
            = BOOK_TEXTURES.register("vanilla", BookTexture.builderOfPath("minecraft:entity/enchanting_table_book"));
    public static final RegistryObject<BookTexture> PRISMARINE
            = BOOK_TEXTURES.register("prismarine", BookTexture.builderOfPath("eplus:entity/prismarine_book"));
    public static final RegistryObject<BookTexture> NETHER
            = BOOK_TEXTURES.register("nether", BookTexture.builderOfPath("eplus:entity/nether_book"));
    public static final RegistryObject<BookTexture> TARTARITE
            = BOOK_TEXTURES.register("tartarite", BookTexture.builderOfPath("eplus:entity/tartarite_book"));
    public static final RegistryObject<BookTexture> WHITE
            = BOOK_TEXTURES.register("white", BookTexture.builderOfPath("eplus:entity/white_book"));
    public static final RegistryObject<BookTexture> METAL
            = BOOK_TEXTURES.register("metal", BookTexture.builderOfPath("eplus:entity/metal_book"));
}
