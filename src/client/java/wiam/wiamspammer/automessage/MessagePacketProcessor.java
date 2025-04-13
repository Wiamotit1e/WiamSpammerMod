package wiam.wiamspammer.automessage;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class MessagePacketProcessor {

    public static ProcessedMessage processMessage(Packet packet) {
        if (packet instanceof GameMessageS2CPacket gamePacket) {
            return buildSystemMessage(gamePacket);
        } else if (packet instanceof ChatMessageS2CPacket chatPacket) {
            return buildChatMessage(chatPacket);
        }
        return null;
    }

    private static ProcessedMessage buildSystemMessage(GameMessageS2CPacket packet) {
        return new ProcessedMessage(null, packet.content().getString(), ProcessedMessage.PMessageType.SYSTEM);
    }

    private static ProcessedMessage buildChatMessage(ChatMessageS2CPacket packet) {
        final MessageType.Parameters params = packet.serializedParameters();
        final String sender = params.name().getString();
        final String content = packet.body().content();
        final boolean isDm = isMinecraftChatType(params);
        final ProcessedMessage.PMessageType type = isDm ? ProcessedMessage.PMessageType.DM : ProcessedMessage.PMessageType.CHAT;
        return new ProcessedMessage(sender, content, type);
    }

    private static boolean isMinecraftChatType(MessageType.Parameters params) {
        return params.type().getKey()
                .map(RegistryKey::getValue)
                .map(Identifier::toString)
                .filter("minecraft:msg_command_incoming"::equals)
                .isPresent();
    }
}
