package chat.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Clients extends Thread implements IMessageListener {
    private final String WAITING_CONNECT_CLIENT = "Ожидаем подключение клиента";
    private final String CONNECTED_CLIENT = "Клиент подключился";

    private final ServerSocket serverSocket;
    private final ILogListener logListener;
    private final ArrayList<ClientHandler> handlers;

    public Clients(ServerSocket serverSocket, ILogListener logListener) throws Exception {
        if (serverSocket == null || logListener == null)
            throw new Exception("Params of ReaderSocket is null");
        this.serverSocket = serverSocket;
        this.logListener = logListener;
        this.handlers = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            do {
                Thread.sleep(100);
                logListener.onSetLog(WAITING_CONNECT_CLIENT);
                handlers.add(new ClientHandler(serverSocket.accept(), this, logListener));
                logListener.onSetLog(CONNECTED_CLIENT);
            } while (!isInterrupted());
        } catch (Exception e) {
            logListener.onSetLog(e.toString());
        }
    }

    public void sendMessageToAll(Message message) {
        handlers.forEach(client -> {
            if (client != null && client.isConnected() && !client.getNickName().equals(message.getNickName()))
                client.sendMessage(message);
            else {
                //if (!client.isConnected())
                //    handlers.remove(client);
            }
        });
    }

    public synchronized void sendMessageTo(String nickName, Message message) {
        handlers.forEach(client -> {
            if (client != null && client.getNickName().equals(nickName))
                client.sendMessage(message);
        });
    }

    @Override
    public synchronized void onNewMessage(Message message, Socket socket) throws Exception {
        if (message.getRecipient() != null && !message.getRecipient().isEmpty())
            sendMessageTo(message.getRecipient(), message);
        else
            sendMessageToAll(message);
    }
}
