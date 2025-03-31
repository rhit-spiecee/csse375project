import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Card> discardPile = new ArrayList<>();
    PlayerDeck deck;
    
    int coins;
    int buy = 1;
    int action = 1;

    public Player(PlayerDeck deck) {
        this.deck = deck;
        this.coins = 0;
    }

    public Player() {
        this.deck = new PlayerDeck();
        this.coins = 0;
    }

    public void drawHand() {
        for (int i = 0; i < 5; i++) {
            drawOneCard();
        }
        this.buy = 1;
        this.action = 1;
    }

    public void drawOneCard() {
        hand.add(deck.draw());
    }

    public int getCoins() {
        int coins = this.coins;
        for (Card card : hand) {
            if (card.type == Card.CardType.TREASURE) {
                coins += card.value;
            }
        }
        return coins;
    }

    public int getBuys() {
        return this.buy;
    }

    public int getActions() {
        return this.action;
    }

    public boolean hasActionCard() {
        for (Card card : hand) {
            if (card.type == Card.CardType.KINGDOM) {
                return true;
            }
        }
        return false;
    }

    public void cleanup() {
        if (hand.isEmpty()) { return; }
        discardPile.add(hand.removeFirst());
    }
}
