import java.sql.Array;
import java.util.ArrayList;

public class Board {
    public boolean startGame(int numPlayers) {
        return numPlayers > 1 && numPlayers < 5;
    }

    public ArrayList<String> getCursedDeck() {

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("cursed");
        }

        return list;
    }
}
