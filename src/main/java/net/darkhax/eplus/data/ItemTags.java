package net.darkhax.eplus.data;

import net.darkhax.eplus.registry.ModItems;
import net.darkhax.eplus.registry.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(DataGenerator generator, BlockTagsProvider blockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.DECORATIVE_BOOKS_ITEM).add(
                ModItems.DECORATIVE_BOOK_DEFAULT.get(),
                ModItems.DECORATIVE_BOOK_VANILLA.get(),
                ModItems.DECORATIVE_BOOK_PRISMARINE.get(),
                ModItems.DECORATIVE_BOOK_NETHER.get(),
                ModItems.DECORATIVE_BOOK_TARTARITE.get(),
                ModItems.DECORATIVE_BOOK_WHITE.get(),
                ModItems.DECORATIVE_BOOK_METAL.get()
        );
    }
}
