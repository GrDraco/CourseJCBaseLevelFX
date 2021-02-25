package chat.core;

public class Client {
    private String login;
    private String nickName;
    private String password;

    public Client(String login, String nickName, String password) {
        this.login = login;
        this.nickName = nickName;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }
}
