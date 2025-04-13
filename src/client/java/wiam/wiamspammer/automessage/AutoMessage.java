package wiam.wiamspammer.automessage;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.ArrayList;
import java.util.List;

public class AutoMessage {

    public static boolean canAutoMessage;

    public static void setAutoMessages(List<AutoMessage> autoMessages) {
        AutoMessage.autoMessages = autoMessages;
    }

    private static List<AutoMessage> autoMessages = new ArrayList<>();

    public static boolean isAutoMessagesEmpty(){
        return autoMessages.isEmpty();
    }

    public AutoMessage(boolean isThisOn, boolean isPositiveMatch, boolean isMatchSystemMessage, boolean isMatchChatMessage, boolean isMatchDmMessage, String pattern, List<String> matchPlayers, String message, float sendDelay, ClientPlayNetworkHandler networkHandler) {
        this.isThisOn = isThisOn;
        this.isPositiveMatch = isPositiveMatch;
        this.matcher = new MessageMatcher(isMatchSystemMessage, isMatchChatMessage, isMatchDmMessage, pattern, matchPlayers);
        this.sender = new MessageSender(networkHandler, message, new MessageSenderController(Math.round(sendDelay * 20)));
    }

    private final boolean isThisOn;

    private final boolean isPositiveMatch;

    private final MessageMatcher matcher;

    private final MessageSender sender;

    private static final ClientTickEvents.EndTick autoMessageTick = (minecraftClient) ->{
        if(isAutoMessagesEmpty()) return;
        autoMessages.stream().filter(autoMessage -> autoMessage.isThisOn).forEach(autoMessage -> autoMessage.sender.tick());
    };

    public static void initialize(){
        ClientTickEvents.END_CLIENT_TICK.register(autoMessageTick);
    }


    public static void execute(ProcessedMessage message) {
        if (!canAutoMessage) return;
        autoMessages.stream().filter(m -> m.isThisOn && m.match(message)).forEach(m -> m.send(message));
    }

    public boolean match(ProcessedMessage message) {
        if(isPositiveMatch) return matcher.match(message);
        else return !matcher.match(message);
    }

    public List<String> group(ProcessedMessage message) {
        return matcher.group(message);
    }

    public void send(ProcessedMessage message) {
        sender.send(group(message));
    }

    public static void turnOn(){
        canAutoMessage = true;
    }

    public static void turnOff(){
        canAutoMessage = false;
    }
}
