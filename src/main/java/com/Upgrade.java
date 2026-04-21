package com;

import java.util.ArrayList;
import java.util.List;

public class Upgrade extends KingdomCard {
    private Board board;

    public Upgrade(Board board) {
        super(Gui.getString("upgrade"), "upgrade", 5, 0, 0, Gui.getString("tip.upgrade"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action += 1;

        if (currentPlayer.hand.isEmpty()) {
            return;
        }

        ArrayList<String> handNames = new ArrayList<>();
        for (Card c : currentPlayer.hand) {
            handNames.add(c.name);
        }

        String cardToTrashName = board.gui.getCardFromAvailableSelection(
                Gui.getString("upgrade.trash.query"),
                handNames
        );

        Card trashedCard = currentPlayer.trashCard(cardToTrashName);
        if (trashedCard != null) {
            board.trashPile.add(trashedCard);
            
            int exactCost = trashedCard.cost + 1;
            List<String> availableToGain = getCardsInDeckExactCost(exactCost, board.kingdomDecks);
            availableToGain.addAll(getCardsInDeckExactCost(exactCost, board.treasureDecks));
            availableToGain.addAll(getCardsInDeckExactCost(exactCost, board.victoryDecks));
            
            if (!availableToGain.isEmpty()) {
                String cardToGainName = board.gui.getCardFromAvailableSelection(
                        java.text.MessageFormat.format(Gui.getString("upgrade.gain.query"), exactCost),
                        new ArrayList<>(availableToGain)
                );

                if (cardToGainName != null && !cardToGainName.isEmpty()) {
                    BoardDeck deck = board.getBoardDeckByName(cardToGainName);
                    if (deck != null) {
                        currentPlayer.discardPile.add(deck.pickUpCard());
                    }
                }
            }
        }
    }
    
    private List<String> getCardsInDeckExactCost(int exactCost, java.util.Map<String, BoardDeck> decks) {
        List<String> exactCostCards = new ArrayList<>();
        int discount = board.players.get(board.currentPlayerIndex).bridgeMod;

        for (java.util.Map.Entry<String, BoardDeck> entry : decks.entrySet()) {
            BoardDeck boardDeck = entry.getValue();
            Card card = boardDeck.getCard();
            
            int baseCost = card.cost;
            int costAfterDiscount = baseCost - discount;
            int effectiveCost = Math.max(0, costAfterDiscount);

            if (effectiveCost == exactCost) {
                if (boardDeck.size() > 0) {
                    exactCostCards.add(entry.getKey());
                }
            }
        }
        return exactCostCards;
    }
}
