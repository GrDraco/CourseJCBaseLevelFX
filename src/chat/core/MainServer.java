package chat.core;

import chat.core.loader.Loader;

import java.net.Socket;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) {
        // Тестирование сервера
        try {
            new ChatServer(new IChatViewModel() {
                @Override
                public void onChangedNickName(String nickName) { }

                @Override
                public void onNewMessage(Message message, Socket socket) {
                    System.out.println(message.toString());
                }

                @Override
                public void onAuth(boolean success) { }

                @Override
                public String getMessageText() {
                    return scanner.nextLine();
                }

                @Override
                public String getHost() {
                    return "localhost";
                }

                @Override
                public int getPort() {
                    return 8081;
                }

                @Override
                public String getLogin() {
                    return "server";
                }

                @Override
                public void onStart() {
                    scanner = new Scanner(System.in);
                }

                @Override
                public void onClose() {
                    try {
                        Loader.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        System.exit(0);
                    }
                }

                @Override
                public void onSetLog(String message) {
                    System.out.println(message);
                }
            }).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Scanner scanner;
}
