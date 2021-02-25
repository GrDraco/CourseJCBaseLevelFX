package chat.core;

import java.net.Socket;

// Интерфейс взаимодейтсвия с ViewModel приложения
public interface IChatViewModel extends IMessageListener, IMessageTextListener, ILogListener {

    void onChangedNickName(String nickName);

    @Override
    void onNewMessage(Message message, Socket socket);

    @Override
    String getMessageText();

    @Override
    void onSetLog(String message);

    void onAuth(boolean success);

    String getHost();

    int getPort();

    String getLogin();

    void onStart();

    void onClose();
}
