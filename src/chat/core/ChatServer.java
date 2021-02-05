package chat.core;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {


    private final IChatViewModel viewModel;
    private WriterSocket writer;

    public ChatServer(IChatViewModel viewModel) throws Exception {
        if (viewModel == null)
            throw new Exception("ChatServer.viewModel is null");
        this.viewModel = viewModel;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(viewModel.getPort())) {
            viewModel.onStart();
            Clients clients = new Clients(serverSocket, viewModel);
            clients.run();
        } catch (Exception e) {
            viewModel.onSetLog(e.toString());
        } finally {
            viewModel.onClose();
        }
    }
}
