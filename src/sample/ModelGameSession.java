package sample;

import java.util.Random;

public class ModelGameSession {
    protected static final String GAME = "Угадайте число от %s до %s";
    protected static final String ERROR_GAME = "Что-то пошло не так! Но ваша попытка защитана :)";
    protected static final String MESSAGE_UPPER = "Мое значение больше";
    protected static final String MESSAGE_LOWER = "Мое значение меньше";
    protected static final String MESSAGE_YOU_WRONG = "Не угадал, ха ха.";
    protected static final String MESSAGE_YOU_WIN = "Поздравляю Вы победили, УРА УРА УРА !!!";
    protected static final String MESSAGE_YOU_LOSE = "Вы проиграли";
    protected static final int TOP_VALUE = 10;

    protected int computer;
    protected int user;
    protected int attempt;
    protected int attempts;
    protected boolean highLevel;

    public ModelGameSession(boolean highLevel, int attempts) {
        this.computer = new Random().nextInt(TOP_VALUE);
        this.user = 0;
        this.attempt = -1;
        this.attempts = attempts;
        this.highLevel = highLevel;
    }

    public ModelSessionResult calculate(String answerUser) {
        // Если ссесия уже отработала то прерывает со страрым результатом
        ModelSessionResult res = result();
        if (result().winner != ModelWinner.UNKNOWN)
            return res;
        // Прободим расчет игры в рамках данной сессии
        attempt++;
        if (attempt >= attempts)
            return new ModelSessionResult(MESSAGE_YOU_LOSE, ModelWinner.COMPUTER);
        if (attempt == 0)
            return new ModelSessionResult(String.format(GAME, 0, TOP_VALUE), ModelWinner.UNKNOWN);
        try {
            user = Integer.parseInt(answerUser);
        } catch (Exception err) {
            return new ModelSessionResult(ERROR_GAME, ModelWinner.UNKNOWN);
        }
        return result();
    }

    protected ModelSessionResult result() {
        if (!highLevel) {
            if (computer > user)
                return new ModelSessionResult(MESSAGE_UPPER, ModelWinner.UNKNOWN);
            else if (computer < user)
                return new ModelSessionResult(MESSAGE_LOWER, ModelWinner.UNKNOWN);
            else if (computer == user)
                return new ModelSessionResult(MESSAGE_YOU_WIN, ModelWinner.USER);
        }
        if (computer > user || computer < user)
            return new ModelSessionResult(MESSAGE_YOU_WRONG, ModelWinner.UNKNOWN);
        else if (computer == user)
            return new ModelSessionResult(MESSAGE_YOU_WIN, ModelWinner.USER);
        return new ModelSessionResult(MESSAGE_YOU_LOSE, ModelWinner.COMPUTER);
    }
}
