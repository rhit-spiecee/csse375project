import java.util.ArrayList;

public class Board {
    public boolean startGame(int numPlayers) {
        return numPlayers > 1 && numPlayers < 5;
    }

    public boolean getCursedDeck() {
        return false;
    }
}
