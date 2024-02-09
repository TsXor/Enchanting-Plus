package net.darkhax.eplus.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import net.darkhax.eplus.api.Blacklist;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;


// TODO: test this and implement a more detailed API
@ZenRegister
@ZenCodeType.Name("mods.eplus.Eplus")
@SuppressWarnings("unused")
public class EnchantingPlusCRT {
    @ZenCodeType.Method
    public static void blacklistItem(Item item) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                Blacklist.blacklistItem(item);
            }
            @Override
            public String describe() {
                String name = new ItemStack(item).getDisplayName().getString();
                return String.format("Blacklisting %s from E+", name);
            }
        });
    }

    @ZenCodeType.Method
    public static void blacklistEnchantment(Enchantment enchantment) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                Blacklist.blacklistEnchantment(enchantment);
            }
            @Override
            public String describe() {
                String name = enchantment.getFullname(1).getString();
                return String.format("Blacklisting %s from E+", name);
            }
        });
    }
}