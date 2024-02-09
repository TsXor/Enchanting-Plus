package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.registry.ModContainerTypes;
import net.darkhax.eplus.util.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;


@ParametersAreNonnullByDefault
public abstract class ContainerAdvancedTablePure extends Container {


    protected final PlayerEntity player;
    protected final TileEntityAdvancedTable tileEntity;
    public final Slot slotEnchant;

    public ContainerAdvancedTablePure(int windowId, PlayerInventory playerInv, TileEntityAdvancedTable tileEntity) {
        super(ModContainerTypes.ADVANCED_ENCHANTING_TABLE.get(), windowId);

        this.player = playerInv.player;
        this.tileEntity = tileEntity;

        // Enchantment slot
        this.slotEnchant = this.addSlot(new SlotEnchant(tileEntity.getInventory(player), ItemStackHandlerEnchant.ENCHANT_SLOT_IDX,
                37, 17, slot -> this.onEnchantItemChange()));
        // Hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x, 43 + 18 * x, 149));
        }
        // Inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 43 + 18 * x, 91 + y * 18));
            }
        }
        // Armor slots
        for (int y = 0; y < 4; y++) {
            this.addSlot(new SlotArmor(playerInv, 39 - y, 7, 24 + y * 19,
                    playerInv.player, EntityUtils.getEquipmentSlot(y)));
        }
    }

    public ContainerAdvancedTablePure(int windowId, PlayerInventory playerInv, PacketBuffer data) {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    public abstract void onEnchantItemChange();

    private static TileEntityAdvancedTable getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof TileEntityAdvancedTable target) { return target; }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override @Nonnull
    public ItemStack quickMoveStack(PlayerEntity playerEntity, int idx) {
        final Slot clickSlot = slots.get(idx);
        assert clickSlot != null; // this is checked before calling this method
        if (!clickSlot.hasItem()) { return ItemStack.EMPTY; }
        ItemStack sourceStack = clickSlot.getItem();
        if (sourceStack.isEmpty()) { return ItemStack.EMPTY; }

        final boolean isClickSlotInPlayerInv = clickSlot.container instanceof PlayerInventory;
        boolean didMove = false;
        for (final Slot targetSlot : this.slots) {
            if (isClickSlotInPlayerInv == targetSlot.container instanceof PlayerInventory) continue;
            if (!targetSlot.mayPlace(sourceStack)) continue;

            ItemStack targetStack = targetSlot.getItem();
            int maxSize = Math.min(targetStack.getMaxStackSize(), targetSlot.getMaxStackSize());
            int placeableSize = Math.min(maxSize - targetStack.getCount(), sourceStack.getCount());
            if (targetSlot.hasItem()) {
                if (!sourceStack.sameItem(targetStack)) continue;
                sourceStack.shrink(placeableSize);
                targetStack.grow(placeableSize);
            } else {
                targetSlot.set(sourceStack.split(placeableSize));
            }
            targetSlot.setChanged();
            didMove = true;
            if (sourceStack.isEmpty()) break;
        }
        if (didMove) {
            clickSlot.set(sourceStack);
            clickSlot.setChanged();
            this.broadcastChanges();
        }
        return sourceStack;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        double dist = this.tileEntity.getBlockPos().distSqr(player.position(), true);
        return dist <= 64.0D && !player.isDeadOrDying();
    }
}
