package net.darkhax.eplus.data;

import mcp.MethodsReturnNonnullByDefault;
import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.registry.ModItems;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        registerTableAndUpgrade(consumer);

        registerDecorativeBook(consumer, ModItems.DECORATIVE_BOOK_DEFAULT, Tags.Items.DUSTS_GLOWSTONE);
        registerDecorativeBook(consumer, ModItems.DECORATIVE_BOOK_VANILLA, Items.BOOK);
        registerDecorativeBook(consumer, ModItems.DECORATIVE_BOOK_PRISMARINE, Tags.Items.GEMS_PRISMARINE);
        registerDecorativeBook(consumer, ModItems.DECORATIVE_BOOK_NETHER, Tags.Items.INGOTS_NETHER_BRICK);
        /* TODO: add tartarite book recipe */
        registerDecorativeBook(consumer, ModItems.DECORATIVE_BOOK_WHITE, Items.PAPER);
        registerDecorativeBook(consumer, ModItems.DECORATIVE_BOOK_METAL, Tags.Items.INGOTS_IRON);
    }

    private void registerTableAndUpgrade(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.TABLE_UPGRADE.get())
                .pattern("gbg")
                .pattern("o o")
                .pattern("geg")
                .define('b', Items.WRITABLE_BOOK)
                .define('o', Tags.Items.OBSIDIAN)
                .define('e', Items.ENDER_EYE)
                .define('g', Tags.Items.INGOTS_GOLD)
                .unlockedBy("have_aet", InventoryChangeTrigger.Instance.hasItems(ModItems.ADVANCED_ENCHANTING_TABLE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.ADVANCED_ENCHANTING_TABLE.get())
                .pattern("gbg")
                .pattern("oto")
                .pattern("geg")
                .define('b', Items.WRITABLE_BOOK)
                .define('o', Tags.Items.OBSIDIAN)
                .define('e', Items.ENDER_EYE)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('t', Items.ENCHANTING_TABLE)
                .unlockedBy("have_et", InventoryChangeTrigger.Instance.hasItems(Items.ENCHANTING_TABLE))
                .save(consumer);
        // TODO: make it really upgrade
        ShapelessRecipeBuilder.shapeless(ModItems.ADVANCED_ENCHANTING_TABLE.get())
                .requires(Items.ENCHANTING_TABLE)
                .requires(ModItems.TABLE_UPGRADE.get())
                .unlockedBy("have_aet", InventoryChangeTrigger.Instance.hasItems(ModItems.ADVANCED_ENCHANTING_TABLE.get()))
                .save(consumer, new ResourceLocation(EnchantingPlus.ID, "upgrade_aet"));
    }

    private void registerDecorativeBook(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends Item> targetItemReg, Ingredient subIngredient) {
        ShapedRecipeBuilder.shaped(targetItemReg.get())
                .pattern(" g ")
                .pattern("gbg")
                .pattern(" g ")
                .define('g', subIngredient)
                .define('b', Items.ENCHANTED_BOOK)
                .unlockedBy("have_aet", InventoryChangeTrigger.Instance.hasItems(ModItems.ADVANCED_ENCHANTING_TABLE.get()))
                .save(consumer);
    }
    private void registerDecorativeBook(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends Item> targetItemReg, IItemProvider subIngredient) {
        registerDecorativeBook(consumer, targetItemReg, Ingredient.of(subIngredient));
    }
    private void registerDecorativeBook(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends Item> targetItemReg, ITag<Item> subIngredient) {
        registerDecorativeBook(consumer, targetItemReg, Ingredient.of(subIngredient));
    }
}
