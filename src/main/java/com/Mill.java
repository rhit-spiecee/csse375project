package com;

import java.util.ArrayList;

public class Mill extends KingdomCard {
    private Board board;

    public Mill(Board board) {
        super(Gui.getString("mill"), "mill", 4, 0, 2, Gui.getString("tip.mill"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action += 1;

        if (board.gui.getMillDiscardChoice()) {
            int discardedCount = 0;
            for (int i = 0; i < 2; i++) {
                if (!currentPlayer.hand.isEmpty()) {
                    String cardToDiscard = board.gui.getCardToDiscard(new ArrayList<>(currentPlayer.hand), board.players.indexOf(currentPlayer));
                    if (cardToDiscard != null && !cardToDiscard.isEmpty()) {
                        if (currentPlayer.discardCard(cardToDiscard)) {
                            discardedCount++;
                        }
                    }
                }
            }
            if (discardedCount == 2) {
                currentPlayer.coins += 2;
            }
        }
    }
}
