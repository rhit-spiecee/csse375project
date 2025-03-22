import java.util.ArrayList;
import java.util.List;

public class Player {
    public int deckSize() {
        return 10;
    }

    public List<String> getDeck() {
        ArrayList<String> deck = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            deck.add("Estate");
        }
        for (int i = 0; i < 7; i++) {
            deck.add("Copper");
        }

        return deck;
    }
}
