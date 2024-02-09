package net.darkhax.eplus.network.messages;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


/**
 * A simple interface for message that want to implement its logic in its own class.
 * <br/><strong>Note: messages that implement this interface should also implement a public no-arg constructor,
 *              which is called when decoding.</strong>
 */
public interface ISerializableMessage {
    void encode(PacketBuffer buffer);
    void decode(PacketBuffer buffer);
    void handle(Supplier<NetworkEvent.Context> context);
}
