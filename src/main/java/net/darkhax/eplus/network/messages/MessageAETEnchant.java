package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class MessageAETEnchant implements ISerializableMessage {
    public MessageAETEnchant() {}

    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void decode(PacketBuffer buffer) {}

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        final ServerPlayerEntity srvPlayer = context.get().getSender();
        if (srvPlayer == null) {
            EnchantingPlus.LOGGER.error("MessageAETEnchant received by not a server, why?");
            return;
        }
        final Container container = srvPlayer.containerMenu;
        if (container instanceof ContainerAdvancedTable containerAET) {
            containerAET.enchantItem();
        }
    }
}
