package com;

import java.util.ArrayList;
import java.util.List;

public class Courtier extends KingdomCard {
    private Board board;

    public Courtier(Board board) {
        super(Gui.getString("courtier"), "courtier", 5, 0, 0, Gui.getString("tip.courtier"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        if (currentPlayer.hand.isEmpty()) {
            return;
        }

        ArrayList<String> handNames = new ArrayList<>();
        for (Card c : currentPlayer.hand) {
            handNames.add(c.name);
        }

        String revealedCardName = board.gui.getCardFromAvailableSelection(
                Gui.getString("courtier.reveal.message"),
                handNames
        );

        if (revealedCardName == null || revealedCardName.isEmpty()) {
            return;
        }

        Card revealedCard = null;
        for (Card c : currentPlayer.hand) {
            if (c.name.equals(revealedCardName)) {
                revealedCard = c;
                break;
            }
        }

        if (revealedCard != null) {
            List<String> types = revealedCard.getTypes();
            int numChoices = Math.min(types.size(), 4);

            if (numChoices > 0) {
                List<Integer> selectedOptions = board.gui.getCourtierOptions(numChoices);

                for (Integer option : selectedOptions) {
                    switch (option) {
                        case 0: // +1 Action
                            currentPlayer.action += 1;
                            break;
                        case 1: // +1 Buy
                            currentPlayer.buy += 1;
                            break;
                        case 2: // +$3
                            currentPlayer.coins += 3;
                            break;
                        case 3: // Gain a Gold
                            board.transferCardFromDeckToPlayer(Gui.getString("gold"), currentPlayer);
                            break;
                    }
                }
            }
        }
    }
}
