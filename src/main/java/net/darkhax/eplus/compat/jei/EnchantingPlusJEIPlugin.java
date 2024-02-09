package net.darkhax.eplus.compat.jei;

import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;


@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class EnchantingPlusJEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(EnchantingPlus.ID, "jei_plugin");
    private static final Minecraft MC = Minecraft.getInstance();

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        this.addDescription(registration, ModItems.ADVANCED_ENCHANTING_TABLE, "advanced_table");
        this.addDescription(registration, ModItems.TABLE_UPGRADE, "upgrade");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_DEFAULT, "decorative");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_VANILLA, "decorative");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_PRISMARINE, "decorative");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_NETHER, "decorative");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_TARTARITE, "decorative");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_WHITE, "decorative");
        this.addDescription(registration, ModItems.DECORATIVE_BOOK_METAL, "decorative");
    }

    private void addDescription(IRecipeRegistration registration, RegistryObject<? extends Item> itemReg, String key) {
        registration.addIngredientInfo(new ItemStack(itemReg.get()), VanillaTypes.ITEM, ITextComponent.nullToEmpty(I18n.get("jei.eplus." + key)));
    }
}