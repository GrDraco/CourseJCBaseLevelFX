package chat.core.history;

import chat.core.ILogListener;
import chat.core.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class History {
    private final String FILE_NAME = "History_%s.txt";
    private final String selfNickName;
    private final ILogListener logListener;

    public History(String selfNickName, ILogListener logListener) {
        this.selfNickName = selfNickName;
        this.logListener = logListener;
    }

    public void add(Message message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(FILE_NAME, selfNickName), true))) {
            writer.write(message.toHistory() + "\n");
        } catch (IOException e) {
            logListener.onSetLog(e.toString());
        }
    }

    public ArrayList<Message> last(int count) {
        if(count <= 0)
            return null;
        ArrayList<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format(FILE_NAME, selfNickName)))) {
            AtomicInteger index = new AtomicInteger();
            List<String> lines = reader.lines().collect(Collectors.toList());
            lines.forEach(line -> {
                index.getAndIncrement();
                if (line != null && !line.isEmpty() && (index.get() > lines.size() - count)) {
                    Message message = Message.parse(line);
                    if (message != null)
                        messages.add(message);
                }
            });
        } catch (IOException e) {
            logListener.onSetLog(e.toString());
        }
        return messages;
    }
}
