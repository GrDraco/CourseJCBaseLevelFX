package chat.core;

import java.net.Socket;

public interface IMessageListener extends java.util.EventListener {
    void onNewMessage(Message message, Socket socket) throws Exception;
}