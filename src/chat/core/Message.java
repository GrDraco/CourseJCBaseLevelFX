package chat.core;

import java.util.Arrays;
import java.util.UUID;

// Модель обмена данными между сервером и клиентом
public class Message {
    private static final String DIV = "//";

    private String id;
    private EnumMessageType type;
    private String nickName;
    private String recipient;
    private String text;

    public static Message createMessage(String nickName, String text) {
        return new Message(nickName, text, EnumMessageType.MESSAGE);
    }

    public static Message createNickName(String nickName) {
        return new Message(nickName, nickName, EnumMessageType.NICK_NAME);
    }

    public static Message createAuthFail() {
        return new Message("", "", EnumMessageType.AUTH_FAIL);
    }

    public static Message createFinish(String nickName) {
        return new Message(nickName, "", EnumMessageType.FINISH);
    }

    public static Message createAuth(String login, String password) {
        return new Message("", login + DIV + password, EnumMessageType.AUTH);
    }

    Message(String nickName, String text, EnumMessageType type) {
        id = UUID.randomUUID().toString();
        this.text = text;
        this.type = type;
        this.nickName = nickName;
        if (text.contains("\\w")) {
            String[] strings = text.split("\\s");
            String username = strings[1];
            String _msg = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));
            this.recipient = username;
            this.text = _msg;
        }
    }

    public String readLogin() {
        if(text == null || text.isEmpty() || !text.contains(DIV))
            return "";
        return text.split(DIV)[0];

    }

    public String readPassword() {
        if(text == null || text.isEmpty() || !text.contains(DIV))
            return "";
        return text.split(DIV)[1];
    }

    public String getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public EnumMessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String toStringShort() {
        return String.format("%s: %s", nickName, text);
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", nickName, id, text);
    }
}
