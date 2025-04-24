package S1G3;

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

    public void addBoughtCard(Card card) {
        discardPile.add(card);
    }

    public void drawHand() {
        if (deck.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                drawOneCard();
            }
        } else {
            drawWhenNotEnoughCards();
        }
        this.buy = 1;
        this.action = 1;
    }

    private void drawWhenNotEnoughCards() {
        emptyRemainingDeck();

        for (Card card : discardPile) {
            deck.add(card);
        }

        discardPile.clear();
        deck.shuffle();

        while (hand.size() < 5) {
            drawOneCard();
        }
    }

    private void emptyRemainingDeck() {
        for (int i = 0; i < deck.size(); i++) {
            drawOneCard();
        }
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
        discardPile.addAll(hand);
        hand.clear();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void discardCard(Card card) {
        hand.remove(card);
        discardPile.add(card);
    }

    public ArrayList<String> getTreasureCardsInHandNames() {
        ArrayList<String> treasureCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.type == Card.CardType.TREASURE) {
                treasureCards.add(Utilities.capitalize(card.name));
            }
        }
        return treasureCards;
    }

    public void trashCard(String cardToTrash) {
        Card cardToRemove = null;
        for (Card card : hand) {
            if (card.name.equalsIgnoreCase(cardToTrash)) {
                cardToRemove = card;
                break;
            }
        }
        hand.remove(cardToRemove);
    }
}
