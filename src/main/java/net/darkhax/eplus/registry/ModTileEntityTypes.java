package net.darkhax.eplus.registry;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EnchantingPlus.ID);

    public static final RegistryObject<TileEntityType<TileEntityAdvancedTable>> ADVANCED_ENCHANTING_TABLE
            = TILE_ENTITY_TYPES.register("advanced_table", () -> TileEntityType.Builder.of(
                    TileEntityAdvancedTable::new, ModBlocks.ADVANCED_ENCHANTING_TABLE.get()
            ).build(null));

    public static final RegistryObject<TileEntityType<TileEntityDecoration>> DECORATIVE_BOOK
            = TILE_ENTITY_TYPES.register("decorative_book", () -> TileEntityType.Builder.of(
                    TileEntityDecoration::new,
                        ModBlocks.DECORATIVE_BOOK_DEFAULT.get(),
                        ModBlocks.DECORATIVE_BOOK_VANILLA.get(),
                        ModBlocks.DECORATIVE_BOOK_PRISMARINE.get(),
                        ModBlocks.DECORATIVE_BOOK_NETHER.get(),
                        ModBlocks.DECORATIVE_BOOK_TARTARITE.get(),
                        ModBlocks.DECORATIVE_BOOK_WHITE.get(),
                        ModBlocks.DECORATIVE_BOOK_METAL.get()
            ).build(null));
}
