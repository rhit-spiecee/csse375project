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

    public ArrayList<String> getCursedDeck() {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < (numberOfPlayers - 1) * 10; i++) {
            list.add("cursed");
        }

        return list;
    }
}
