package com;

import java.util.List;
import java.util.ArrayList;

public class Torturer extends KingdomCard {
    private Board board;

    public Torturer(Board board) {
        super(Gui.getString("torturer"), "torturer", 5, 0, 0, Gui.getString("tip.torturer"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();

        for (Player p : board.players) {
            if (p != currentPlayer) {
                if (p.hasMoatCard()) {
                    int index = board.players.indexOf(p);
                    boolean block = board.gui.getIfPlayerWantsToBlock(index);
                    if (block) {
                        continue;
                    }
                }

                int index = board.players.indexOf(p);
                boolean discardTwo = board.gui.getTorturerChoice(index);

                if (discardTwo) {
                    // Discard 2 cards
                    for (int i = 0; i < 2; i++) {
                        if (p.hand.isEmpty()) break;
                        String cardToDiscard = board.gui.getCardToDiscard(new ArrayList<>(p.hand), index);
                        if (cardToDiscard != null && !cardToDiscard.isEmpty()) {
                            p.discardCard(cardToDiscard);
                        } else {
                            // If they just hit OK with nothing, auto-discard the first to prevent getting stuck
                            p.discardCard(p.hand.get(0));
                        }
                    }
                } else {
                    // Gain a curse into hand
                    board.transferCardFromDeckToPlayer(Gui.getString("cursed"), p);
                }
            }
        }
    }

    @Override
    public List<String> getTypes() {
        List<String> types = super.getTypes();
        types.add("Attack");
        return types;
    }
}
