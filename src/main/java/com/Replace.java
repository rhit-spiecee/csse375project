package com;

import java.util.ArrayList;
import java.util.List;

public class Replace extends KingdomCard {
    private Board board;

    public Replace(Board board) {
        super(Gui.getString("replace"), "replace", 5, 0, 0, Gui.getString("tip.replace"));
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

        String cardToTrashName = board.gui.getCardFromAvailableSelection(
                Gui.getString("replace.trash.query"),
                handNames
        );

        if (cardToTrashName == null || cardToTrashName.isEmpty()) {
            return;
        }

        Card trashedCard = currentPlayer.trashCard(cardToTrashName);
        if (trashedCard != null) {
            board.trashPile.add(trashedCard);
            
            int maxCost = trashedCard.cost + 2;
            List<String> availableToGain = board.getCardsInDeckBelowCostOf(maxCost, board.kingdomDecks);
            availableToGain.addAll(board.getCardsInDeckBelowCostOf(maxCost, board.treasureDecks));
            availableToGain.addAll(board.getCardsInDeckBelowCostOf(maxCost, board.victoryDecks));
            
            if (availableToGain.isEmpty()) return;

            String cardToGainName = board.gui.getCardFromAvailableSelection(
                    java.text.MessageFormat.format(Gui.getString("replace.gain.query"), 2),
                    new ArrayList<>(availableToGain)
            );

            if (cardToGainName != null && !cardToGainName.isEmpty()) {
                BoardDeck deck = board.getBoardDeckByName(cardToGainName);
                Card gainedCard = deck.pickUpCard();
                
                List<String> types = gainedCard.getTypes();
                boolean isAction = types.contains("Action");
                boolean isTreasure = types.contains("Treasure");
                
                if (isAction) {
                    currentPlayer.deck.addTop(gainedCard);
                } else {
                    if (isTreasure) {
                        currentPlayer.deck.addTop(gainedCard);
                    } else {
                        currentPlayer.discardPile.add(gainedCard);
                    }
                }
                
                if (types.contains("Victory")) {
                    for (Player p : board.players) {
                        if (p != currentPlayer) {
                            if (p.hasMoatCard()) {
                                int index = board.players.indexOf(p);
                                if (board.gui.getIfPlayerWantsToBlock(index)) continue;
                            }
                            // Gain curse to discard: more direct logic
                            BoardDeck curseDeck = board.getBoardDeckByName(Gui.getString("cursed"));
                            if (curseDeck.size() > 0) {
                                p.discardPile.add(curseDeck.pickUpCard());
                            }
                        }
                    }
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
