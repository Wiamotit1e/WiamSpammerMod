package wiam.wiamspammer.automessage;

import org.jetbrains.annotations.Nullable;

public record ProcessedMessage(@Nullable String sender, String message, PMessageType type) {

    public enum PMessageType {
        DM,
        CHAT,
        SYSTEM
    }

    public void sendToAutoMessage() {
        AutoMessage.execute(this);
    }
}
