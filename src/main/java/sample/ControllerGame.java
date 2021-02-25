package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ControllerGame {
    @FXML
    void startHighLevel() {
        ModelGame.instance().setHighLevel(true);
        gameShow();
    }

    @FXML
    void startLiteLevel() {
        ModelGame.instance().setHighLevel(false);
        gameShow();
    }

    @FXML
    private Pane sessionPanel;

    @FXML
    private Pane menuPanel;

    @FXML
    private Label missionMessage;

    @FXML
    private TextField answerValue;

    @FXML
    private Label resultMessage;

    @FXML
    void answerClick() {
        ModelSessionResult result = ModelGame.instance().playGame(answerValue.getText());
        if (result.winner != ModelWinner.UNKNOWN) {
            new Alert(Alert.AlertType.INFORMATION, result.message).show();
            menuShow();
        }
        resultMessage.setText(result.message);
        answerValue.clear();
    }

    @FXML
    public void initialize() {
        menuShow();
    }

    protected void clearGame() {
        missionMessage.setText("");
        resultMessage.setText("");
        answerValue.clear();
    }

    protected void menuShow() {
        menuPanel.setVisible(true);
        sessionPanel.setVisible(false);
    }

    protected void gameShow() {
        menuPanel.setVisible(false);
        sessionPanel.setVisible(true);
        clearGame();
        missionMessage.setText(ModelGame.instance().newGame());
    }
}