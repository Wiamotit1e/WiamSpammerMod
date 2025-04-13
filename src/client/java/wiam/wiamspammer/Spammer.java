package wiam.wiamspammer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class Spammer {

    public static int spamDelay;

    public static int tickOfNextSpam;

    public static boolean canSpam;

    public static String spamMessage;

    public static ClientPlayNetworkHandler networkHandler;

    public static ClientTickEvents.EndTick spamTick = minecraftClient -> {
        if(canSpam) {
            if(tickOfNextSpam > 0) {
                tickOfNextSpam--;
            }else {
                Util.sendMessage(spamMessage, networkHandler);
                tickOfNextSpam = spamDelay;
            }
        }
    };

    public static void initialize() {
        canSpam = false;
        spamDelay = 0;
        tickOfNextSpam = 0;
        ClientTickEvents.END_CLIENT_TICK.register(spamTick);
    }

    public static void startSpamming(float delayInSeconds, String message, ClientPlayNetworkHandler inputNetworkHandler) {
        spamDelay = Math.round(delayInSeconds * 20);
        tickOfNextSpam = 0;
        spamMessage = message;
        networkHandler = inputNetworkHandler;
        canSpam = true;
    }
    public static void stopSpamming() {
        canSpam = false;
        spamMessage = null;
    }

}
