package chat;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ViewMain {
    @FXML
    private ListView<String> listMessages;

    @FXML
    private TextField textMessage;

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            buttonEnterClick();
        }
    }

    @FXML
    void buttonEnterClick() {
        String message = textMessage.getText();
        if (message.isEmpty())
            return;
        listMessages.getItems().add(message);
        textMessage.clear();
    }
}
