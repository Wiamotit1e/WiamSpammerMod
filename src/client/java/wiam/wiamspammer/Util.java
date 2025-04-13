package wiam.wiamspammer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.HashMap;
import java.util.Map;

public class Util {

    private static final Map<String,Integer> sendMap = new HashMap<>();

    private static final ClientTickEvents.EndTick sendTick = minecraftClient -> {
        for (Map.Entry<String,Integer> entry : sendMap.entrySet()) {
            if(entry.getValue() == 0) {
                sendMessage(entry.getKey());
                removeSend(entry.getKey());
            }else {
                entry.setValue(entry.getValue() - 1);
            }
        }
    };

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(sendTick);
    }

    public static void sendMessage(String message) {
        sendMessage(message, MinecraftClient.getInstance().getNetworkHandler());
    }

    public static void sendMessage(String message, ClientPlayNetworkHandler networkHandler) {
        if(networkHandler == null) return;
        if (message.startsWith("/")) {
            networkHandler.sendChatCommand(message.substring(1));
        } else {
            networkHandler.sendChatMessage(message);
        }
    }

    public static void sendMessage(String message, int delay) {
        if (sendMap.containsKey(message)) return;
        if(delay == 0) {
            sendMessage(message);
        } else {
            setSend(message, delay);
        }
    }

    public static void removeAllMessages() {
        sendMap.clear();
    }

    private static void setSend(String message, Integer delay) {
        sendMap.put(message, delay);
    }

    private static void removeSend(String message) {
        sendMap.remove(message);
    }
}
