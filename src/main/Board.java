import java.sql.Array;
import java.util.ArrayList;

public class Board {

    int numberOfPlayers = 0;
    public boolean startGame(int numPlayers) {
        if (numPlayers > 1 && numPlayers < 5) {
            numberOfPlayers = numPlayers;
            return true;
        }
        return false;
    }

}
