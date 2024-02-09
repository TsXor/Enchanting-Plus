package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;


/**
 * TODO: enable adding multiple enchantments a time
 */
@SuppressWarnings("deprecation")
public class MessageAETSelect implements ISerializableMessage {
    static final int LEVEL_MAX = 255;

    @Nullable public EnchantmentData newSelection;

    @SuppressWarnings("unused")
    public MessageAETSelect() {}

    public MessageAETSelect(@Nullable EnchantmentData newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        if (this.newSelection == null) {
            // mark byte
            buffer.writeByte(0);
        } else {
            // mark byte
            buffer.writeByte(1);
            // enchantment is stored by key
            ResourceLocation enchantmentKey = Registry.ENCHANTMENT.getKey(this.newSelection.enchantment);
            Objects.requireNonNull(enchantmentKey);
            buffer.writeResourceLocation(enchantmentKey);
            // store enchantment level as a byte
            if (this.newSelection.level <= LEVEL_MAX) {
                buffer.writeByte(this.newSelection.level);
            } else {
                buffer.writeByte(LEVEL_MAX);
                EnchantingPlus.LOGGER.warn("Selected enchantment level too big, capped to {}.", LEVEL_MAX);
            }
        }
    }

    @Override
    public void decode(PacketBuffer buffer) {
        int nonNullMark = buffer.readByte();
        if (nonNullMark == 0) {
            this.newSelection = null;
        } else {
            ResourceLocation enchantmentKey = buffer.readResourceLocation();
            Enchantment enchantment = Registry.ENCHANTMENT.get(enchantmentKey);
            Objects.requireNonNull(enchantment);
            int level = buffer.readByte();
            this.newSelection = new EnchantmentData(enchantment, level);
        }
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        final ServerPlayerEntity srvPlayer = context.get().getSender();
        if (srvPlayer == null) {
            EnchantingPlus.LOGGER.error("MessageAETSelect received by not a server, why?");
            return;
        }
        final Container container = srvPlayer.containerMenu;
        if (container instanceof ContainerAdvancedTable containerAET) {
            containerAET.updateEnchantment(this.newSelection);
        }
    }
}
