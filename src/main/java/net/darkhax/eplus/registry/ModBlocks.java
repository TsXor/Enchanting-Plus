package net.darkhax.eplus.registry;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.api.BookTexture;
import net.darkhax.eplus.block.BlockAdvancedTable;
import net.darkhax.eplus.block.BlockBookDecoration;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


public class ModBlocks {
    private static Supplier<BlockBookDecoration> decorativeBookBuilderOf(RegistryObject<BookTexture> textureReg) {
        return () -> new BlockBookDecoration() {
            @Override
            public BookTexture getBookTexture() {
                return textureReg.get();
            }
        };
    }

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, EnchantingPlus.ID);

    public static final RegistryObject<Block> ADVANCED_ENCHANTING_TABLE
            = BLOCKS.register("advanced_table", BlockAdvancedTable::new);

    public static final RegistryObject<Block> DECORATIVE_BOOK_DEFAULT
            = BLOCKS.register("decorative_book_default", decorativeBookBuilderOf(ModBookTextures.DEFAULT));
    public static final RegistryObject<Block> DECORATIVE_BOOK_VANILLA
            = BLOCKS.register("decorative_book_vanilla", decorativeBookBuilderOf(ModBookTextures.VANILLA));
    public static final RegistryObject<Block> DECORATIVE_BOOK_PRISMARINE
            = BLOCKS.register("decorative_book_prismarine", decorativeBookBuilderOf(ModBookTextures.PRISMARINE));
    public static final RegistryObject<Block> DECORATIVE_BOOK_NETHER
            = BLOCKS.register("decorative_book_nether", decorativeBookBuilderOf(ModBookTextures.NETHER));
    public static final RegistryObject<Block> DECORATIVE_BOOK_TARTARITE
            = BLOCKS.register("decorative_book_tartarite", decorativeBookBuilderOf(ModBookTextures.TARTARITE));
    public static final RegistryObject<Block> DECORATIVE_BOOK_WHITE
            = BLOCKS.register("decorative_book_white", decorativeBookBuilderOf(ModBookTextures.WHITE));
    public static final RegistryObject<Block> DECORATIVE_BOOK_METAL
            = BLOCKS.register("decorative_book_metal", decorativeBookBuilderOf(ModBookTextures.METAL));
}
