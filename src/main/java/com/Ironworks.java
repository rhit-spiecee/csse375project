package com;

import java.util.ArrayList;
import java.util.List;

public class Ironworks extends KingdomCard {
    private Board board;

    public Ironworks(Board board) {
        super(Gui.getString("ironworks"), "ironworks", 4, 0, 0, Gui.getString("tip.ironworks"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        List<String> options = board.getCardsInDeckBelowCostOf(4, board.kingdomDecks);

        String gained = board.gui.getCardFromAvailableSelection(
                Gui.getString("ironworks.gain"),
                new ArrayList<>(options)
        );

        if (!gained.isEmpty()) {
            Card card = board.getBoardDeckByName(gained).getCard();
            board.transferCardFromDeckToPlayer(gained, currentPlayer);

            if (card instanceof KingdomCard) currentPlayer.action++;
            if (card instanceof TreasureCard) currentPlayer.coins++;
            if (card instanceof VictoryCard) currentPlayer.drawOneCard();
        }
    }
}