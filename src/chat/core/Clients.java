package chat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Clients extends Thread {
    private final String WAITING_CONNECT_CLIENT = "Ожидаем подключение клиента";
    private final String CONNECTED_CLIENT = "Клиент подключился";

    private final ServerSocket serverSocket;
    private final ILogListener logListener;
    private final ArrayList<ClientHandler> handlers;
    private final IDbLoader loader;

    public Clients(ServerSocket serverSocket, ILogListener logListener) throws Exception {
        if (serverSocket == null || logListener == null)
            throw new Exception("Params of ReaderSocket is null");
        this.serverSocket = serverSocket;
        this.logListener = logListener;
        this.handlers = new ArrayList<>();
        this.loader = new IDbLoader() {
            public final ArrayList<Client> clients = new ArrayList<>();
            @Override
            public synchronized String auth(String login, String password) {
                return clients.stream().filter(client -> client.getLogin().equals(login) && client.getPassword().equals(password)).findFirst().get().getNickName();
            }

            @Override
            public void addClient(String login, String nickName, String password)
            {
                clients.add(new Client(login, nickName, password));
            }
        };
        this.loader.addClient("ititov", "IT", "123");
        this.loader.addClient("frasulov", "FR","321");
    }

    @Override
    public void run() {
        try {
            do {
                Thread.sleep(100);
                logListener.onSetLog(WAITING_CONNECT_CLIENT);
                handlers.add(new ClientHandler(serverSocket.accept(), logListener, loader));
                logListener.onSetLog(CONNECTED_CLIENT);
            } while (!isInterrupted());
        } catch (Exception e) {
            logListener.onSetLog(e.toString());
        }
    }

    public void sendMessageToAll(Message message) {
        handlers.forEach(client -> {
            if (client.isConnected())
                client.sendMessage(message);
            else {
                handlers.remove(client);
            }
        });
    }

    public void sendMessageTo(String nickName, Message message) {
        handlers.forEach(client -> {
            if (client.getNickName().equals(nickName))
                client.sendMessage(message);
        });
    }
}
