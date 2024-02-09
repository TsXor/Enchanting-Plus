package net.darkhax.eplus.registry;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;


/**
 * Fun fact: this is not a registry.
 */
public class ModTags {
    private static ITag.INamedTag<Item> modItemTag(String path) {
        return ItemTags.bind(EnchantingPlus.ID + ":" + path);
    }

    private static ITag.INamedTag<Block> modBlockTag(String path) {
        return BlockTags.bind(EnchantingPlus.ID + ":" + path);
    }

    private static ITag.INamedTag<EntityType<?>> modEntityTag(String path) {
        return EntityTypeTags.bind(EnchantingPlus.ID + ":" + path);
    }

    public static final ITag.INamedTag<Block> DECORATIVE_BOOKS_BLOCK = modBlockTag("decorative_books");
    public static final ITag.INamedTag<Item> DECORATIVE_BOOKS_ITEM = modItemTag("decorative_books");
}
