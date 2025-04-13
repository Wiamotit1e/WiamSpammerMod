package wiam.wiamspammer.automessage;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageMatcher {

    public MessageMatcher(boolean isMatchSystemMessage, boolean isMatchChatMessage, boolean isMatchDmMessage, String pattern, List<String> matchPlayers) {
        this.isMatchSystemMessage = isMatchSystemMessage;
        this.isMatchChatMessage = isMatchChatMessage;
        this.isMatchDmMessage = isMatchDmMessage;
        this.pattern = pattern;
        this.matchPlayers = matchPlayers;
    }

    public boolean isMatchSystemMessage;

    public boolean isMatchChatMessage;

    public boolean isMatchDmMessage;

    public String pattern;

    public List<String> matchPlayers;

    public boolean match(ProcessedMessage message) {
        ProcessedMessage.PMessageType type = message.type();
        if(type == ProcessedMessage.PMessageType.SYSTEM && isMatchSystemMessage) {
            return message.message().matches(pattern);
        } else if((type == ProcessedMessage.PMessageType.CHAT) && isMatchChatMessage){
            return message.message().matches(pattern) && (matchPlayers.isEmpty() || matchPlayers.contains(message.sender()));
        } else if((type == ProcessedMessage.PMessageType.DM) && isMatchDmMessage){
            return message.message().matches(pattern) && (matchPlayers.isEmpty() || matchPlayers.contains(message.sender()));
        } else return false;
    }

    public List<String> group(ProcessedMessage message) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(message.message());
        if (m.matches()) {
            List<String> groups = new ArrayList<>();
            for (int i = 1; i <= m.groupCount(); i++) {
                groups.add(m.group(i));
            }
            return groups;
        }
        return Collections.emptyList();
    }
}
