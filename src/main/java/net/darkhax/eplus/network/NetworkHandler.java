package net.darkhax.eplus.network;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.network.messages.ISerializableMessage;
import net.darkhax.eplus.network.messages.MessageAETEnchant;
import net.darkhax.eplus.network.messages.MessageAETSelect;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class NetworkHandler {
    /**
     * A simple shim for message that implements <code>ISerializableMessage</code>.
     * @param <MSG> message type
     */
    static class MessageCodecShim<MSG extends ISerializableMessage> {
        Class<MSG> messageType;
        MessageCodecShim(Class<MSG> messageType) { this.messageType = messageType; }
        void encode(MSG message, PacketBuffer buffer) {
            message.encode(buffer);
        }
        @SuppressWarnings("deprecation")
        MSG decode(PacketBuffer buffer) {
            try {
                MSG message = this.messageType.newInstance();
                message.decode(buffer);
                return message;
            } catch (IllegalAccessException e) {
                EnchantingPlus.LOGGER.error("cannot access constructor of class {}", this.messageType);
                return null;
            } catch (InstantiationException e) {
                EnchantingPlus.LOGGER.error("cannot create instance of class {}", this.messageType);
                return null;
            }
        }
        void handle(MSG message, Supplier<NetworkEvent.Context> context) {
            message.handle(context);
        }
    }

    public static final String PROTOCOL_VERSION = "1.0";
    public static final String CHANNEL_NAME = "nw_chan";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EnchantingPlus.ID, CHANNEL_NAME),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static <MSG extends ISerializableMessage> void registerMessageType(int id, Class<MSG> messageType) {
        MessageCodecShim<MSG> codec = new MessageCodecShim<>(messageType);
        INSTANCE.registerMessage(id, messageType, codec::encode, codec::decode, codec::handle);
    }

    public static void setup() {
        int id = 0;
        registerMessageType(id++, MessageAETEnchant.class);
        registerMessageType(id++, MessageAETSelect.class);
    }
}