package chat;

import chat.core.ChatClient;
import chat.core.EnumMessageType;
import chat.core.IChatViewModel;
import chat.core.Message;
import chat.core.loader.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.Socket;

public class ViewClientChat {

    private ChatClient model;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textLogin;

    @FXML
    public Pane panelAuth;

    @FXML
    private Pane panelBody;

    @FXML
    private ListView<String> listMessages;

    @FXML
    private TextField textMessage;

    @FXML
    private TextField textNickName;

    @FXML
    private Label labelTextFail;

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            buttonEnterClick();
        }
    }

    @FXML
    void buttonNickNameClick() {
        String nickName = textNickName.getText();
        if (nickName.isEmpty())
            return;
        model.sendChangeNickName(nickName);
    }

    @FXML
    protected void initialize() {
        try {
            model = new ChatClient(new IChatViewModel() {
                @Override
                public void onChangedNickName(String nickName) {
                    textNickName.setText(nickName);
                    listMessages.getItems().add(Message.createMessage("Server", "Ваш ник: " + nickName).toStringShort());
                }

                @Override
                public void onNewMessage(Message message, Socket socket) {
                    if (message == null)
                        return;
                    if (message.getNickName().equals(model.getNickName()))
                        message.setNickName("Вы");
                    if (model.isAuthorized()) {
                        panelAuth.setVisible(false);
                        panelBody.setVisible(true);
                        if (message.getType() == EnumMessageType.MESSAGE)
                            listMessages.getItems().add(message.toStringShort());
                    } else {
                        panelAuth.setVisible(true);
                        panelBody.setVisible(false);
                    }
                }

                @Override
                public void onAuth(boolean success) {
                    if (success) {
                        panelAuth.setVisible(false);
                        panelBody.setVisible(true);
                        listMessages.getItems().add(Message.createMessage("Auth", "Вы авторизованны").toStringShort());
                    } else {
                        panelAuth.setVisible(true);
                        panelBody.setVisible(false);
                        labelTextFail.setText(Message.createMessage("Auth", "Не верно введены логин и пароль").toStringShort());
                    }
                }

                @Override
                public String getMessageText() {
                    return null;
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
                    return textLogin.getText();
                }

                @Override
                public void onStart() {

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
            });
            model.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonEnterClick() {
        String message = textMessage.getText();
        if (message.isEmpty())
            return;
        try {
            model.onNewMessage(model.sendMessage(message), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        textMessage.clear();
    }

    @FXML
    void buttonLogOnClick() {
        model.sendAuth(textLogin.getText(), textPassword.getText());
    }
}
