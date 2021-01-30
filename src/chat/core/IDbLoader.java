package chat.core;

public interface IDbLoader {
    String auth(String login, String password);
    void addClient(String login, String nickName, String password);
}