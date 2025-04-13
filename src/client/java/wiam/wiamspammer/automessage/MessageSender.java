package wiam.wiamspammer.automessage;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import wiam.wiamspammer.Util;

import java.util.List;
import java.util.regex.Pattern;

public class MessageSender {

    public MessageSender(ClientPlayNetworkHandler networkHandler, String message, MessageSenderController controller) {
        this.networkHandler = networkHandler;
        this.message = message;
        this.controller = controller;
    }

    private final MessageSenderController controller;

    private final ClientPlayNetworkHandler networkHandler;

    private final String message;

    public void send(List<String> args) {
        if(networkHandler == null) return;
        String output = message;
        if (controller.canSend()) {
            for (int i = 0; i < args.size(); i++) output = output.replaceAll(Pattern.quote("${" + i + "}"), args.get(i));
            Util.sendMessage(output, getSendDelay());
        }
    }

    public void tick() {
    }

    public int getSendDelay(){
        return this.controller.getSendDelay();
    }
}
