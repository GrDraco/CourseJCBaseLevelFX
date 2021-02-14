package chat.core;

import chat.core.loader.Loader;

import java.net.Socket;

public class ClientHandler implements IMessageListener {
    private final String AUTHORIZED = "Авторизовался пользователь ";
    private final String NOT_AUTH = "not auth";

    private final Socket socket;
    private final ILogListener logListener;
    private WriterSocket writer;
    private final ReaderSocket reader;
    private boolean isAuthorized;
    private String nickName;
    private final IMessageListener serverListener;

    public ClientHandler(Socket socket, IMessageListener serverListener, ILogListener logListener) throws Exception {
        this.socket = socket;
        this.serverListener = serverListener;
        this.logListener = logListener;
        this.reader = new ReaderSocket(socket, this, logListener);
        this.isAuthorized = false;
        // Ожидаем авторизацию
        reader.start();
        // По истечении таймаута
        new Thread(() -> {
            try {
                Thread.sleep(120000);
                reader.interrupt();
            } catch (InterruptedException e) {
                logListener.onSetLog(e.toString());
            }
        });
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    public void sendMessage(Message message) {
        if(!socket.isClosed() && writer != null)
            writer.sendMessage(message);
    }

    @Override
    public void onNewMessage(Message message, Socket socket) throws Exception {
        if(message.getType() == EnumMessageType.AUTH) {
            nickName = Loader.auth(message.readLogin(), message.readPassword());
            if (nickName != null && !nickName.isEmpty()) {
                isAuthorized = true;
                logListener.onSetLog(AUTHORIZED + nickName);
                writer = new WriterSocket(nickName, socket, null, logListener);
                sendMessage(Message.createNickName(nickName));
            }
            else {
                writer = new WriterSocket(NOT_AUTH, socket, null, logListener);
                sendMessage(Message.createAuthFail());
            }
        } else if (message.getType() == EnumMessageType.FINISH) {
            reader.interrupt();
        } else if (message.getType() == EnumMessageType.CHANGE_NICK_NAME) {
            Loader.changeNickName(message.getNickName(), message.getText());
            nickName = message.getText();
            sendMessage(Message.createNickName(nickName));
        } else if (message.getType() == EnumMessageType.MESSAGE) {
            logListener.onSetLog(message.toString());
            serverListener.onNewMessage(message, socket);
        }
    }
}
