package net.darkhax.eplus.data;

import net.darkhax.eplus.registry.ModBlocks;
import net.darkhax.eplus.registry.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.DECORATIVE_BOOKS_BLOCK).add(
                ModBlocks.DECORATIVE_BOOK_DEFAULT.get(),
                ModBlocks.DECORATIVE_BOOK_VANILLA.get(),
                ModBlocks.DECORATIVE_BOOK_PRISMARINE.get(),
                ModBlocks.DECORATIVE_BOOK_NETHER.get(),
                ModBlocks.DECORATIVE_BOOK_TARTARITE.get(),
                ModBlocks.DECORATIVE_BOOK_WHITE.get(),
                ModBlocks.DECORATIVE_BOOK_METAL.get()
        );
    }
}
