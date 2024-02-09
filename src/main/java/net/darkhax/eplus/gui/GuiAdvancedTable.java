package net.darkhax.eplus.gui;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.api.event.InfoBoxEvent;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.NetworkHandler;
import net.darkhax.eplus.network.messages.MessageAETEnchant;
import net.darkhax.eplus.network.messages.MessageAETSelect;
import net.darkhax.eplus.util.EnchantmentUtils;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;


/**
 * Logic of interaction with container.
 * <br/>TODO: enable adding multiple enchantments a time
 */
@ParametersAreNonnullByDefault
public class GuiAdvancedTable extends GuiAdvancedTablePure {
    public GuiAdvancedTable(ContainerAdvancedTable container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected ItemStack getEnchantButtonStack() {
        return this.menu.isCurseAvailable() ? ITEM_SPOOKY_BONE : ITEM_ENCHANTED_BOOK;
    }

    @Override
    protected List<String> getInfoBoxContent() {
        final List<String> info = new ArrayList<>();
        if (!this.menu.slotEnchant.hasItem()) {
            info.add(I18n.get("gui.eplus.info.noitem"));
        } else if (getEnchantmentList().isEmpty()) {
            info.add(I18n.get("gui.eplus.info.noench"));
        } else {
            final boolean isCreative = this.menu.isPlayerCreative();
            final int playerXP = isCreative ? Integer.MAX_VALUE : EnchantmentUtils.getExperience(this.inventory.player);
            final int cost = this.menu.getCost();
            info.add(isCreative ? I18n.get("eplus.info.infinity") : I18n.get("eplus.info.playerxp", playerXP));
            info.add(I18n.get("eplus.info.costxp", cost));
            info.add(I18n.get("eplus.info.power", this.menu.getEnchantmentPower() + "%"));
            if (cost > playerXP) {
                info.add(" "); info.add(TextFormatting.RED + I18n.get("gui.eplus.info.tooexpensive"));
            }
        }
        String sneakMessage = KEYBINDING_SNEAK.getTranslatedKeyMessage().getString();
        String tip = I18n.get("eplus.info.tip." + this.tipKey, sneakMessage);
        String tipPrefix = I18n.get("eplus.info.tip.prefix");
        info.add(" "); info.add(TextFormatting.YELLOW + tipPrefix + TextFormatting.RESET + tip);
        if (this.menu.isPlayerLucky()) {
            String tipBonus = I18n.get("eplus.info.tip.bonus");
            String tipBirthday = I18n.get("eplus.info.tip.birthday");
            info.add(" "); info.add(TextFormatting.LIGHT_PURPLE + tipBonus + TextFormatting.RESET + tipBirthday);
        }
        final InfoBoxEvent event = new InfoBoxEvent(this, info);
        MinecraftForge.EVENT_BUS.post(event);
        return event.info;
    }

    @Override
    protected List<String> getEnchantButtonTip() {
        final List<String> text = new ArrayList<>();
        if (!this.menu.canPlayerAfford()) {
            text.add(I18n.get("gui.eplus.tooltip.tooexpensive"));
        } else if (this.menu.getCost() == 0) {
            text.add(I18n.get("gui.eplus.tooltip.nochange"));
        } else {
            text.add(I18n.get("gui.eplus.tooltip.enchant"));
        }
        return text;
    }

    @Override
    protected EnchantmentList getEnchantmentList() {
        return this.menu.availableEnchantments;
    }

    @Override
    protected boolean shouldInfoBeLocked(EnchantmentData info) {
        if (info.enchantment.isCurse() && !this.menu.isCurseAvailable()) {
            // downgrade curses
            return info.level <= 0;
        } else {
            return !EnchantmentUtils.isLevelUpgradable(info.level, info.enchantment)
                    || Blacklist.isEnchantmentBlacklisted(info.enchantment);
        }
    }

    @Override
    public void handleEnchantmentSelection(@Nullable EnchantmentData info) {
        this.menu.updateEnchantment(info);
        NetworkHandler.INSTANCE.sendToServer(new MessageAETSelect(info));
    }

    @Override
    public void handleEnchantButtonClick(Button button) {
        if (!this.menu.slotEnchant.hasItem()) return;
        if (this.menu.canPlayerAfford()) {
            this.menu.enchantItem();
            NetworkHandler.INSTANCE.sendToServer(new MessageAETEnchant());
        }
    }
}
