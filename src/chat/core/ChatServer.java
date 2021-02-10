package chat.core;

import java.net.ServerSocket;

public class ChatServer {

    private final IChatViewModel viewModel;

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
