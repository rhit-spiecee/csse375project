package com;

import java.util.ArrayList;

public class Patrol extends KingdomCard {
    private Board board;

    public Patrol(Board board) {
        super(Gui.getString("patrol"), "patrol", 5, 0, 0, Gui.getString("tip.patrol"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();

        ArrayList<Card> revealedCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (currentPlayer.deck.size() == 0) {
                currentPlayer.recycleCards();
            }
            if (currentPlayer.deck.size() > 0) {
                revealedCards.add(currentPlayer.deck.draw());
            }
        }

        ArrayList<Card> cardsToPutBack = new ArrayList<>();

        for (Card card : revealedCards) {
            if (card instanceof VictoryCard || card.name.equals(Gui.getString("cursed"))) {
                currentPlayer.hand.add(card);
            } else {
                cardsToPutBack.add(card);
            }
        }

        while (!cardsToPutBack.isEmpty()) {
            if (cardsToPutBack.size() == 1) {
                currentPlayer.deck.addTop(cardsToPutBack.remove(0));
            } else {
                ArrayList<String> cardNames = new ArrayList<>();
                for (Card c : cardsToPutBack) {
                    cardNames.add(c.name);
                }

                String chosenCardName = board.gui.getCardFromAvailableSelection(
                        Gui.getString("patrol.order.query"),
                        cardNames
                );

                Card chosenCard = null;
                for (Card c : cardsToPutBack) {
                    if (c.name.equals(chosenCardName)) {
                        chosenCard = c;
                        break;
                    }
                }

                // If user cancels, just pick the first one to avoid infinite loop
                if (chosenCard == null) {
                    chosenCard = cardsToPutBack.get(0);
                }

                cardsToPutBack.remove(chosenCard);
                currentPlayer.deck.addTop(chosenCard);
            }
        }
    }
}
