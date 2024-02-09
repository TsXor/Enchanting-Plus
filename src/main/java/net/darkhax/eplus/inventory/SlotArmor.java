/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 * <p>
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.eplus.inventory;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SlotArmor extends Slot {
    /**
     * A copy of <code>PlayerContainer.TEXTURE_EMPTY_SLOTS</code> because it is private.
     */
    public static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] {
            PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS,
            PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS,
            PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE,
            PlayerContainer.EMPTY_ARMOR_SLOT_HELMET
    };

    /**
     * The entity to show armor for.
     */
    private final Entity entity;

    /**
     * The type of equipment held by the slot.
     */
    private final EquipmentSlotType slotType;

    /**
     * Creates a new armor inventory slot. Typically used for player armor in a GUI.
     * @param inventory The inventory of the slot.
     * @param index The index of the slot.
     * @param xPosition The x position of the slot.
     * @param yPosition The y position of the slot.
     * @param entity The entity to show armor for.
     * @param type The type of equipment held by the slot.
     */
    public SlotArmor(IInventory inventory, int index, int xPosition, int yPosition, Entity entity, EquipmentSlotType type) {
        super(inventory, index, xPosition, yPosition);
        this.entity = entity;
        this.slotType = type;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.canEquip(this.slotType, this.entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(PlayerContainer.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[this.slotType.getIndex()]);
    }
}
