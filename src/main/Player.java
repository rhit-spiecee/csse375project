import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand = new ArrayList<Card>();
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
            if (card.getCardType() == Card.CardType.TREASURE) {
                coins += card.getValue();
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
}
