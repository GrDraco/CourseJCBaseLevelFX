package chat.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements IMessageListener {
    private final String WAITING_CONNECT_CLIENT = "Ожидаем подключение клиента";
    private final String CONNECTED_CLIENT = "Клиент подключился";

    private ServerSocket serverSocket;
    private final IChatViewModel viewModel;
    private final ArrayList<ClientHandler> handlers;
    private boolean shutdown = false;

    public ChatServer(IChatViewModel viewModel) throws Exception {
        if (viewModel == null)
            throw new Exception("ChatServer.viewModel is null");
        this.viewModel = viewModel;
        this.handlers = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(viewModel.getPort())) {
            viewModel.onStart();
            this.serverSocket = serverSocket;
            connectedClients();
        } catch (Exception e) {
            viewModel.onSetLog(e.toString());
        } finally {
            viewModel.onClose();
            shutdown = true;
        }
    }

    public void connectedClients() {
        try {
            do {
                Thread.sleep(100);
                viewModel.onSetLog(WAITING_CONNECT_CLIENT);
                handlers.add(new ClientHandler(serverSocket.accept(), this, viewModel));
                viewModel.onSetLog(CONNECTED_CLIENT);
            } while (!shutdown);
        } catch (Exception e) {
            viewModel.onSetLog(e.toString());
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
