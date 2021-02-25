package sample;

import javafx.scene.control.Alert;

import java.util.Random;

public class ModelGame {
    protected static final int ATTEMPTS_DEF = 3;

    protected static ModelGame game;
    protected boolean highLevel;
    protected int attempts;

    public static ModelGame instance() {
        if (game == null)
            game = new ModelGame();
        return game;
    }

    protected ModelGameSession gameSession;

    protected ModelGame() {
        highLevel = false;
        attempts = ATTEMPTS_DEF;
    }

    public void setAttempts(int value) {
        attempts = value;
    }

    public void setHighLevel(boolean value) {
        highLevel = value;
    }

    public String newGame() {
        this.gameSession = new ModelGameSession(highLevel, attempts);
        return this.gameSession.calculate("").message;
    }

    public ModelSessionResult playGame(String answerUser) {
        return this.gameSession.calculate(answerUser);
    }
}
