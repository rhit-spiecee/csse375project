package com;

import java.util.Stack;

public class BoardDeck { 
    final Stack<Card> deck = new Stack<>();
    final Card card;

    public BoardDeck(Card card, int size) {
        if (size < 8 || size > 60) {
            throw new RuntimeException("Size must be at least 8 and at most 60");
        }
        for (int i = 0; i < size; i++) {
            deck.add(card);
        }
        this.card = card;
    }

    public int size() {
        return deck.size();
    }

    public Card getCard() {
        return card;
    }

    public Card buyCard() {
        if (deck.isEmpty()) {
            throw new RuntimeException("Deck is empty");
        }
        return deck.pop();
    }

    public boolean isNotEmpty() {
        return !deck.isEmpty();
    }
}
