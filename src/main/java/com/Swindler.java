package com;

import java.util.List;
import java.util.ArrayList;

public class Swindler extends KingdomCard {
    private Board board;

    public Swindler(Board board) {
        super(Gui.getString("swindler"), "swindler", 3, 2, 0, Gui.getString("tip.swindler"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        for (int i = 0; i < board.numPlayers; i++) {
            if (i == board.currentPlayerIndex) continue;
            Player victim = board.players.get(i);

            if (victim.hasMoatCard()) {
                if (board.gui.getIfPlayerWantsToBlock(i)) continue;
            }

            if (victim.deck.size() == 0) victim.recycleCards();

            if (victim.deck.size() > 0) {
                Card trashed = victim.deck.draw();
                board.trashPile.add(trashed);

                List<String> replacementOptions = board.getCardsInDeckBelowCostOf(trashed.cost, board.kingdomDecks);

                if (replacementOptions.isEmpty()) {
                    continue;
                }

                String toGain = board.gui.getCardFromAvailableSelection(
                        Gui.getString("swindler.gain.message"),
                        new ArrayList<>(replacementOptions)
                );

                if (!toGain.isEmpty()) {
                    board.transferCardFromDeckToPlayer(toGain, victim);
                }
            }
        }
    }
}