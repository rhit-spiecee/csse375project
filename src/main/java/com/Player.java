package com;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Player {
    static final int INITIAL_HAND_SIZE = 5;
    static final int INITIAL_BUYS = 1;
    static final int INITIAL_ACTIONS = 1;

    ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Card> discardPile = new ArrayList<>();
    PlayerDeck deck;

    public int bridgeMod = 0;
    int actionsPlayedThisTurn = 0;
    int coins = 0;
    int buy = INITIAL_BUYS;
    int action = INITIAL_ACTIONS;
    ResourceBundle bundle;

    public Player(PlayerDeck deck, ResourceBundle bundle) {
        this.deck = deck;
        this.bundle = bundle;
    }

    public Player(ResourceBundle bundle) {
        this.deck = new PlayerDeck(bundle);
        this.bundle = bundle;
    }

    public Player() {
        this.bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        this.deck = new PlayerDeck(bundle);
    }

    public void addBoughtCard(Card card) {
        discardPile.add(card);
    }

    public void drawHand() {
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            drawOneCard();
        }
        this.buy = INITIAL_BUYS;
        this.action = INITIAL_ACTIONS;
    }

    void recycleCards() {
        for (Card card : discardPile) {
            deck.add(card);
        }

        discardPile.clear();
        deck.shuffle();
    }

    void emptyRemainingDeck() {
        int deckSize = deck.size();
        for (int i = 0; i < deckSize; i++) {
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
            if (card instanceof TreasureCard) {
                coins += card.coinValue;
            }
        }
        return coins;
    }

    public int getCoinsInHand() {
        int coins = 0;
        for (Card card : hand) {
            if (card instanceof TreasureCard) {
                coins += card.coinValue;
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

    public boolean hasActionCardInHand() {
        for (Card card : hand) {
            if (card instanceof KingdomCard) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMoatCard() {
        for (Card card : hand) {
            if (card.name.equals(bundle.getString("moat"))) {
                return true;
            }
        }
        return false;
    }

    public void cleanup() {
        coins = 0;
        buy = INITIAL_BUYS;
        action = INITIAL_ACTIONS;
        actionsPlayedThisTurn = 0;
        discardPile.addAll(hand);
        hand.clear();
        bridgeMod = 0;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void discardCard(Card card) {
        if (hand.remove(card)) {
            discardPile.add(card);
        }
    }

    public boolean discardCard(String cardToDiscard) {
        for (Card card : hand) {
            if (card.name.equals(cardToDiscard)) {
                discardCard(card);
                return true;
            }
        }
        return false;
    }

    public List<KingdomCard> getActionCardsInHand() {
        List<KingdomCard> actionCards = new ArrayList<>();
        for (Card card : hand) {
            if (card instanceof KingdomCard) {
                actionCards.add((KingdomCard) card);
            }
        }
        return actionCards;
    }

    public ArrayList<Card> getTreasureCardsInHand() {
        ArrayList<Card> treasureCards = new ArrayList<>();
        for (Card card : hand) {
            if (card instanceof TreasureCard) {
                treasureCards.add(card);
            }
        }
        return treasureCards;
    }

    public Card trashCard(String cardToTrash) {
        Card cardToRemove = null;
        for (Card card : hand) {
            if (card.name.equalsIgnoreCase(cardToTrash)) {
                cardToRemove = card;
                break;
            }
        }
        if (cardToRemove == null) {
            return null;
        }
        hand.remove(cardToRemove);

        return cardToRemove;
    }

    public void removeTreasureCardsOfCost(int cost) {
        int goldValue = 3;
        int silverValue = 2;
        int copperValue = 1;

        int coinsRemainingAfterGold = getCoinsAfterRemovingCard(
                cost,
                goldValue,
                bundle.getString("gold"));
        int coinsRemainingAfterSilver = getCoinsAfterRemovingCard(
                coinsRemainingAfterGold,
                silverValue,
                bundle.getString("silver"));
        getCoinsAfterRemovingCard(
                coinsRemainingAfterSilver,
                copperValue,
                bundle.getString("copper"));

    }

    int getCoinsAfterRemovingCard(
            int coinsRemaining,
            int treasuryCardValue,
            String treasureCardType) {
        int index = hasTreasureCardType(treasureCardType);
        while (coinsRemaining >= treasuryCardValue && index > -1) {
            discardPile.add(hand.get(index));
            hand.remove(index);
            coinsRemaining -= treasuryCardValue;
            index = hasTreasureCardType(treasureCardType);
        }
        return coinsRemaining;
    }

    int hasTreasureCardType(String type) {
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
        // emptyRemainingDeck();
        // discardPile.addAll(hand);

        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(discardPile);
        allCards.addAll(hand);
        allCards.addAll(deck.getCards());

        int score = 0;
        for (Card card : allCards) {
            score += card.getVictoryPoints(allCards);
        }
        return score;
    }

    public ArrayList<Card> getCardsInHandExceptOne(String cardToExclude) {
        boolean foundCard = false;
        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : hand) {
            if (card.name.equals(cardToExclude) && !foundCard) {
                foundCard = true;
            } else {
                cards.add(card);
            }
        }

        return cards;
    }
}
