package sample;

public class ModelSessionResult {
    public ModelWinner winner;
    public String message;
    public ModelSessionResult(String message, ModelWinner winner) {
        this.message = message;
        this.winner = winner;
    }
}
