package chat.core;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

// Класс чтения данных из сокета
public class ReaderSocket  extends Thread {
    private final Socket socket;
    private final IMessageListener messageListener;
    private final ILogListener logListener;
    private Message message;

    public ReaderSocket(Socket socket, IMessageListener messageListener, ILogListener logListener) throws Exception {
        if (socket == null || messageListener == null || logListener == null)
            throw new Exception("Params of ReaderSocket is null");
        this.socket = socket;
        this.messageListener = messageListener;
        this.logListener = logListener;
    }

    @Override
    public void run() {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            do {
                Thread.sleep(100);
                if (socket.isClosed())
                    break;
                message = new Gson().fromJson(dataInputStream.readUTF(), Message.class);
                messageListener.onNewMessage(message, socket);
            } while (message.getType() != EnumMessageType.FINISH && !isInterrupted());
            socket.close();
        } catch (IOException | InterruptedException e) {
            if(socket.isClosed())
                return;
            logListener.onSetLog(e.toString());
        } catch (Exception e) {
            logListener.onSetLog(e.toString());
        }
    }

    public Message getMessage() {
        return message;
    }
}
