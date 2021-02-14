package chat.core.history;

import chat.core.ILogListener;
import chat.core.Message;

import java.io.*;
import java.util.ArrayList;

public class History {
    private final String FILE_NAME = "History_%s.txt";

    private static History instance;
    public static History getInstance() throws Exception {
        if (instance == null)
            throw new Exception("History.instance is null");
        return instance;
    }
    public static void createInstance(String selfNickName, ILogListener logListener) {
        instance = new History(selfNickName, logListener);
    }

    private final String selfNickName;
    private final ILogListener logListener;
    public History(String selfNickName, ILogListener logListener) {
        this.selfNickName = selfNickName;
        this.logListener = logListener;
    }

    public void add(Message message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(FILE_NAME, selfNickName)))) {
            for (int i = 0; i < 20; i++) {
                writer.write(message.toHistory() + "\n");
            }
        } catch (IOException e) {
            logListener.onSetLog(e.toString());
        }
    }

    public ArrayList<Message> last(int count) {
        ArrayList<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format(FILE_NAME, selfNickName)))) {
            String message;
            int index = 0;
            while ((message = reader.readLine()) != null) {
                index++;
                if (index > reader.lines().count() - count)
                    messages.add(Message.parse(message));
            }
        } catch (IOException e) {
            logListener.onSetLog(e.toString());
        }
        return messages;
    }
}
