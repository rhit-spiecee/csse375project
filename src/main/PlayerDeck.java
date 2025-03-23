import javax.naming.SizeLimitExceededException;
import java.util.NoSuchElementException;
import java.util.Stack;

public class PlayerDeck {
    public static final int MAX_DECK_SIZE = 308;
    private Stack<Card> deck;

    public PlayerDeck() {
        deck = new Stack<>();

        for (int i = 0; i < 3; i++) {
            deck.add(new Card("estate", 2, Card.CardType.VICTORY, 1));
        }
        for (int i = 0; i < 7; i++) {
            deck.add(new Card("copper", 0, Card.CardType.TREASURE, 1));
        }
    }

    public int size() {
        return deck.size();
    }

    public Card draw() {
        if (this.size() == 0) {
            throw new NoSuchElementException();
        }
        return deck.pop();
    }

    public void add(Card card) {
        if (this.size() == MAX_DECK_SIZE) {
            throw new IndexOutOfBoundsException();
        }
        deck.add(card);
    }
}
