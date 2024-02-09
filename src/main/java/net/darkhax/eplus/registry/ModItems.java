package net.darkhax.eplus.registry;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.item.ItemTableUpgrade;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnchantingPlus.ID);

    public static final RegistryObject<BlockItem> ADVANCED_ENCHANTING_TABLE = ITEMS.register("advanced_table",
            () -> new BlockItem(ModBlocks.ADVANCED_ENCHANTING_TABLE.get(), new Item.Properties().stacksTo(16).tab(EnchantingPlus.ITEM_GROUP)));

    public static final RegistryObject<ItemTableUpgrade> TABLE_UPGRADE = ITEMS.register("table_upgrade", ItemTableUpgrade::new);

    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_DEFAULT = ITEMS.register("decorative_book_default",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_DEFAULT.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_VANILLA = ITEMS.register("decorative_book_vanilla",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_VANILLA.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_PRISMARINE = ITEMS.register("decorative_book_prismarine",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_PRISMARINE.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_NETHER = ITEMS.register("decorative_book_nether",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_NETHER.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_TARTARITE = ITEMS.register("decorative_book_tartarite",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_TARTARITE.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_WHITE = ITEMS.register("decorative_book_white",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_WHITE.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> DECORATIVE_BOOK_METAL = ITEMS.register("decorative_book_metal",
            () -> new BlockItem(ModBlocks.DECORATIVE_BOOK_METAL.get(), new Item.Properties().tab(EnchantingPlus.ITEM_GROUP)));
}
