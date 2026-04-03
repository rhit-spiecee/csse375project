package com;

import java.util.ArrayList;

public class TradingPost extends KingdomCard {
    private Board board;

    public TradingPost(Board board) {
        super(Gui.getString("tradingpost"), "tradingpost", 5, 0, 0, Gui.getString("tip.tradingpost"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        int trashedCount = 0;
        
        for (int i = 0; i < 2; i++) {
            if (currentPlayer.hand.isEmpty()) {
                break;
            }

            ArrayList<String> handNames = new ArrayList<>();
            for (Card c : currentPlayer.hand) {
                handNames.add(c.name);
            }

            String message = java.text.MessageFormat.format(Gui.getString("tradingpost.trash.query"), 2 - i);

            String cardToTrashName = board.gui.getCardFromAvailableSelection(
                    message,
                    handNames
            );

            if (cardToTrashName != null && !cardToTrashName.isEmpty()) {
                Card trashedCard = currentPlayer.trashCard(cardToTrashName);
                if (trashedCard != null) {
                    board.trashPile.add(trashedCard);
                    trashedCount++;
                }
            } else {
                // Trading Post is mandatory if you play it.
                // To prevent infinite loop if user hits cancel repeatedly, 
                // we'll just force trash the first card.
                Card trashedCard = currentPlayer.trashCard(currentPlayer.hand.get(0).name);
                if (trashedCard != null) {
                    board.trashPile.add(trashedCard);
                    trashedCount++;
                }
            }
        }

        if (trashedCount == 2) {
            board.transferCardFromDeckToPlayer(Gui.getString("silver"), currentPlayer);
        }
    }
}
