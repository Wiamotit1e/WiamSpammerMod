package wiam.wiamspammer.automessage;


public class MessageSenderController {

    public MessageSenderController(int sendDelay) {
        this.sendDelay = sendDelay;
    }

    private final int sendDelay;



    public boolean canSend() {
        return true;
    }


    public int getSendDelay() {
        return sendDelay;
    }
}
