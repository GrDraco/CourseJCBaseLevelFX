package chat.core;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// Класс передачи данных через сокет
public class WriterSocket extends Thread {
    private final String nickName;
    private final Socket socket;
    private final IMessageTextListener messageTextListener;
    private final ILogListener logListener;
    private DataOutputStream writer;

    public WriterSocket(String nickName, Socket socket, IMessageTextListener messageTextListener, ILogListener logListener) throws Exception {
        if (nickName == null || nickName.isEmpty() || socket == null || logListener == null)
            throw new Exception("Params of WriterSocket is null");
        this.nickName = nickName;
        this.socket = socket;
        this.messageTextListener = messageTextListener;
        this.logListener = logListener;
        writer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        if (messageTextListener == null)
            return;
        try {
            Message message;
            do {
                message = sendMessage(messageTextListener.getMessageText());
            } while (message.getType() != EnumMessageType.FINISH);
            writer.close();
            socket.close();
        }
        catch (IOException e) {
            if(socket.isClosed())
                return;
            logListener.onSetLog(e.toString());
        }
    }

    public Message sendMessage(String text) {
        if(socket.isClosed())
            return null;
        return sendMessage(Message.createMessage(nickName, text));
    }

    public Message sendMessage(Message message) {
        if(socket.isClosed())
            return null;
        try {
            writer.writeUTF(new Gson().toJson(message));
            writer.flush();
        }
        catch (IOException e) {
            if(socket.isClosed())
                return null;
            logListener.onSetLog(e.toString());
        }
        return message;
    }

}
