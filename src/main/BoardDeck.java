import java.util.Stack;

public class BoardDeck {
    private final Stack<Card> deck = new Stack<>();

    public BoardDeck(Card card, int size) {
        for(int i = 0; i < size; i++) {
            deck.add(card);
        }
    }

    public int size() {
        return deck.size();
    }
}
