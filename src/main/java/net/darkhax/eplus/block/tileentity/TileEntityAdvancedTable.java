package net.darkhax.eplus.block.tileentity;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.registry.ModTileEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;


@ParametersAreNonnullByDefault
public class TileEntityAdvancedTable extends TileEntityWithBook implements INamedContainerProvider, INameable {
    private ITextComponent customName;
    private final Map<UUID, ItemStackHandlerEnchant> inventories = new HashMap<>();

    public TileEntityAdvancedTable() {
        super(ModTileEntityTypes.ADVANCED_ENCHANTING_TABLE.get());
    }

    public ItemStackHandlerEnchant getInventory(PlayerEntity player) {
        final ItemStackHandlerEnchant inventory = this.inventories
                .getOrDefault(player.getUUID(), new ItemStackHandlerEnchant(this));
        this.inventories.put(player.getUUID(), inventory);
        return inventory;
    }

    public Map<UUID, ItemStackHandlerEnchant> getInventories() {
        return this.inventories;
    }

    @Override
    public void serialize(CompoundNBT dataTag) {
        final ListNBT list = new ListNBT();
        for (final Entry<UUID, ItemStackHandlerEnchant> inventory : this.inventories.entrySet()) {
            final CompoundNBT invTag = new CompoundNBT();
            invTag.putUUID("Owner", inventory.getKey());
            invTag.put("Inventory", inventory.getValue().serializeNBT());
            list.add(invTag);
        }
        dataTag.put("InvList", list);
        if (customName != null) {
            dataTag.putString("CustomName", ITextComponent.Serializer.toJson(customName));
        }
    }

    @Override
    public void deserialize(CompoundNBT dataTag) {
        this.inventories.clear();
        final ListNBT list = dataTag.getList("InvList", NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            final CompoundNBT tag = list.getCompound(i);
            final UUID owner = tag.getUUID("Owner");
            final ItemStackHandlerEnchant inv = new ItemStackHandlerEnchant(this);
            inv.deserializeNBT(tag.getCompound("Inventory"));
            this.inventories.put(owner, inv);
        }
        if (dataTag.contains("CustomName", 8)) {
            customName = ITextComponent.Serializer.fromJson(dataTag.getString("CustomName"));
        }
    }

    @Override @Nonnull
    public ITextComponent getName() {
        return customName != null ? customName
                : new TranslationTextComponent("block.eplus.advanced_table");
    }

    @Override @Nonnull
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Override @Nullable
    public ITextComponent getCustomName() {
        return customName;
    }

    @Override @Nullable
    public Container createMenu(int id, PlayerInventory player, PlayerEntity entity) {
        return new ContainerAdvancedTable(id, player, this);
    }
}