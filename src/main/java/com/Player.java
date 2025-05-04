package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Player {
    ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Card> discardPile = new ArrayList<>();
    PlayerDeck deck;

    int coins = 0;
    int buy = 1;
    int action = 1;

    public Player(PlayerDeck deck) {
        this.deck = deck;
    }

    public Player() {
        this.deck = new PlayerDeck();
    }

    public void addBoughtCard(Card card) {
        discardPile.add(card);
    }

    public void drawHand() {
        for (int i = 0; i < 5; i++) {
            drawOneCard();
        }
        this.buy = 1;
        this.action = 1;
    }

    private void drawWhenNotEnoughCards() {
        emptyRemainingDeck();

        recycleCards();

        while (hand.size() < 5) {
            drawOneCard();
        }
    }

    private void recycleCards() {
        for (Card card : discardPile) {
            deck.add(card);
        }

        discardPile.clear();
        deck.shuffle();
    }

    private void emptyRemainingDeck() {
        for (int i = 0; i < deck.size(); i++) {
            drawOneCard();
        }
    }

    public void drawOneCard() {
        if (deck.size() == 0) {
            recycleCards();
        }
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

    public int getCoinsInHand() {
        int coins = 0;
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

    public boolean hasMoatCard() {
        for (Card card : hand) {
            if (card.name.equals("moat")) {
                return true;
            }
        }
        return false;
    }

    public void cleanup() {
        coins = 0;
        buy = 1;
        action = 1;
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

    public void discardCard(String cardToDiscard) {
        for (Card card : hand) {
            if (card.name.equals(cardToDiscard)) {
                discardCard(card);
                return;
            }
        }
    }

    public List<KingdomCard> getActionCards() {
        List<KingdomCard> actionCards = new ArrayList<>();
        for (Card card : hand) {
            if (card instanceof KingdomCard) {
                actionCards.add((KingdomCard) card);
            }
        }
        return actionCards;
    }

    public ArrayList<String> getCardsInHandNamesExcept(String cardName) {
        HashSet<String> cardNames = new HashSet<>();
        for (Card card : hand) {
            if (!card.name.equalsIgnoreCase(cardName)) {
                cardNames.add(card.name);
            }
        }
        return new ArrayList<>(cardNames.stream().toList());
    }

    public ArrayList<String> getTreasureCardsInHandNames() {
        HashSet<String> treasureCards = new HashSet<>();
        for (Card card : hand) {
            if (card.type == Card.CardType.TREASURE) {
                treasureCards.add(card.name);
            }
        }
        return new ArrayList<>(treasureCards.stream().toList());
    }

    public Card trashCard(String cardToTrash) {
        Card cardToRemove = null;
        for (Card card : hand) {
            if (card.name.equalsIgnoreCase(cardToTrash)) {
                cardToRemove = card;
                break;
            }
        }
        hand.remove(cardToRemove);

        return cardToRemove;
    }

    public void removeTreasureCardsOfCost(int cost) {
        int goldValue = 3;
        int silverValue = 2;
        int copperValue = 1;

        int coinsRemainingAfterGold = getCoinsAfterRemovingCard(cost, goldValue, "gold");
        int coinsRemainingAfterSilver = getCoinsAfterRemovingCard(coinsRemainingAfterGold, silverValue, "silver");
        getCoinsAfterRemovingCard(coinsRemainingAfterSilver, copperValue, "copper");

    }

    private int getCoinsAfterRemovingCard(int coinsRemaining, int treasuryCardValue, String treasureCardType) {
        int index = hasTreasureCardType(treasureCardType);
        while (coinsRemaining >= treasuryCardValue && index > -1) {
            hand.remove(index);
            coinsRemaining -= treasuryCardValue;
            index = hasTreasureCardType(treasureCardType);
        }
        return coinsRemaining;
    }

    private int hasTreasureCardType(String type) {
        int index = 0;
        for (Card card : hand) {
            if (card.name.equals(type)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int calculateScore() {
        emptyRemainingDeck();
        discardPile.addAll(hand);
        int score = 0;
        for (Card card : discardPile) {
            if (card.type == Card.CardType.VICTORY) {
                score += card.value;
            }
        }
        return score;
    }
}
