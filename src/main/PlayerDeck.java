public class PlayerDeck {
    private int i;

    public int size() {
        return 10;
    }

    public Card draw() {
        i++;
        if (i >= 1 && i <= 7) {
            return new Card("copper", 0, Card.CardType.TREASURE, 1);
        }
        return new Card("estate", 2, Card.CardType.VICTORY, 1);
    }
}
