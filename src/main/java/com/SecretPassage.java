package com;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SecretPassage extends KingdomCard {
    private Board board;

    public SecretPassage(Board board) {
        super(Gui.getString("secretpassage"), "secretpassage", 4, 0, 0, Gui.getString("tip.secretpassage"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
        currentPlayer.action += 1;

        if (!currentPlayer.hand.isEmpty()) {
            ArrayList<String> handNames = currentPlayer.hand.stream()
                    .map(c -> c.name)
                    .collect(Collectors.toCollection(ArrayList::new));

            String cardBackName = board.gui.getCardFromAvailableSelection(
                    Gui.getString("secretpassage.card.query"),
                    handNames
            );

            if (cardBackName != null && !cardBackName.isEmpty()) {
                Card cardToPutBack = null;
                for (Card c : currentPlayer.hand) {
                    if (c.name.equals(cardBackName)) {
                        cardToPutBack = c;
                        break;
                    }
                }

                if (cardToPutBack != null) {
                    int pos = board.gui.getSecretPassagePosition(currentPlayer.deck.size());
                    currentPlayer.putCardInDeck(cardToPutBack, pos);
                }
            }
        }
    }
}
