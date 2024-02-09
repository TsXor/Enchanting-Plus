package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.api.event.AvailableEnchantmentsEvent;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.gui.GuiAdvancedTablePure;
import net.darkhax.eplus.util.EnchantmentUtils;
import net.darkhax.eplus.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * This class is a "mixin". It does the job of <code>EnchantmentLogicController</code> in previous versions.
 * <br/>Why? Because as a member of GUI and Container classes, the Controller have limited access to information needed,
 * which is a pain in my ass. To solve this while keeping enchantment logic separated, I stripped out pure logic in this class.
 * <br/>TODO: enable adding multiple enchantments a time
 */
public class ContainerAdvancedTable extends ContainerAdvancedTablePure {
    public static final UUID LUCKY_UUID = UUID.fromString("10755ea6-9721-467a-8b5c-92adf689072c");

    private float enchantmentPower;
    private int cost;
    public final GuiAdvancedTablePure.EnchantmentList availableEnchantments = new GuiAdvancedTablePure.EnchantmentList();
    @Nullable private EnchantmentData selectedEnchantment = null;

    public ContainerAdvancedTable(int windowId, PlayerInventory playerInv, TileEntityAdvancedTable tileEntity) {
        super(windowId, playerInv, tileEntity);
    }

    public ContainerAdvancedTable(int windowId, PlayerInventory playerInv, PacketBuffer data) {
        super(windowId, playerInv, data);
    }


    public boolean isPlayerCreative() {
        PlayerEntity playerSP = Minecraft.getInstance().player;
        if (playerSP != null) return playerSP.isCreative();
        return this.player.isCreative();
    }

    public boolean isPlayerLucky() {
        PlayerEntity playerSP = Minecraft.getInstance().player;
        if (playerSP != null) return playerSP.getUUID().equals(LUCKY_UUID);
        return this.player.getUUID().equals(LUCKY_UUID);
    }

    public boolean canPlayerAfford() {
        return isPlayerCreative() || this.cost <= EnchantmentUtils.getExperience(this.player);
    }

    public boolean isCurseAvailable() {
        return WorldUtils.isWickedNight(this.player.level);
    }

    public int getCost() {
        return cost;
    }

    public float getEnchantmentPower() {
        return enchantmentPower;
    }

    private void updateEnchantmentPower() {
        this.enchantmentPower = EnchantmentUtils.getEnchantPowerReceived(this.player.level, this.tileEntity.getBlockPos());
    }

    private void updateCost() {
        this.cost = 0;
        if (this.selectedEnchantment == null) return;

        // Calculate cost of enchantment
        // adding/removing curse and applying normal cost the same
        this.cost += EnchLogic.calculateEnchUpgradeCost(this.selectedEnchantment);
        // Apply bookshelf discount
        if (this.enchantmentPower > 0) {
            this.cost -= (int) ((float)this.cost * this.enchantmentPower / 100f);
        }
    }

    @Override
    public void onEnchantItemChange() {
        ItemStack inputStack = this.slotEnchant.getItem();

        // obtain available enchantments, existing enchantments will always be available,
        // but its label may be locked
        Map<Enchantment, Integer> availableEnchantmentMap = new HashMap<>();
        Map<Enchantment, Integer> existingEnchantmentMap = EnchantmentHelper.getEnchantments(inputStack);
        List<Enchantment> validEnchantments = EnchLogic.getValidEnchantments(
                inputStack, this.player.level, this.tileEntity.getBlockPos());
        validEnchantments.forEach(enchantment -> availableEnchantmentMap.put(enchantment, 0));
        availableEnchantmentMap.putAll(existingEnchantmentMap);

        // set available enchantments
        AvailableEnchantmentsEvent event = new AvailableEnchantmentsEvent(inputStack, availableEnchantmentMap);
        MinecraftForge.EVENT_BUS.post(event);
        this.availableEnchantments.clear();
        event.enchantments.forEach(
                (enchantment, level) -> this.availableEnchantments.add(new EnchantmentData(enchantment, level))
        );
        this.availableEnchantments.isDirty = true;

        // trigger callback
        this.updateEnchantment(null);
        // we update power on item change like vanilla enchanting table does
        this.updateEnchantmentPower();
    }

    public void updateEnchantment(@Nullable EnchantmentData info) {
        this.selectedEnchantment = info;
        this.updateCost();
    }

    public void enchantItem() {
        if (this.selectedEnchantment == null) { return; }
        // Only non-creative players get charged
        if (!this.player.isCreative()) {
            // If player doesn't have enough exp, ignore them.
            if (EnchantmentUtils.getExperience(this.player) < this.getCost()) return;
            if (this.cost > 0) EnchantmentUtils.removeExperience(this.player, this.cost);
        }
        // Apply new enchantment
        ItemStack inputStack = this.slotEnchant.getItem();
        if (this.selectedEnchantment.enchantment.isCurse() && ! this.isCurseAvailable()) {
            EnchantmentUtils.upgradeEnchantment(inputStack, this.selectedEnchantment, -1);
        } else {
            EnchantmentUtils.upgradeEnchantment(inputStack, this.selectedEnchantment, 1);
        }

        // Update the logic.
        this.onEnchantItemChange();
    }
}
