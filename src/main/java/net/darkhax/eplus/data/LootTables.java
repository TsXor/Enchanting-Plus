package net.darkhax.eplus.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mcp.MethodsReturnNonnullByDefault;
import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Reference: <a href="https://github.com/KnightMiner/Ceramics/blob/1.16/src/main/java/knightminer/ceramics/datagen/BlockLootTables.java">ceramics:BlockLootTables.java</a>
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LootTables extends LootTableProvider {
    public static class BlockLT extends BlockLootTables {
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream()
                    .filter((block) -> EnchantingPlus.ID.equals(Objects.requireNonNull(block.getRegistryName()).getNamespace()))
                    .toList();
        }

        @Override
        protected void addTables() {
            dropSelf(ModBlocks.ADVANCED_ENCHANTING_TABLE.get());

            dropSelf(ModBlocks.DECORATIVE_BOOK_DEFAULT.get());
            dropSelf(ModBlocks.DECORATIVE_BOOK_VANILLA.get());
            dropSelf(ModBlocks.DECORATIVE_BOOK_PRISMARINE.get());
            dropSelf(ModBlocks.DECORATIVE_BOOK_NETHER.get());
            dropSelf(ModBlocks.DECORATIVE_BOOK_TARTARITE.get());
            dropSelf(ModBlocks.DECORATIVE_BOOK_WHITE.get());
            dropSelf(ModBlocks.DECORATIVE_BOOK_METAL.get());
        }
    }

    public LootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    public String getName() {
        return "Enchanting-Plus Loot Tables";
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(BlockLT::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((loc, table) -> LootTableManager.validate(validationtracker, loc, table));
        // Remove vanilla's tables, which we also loaded so we can redirect stuff to them.
        // This ensures the remaining generator logic doesn't write those to files.
        map.keySet().removeIf((loc) -> !loc.getNamespace().equals(EnchantingPlus.ID));
    }
}
