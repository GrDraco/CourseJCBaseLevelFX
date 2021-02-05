package chat.core;

public interface ILogListener extends java.util.EventListener {
    void onSetLog(String message);
}
