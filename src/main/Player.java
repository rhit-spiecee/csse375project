import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand = new ArrayList<Card>();
    PlayerDeck deck = new PlayerDeck();

    public void drawHand() {
        for (int i = 0; i < 5; i++) {
            drawOneCard();
        }
    }

    public void drawOneCard() {
        hand.add(deck.draw());
    }
}
