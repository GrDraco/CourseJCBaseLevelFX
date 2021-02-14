package chat.core;

import chat.core.history.History;
import javafx.application.Platform;

import java.net.Socket;

public class ChatClient extends Thread implements IMessageListener{
    private final IChatViewModel viewModel;
    private ReaderSocket reader;
    private WriterSocket writer;
    private String nickName;

    public ChatClient(IChatViewModel viewModel) throws Exception {
        if (viewModel == null)
            throw new Exception("ChatClient.viewModel is null");
        this.viewModel = viewModel;
    }

    public void run() {
        try (Socket socket = new Socket(viewModel.getHost(),viewModel.getPort())) {
            viewModel.onStart();
            writer = new WriterSocket("auth", socket, viewModel, viewModel);
            reader = new ReaderSocket(socket, this, viewModel);
            reader.run();
        } catch (Exception e) {
            viewModel.onSetLog(e.toString());
        } finally {
            reader.interrupt();
            viewModel.onClose();
        }
    }

    public void sendAuth(String login, String password)
    {
        if(writer != null)
            writer.sendMessage(Message.createAuth(login, password));
    }

    public void sendChangeNickName(String nickName)
    {
        if(writer != null)
            writer.sendMessage(Message.createChangeNickName(this.nickName, nickName));
    }

    public void sendMessage(String message)
    {
        if(writer != null)
            writer.sendMessage(message);
    }

    public boolean isAuthorized() {
        return nickName != null && !nickName.isEmpty();
    }

    @Override
    public void onNewMessage(Message message, Socket socket) throws Exception {
        if(message.getType() == EnumMessageType.MESSAGE) {
            History.getInstance().add(message);
            Platform.runLater(new Thread(() -> viewModel.onNewMessage(message, socket)));
        }
        else if (nickName != null && message.getType() == EnumMessageType.NICK_NAME) {
            setNickName(message.getText() != null && !message.getText().isEmpty() ? message.getText() : null);
            writer = new WriterSocket(nickName, socket, viewModel, viewModel);
            Platform.runLater(new Thread(() -> {
                viewModel.onChangedNickName(nickName);
            }));
        } else if (nickName == null && (message.getType() == EnumMessageType.NICK_NAME || message.getType() == EnumMessageType.AUTH_FAIL)) {
            boolean changeNickName = !isAuthorized();
            setNickName(message.getText() != null && !message.getText().isEmpty() ? message.getText() : null);
            writer = isAuthorized() ? new WriterSocket(nickName, socket, viewModel, viewModel) : writer;
            Platform.runLater(new Thread(() -> {
                if (changeNickName)
                    viewModel.onChangedNickName(nickName);
                else {
                    viewModel.onAuth(isAuthorized());
                    try {
                        History.getInstance().last(100).forEach(msg -> {
                            viewModel.onNewMessage(msg, socket);
                        });
                    } catch (Exception e) {
                        viewModel.onSetLog(e.toString());
                    }
                }
            }));
        }
    }

    private void setNickName(String value) {
        nickName = value;
        History.createInstance(nickName, viewModel);
    }
}
