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

        if (cardToTrashName == null || cardToTrashName.isEmpty()) {
            return;
        }

        Card trashedCard = currentPlayer.trashCard(cardToTrashName);
        if (trashedCard != null) {
            board.trashPile.add(trashedCard);
            
            int exactCost = trashedCard.cost + 1;
            List<String> availableToGain = getCardsInDeckExactCost(exactCost, board.kingdomDecks);
            availableToGain.addAll(getCardsInDeckExactCost(exactCost, board.treasureDecks));
            availableToGain.addAll(getCardsInDeckExactCost(exactCost, board.victoryDecks));
            
            if (availableToGain.isEmpty()) return;

            String cardToGainName = board.gui.getCardFromAvailableSelection(
                    java.text.MessageFormat.format(Gui.getString("upgrade.gain.query"), exactCost),
                    new ArrayList<>(availableToGain)
            );

            if (cardToGainName != null && !cardToGainName.isEmpty()) {
                // By default put into discard unless `transferCard` puts it into hand.
                // Dominion rules for Upgrade say "Gain a card", meaning default placement is discard.
                // But in this implementation, transferCardFromDeckToPlayer puts it into hand!
                // To be consistent with "bought" cards which go to discard (or what players expect),
                // wait, players' `addBoughtCard` adds to discard via boughtPile.
                // Let's just use `addBoughtCard` or `transferCardFromDeckToPlayer` depending on `Gui` intent.
                // Normally gaining goes to discard. In `transferCardFromDeckToPlayer`, it goes to hand directly.
                // Let's follow standard dominion rules: gain goes to discard pile.
                BoardDeck deck = board.getBoardDeckByName(cardToGainName);
                Card gainedCard = deck.pickUpCard();
                currentPlayer.discardPile.add(gainedCard);
            }
        }
    }
    
    private List<String> getCardsInDeckExactCost(int exactCost, java.util.Map<String, BoardDeck> decks) {
        List<String> exactCostCards = new ArrayList<>();
        int discount = board.players.get(board.currentPlayerIndex).bridgeMod;

        for (java.util.Map.Entry<String, BoardDeck> entry : decks.entrySet()) {
            Card card = entry.getValue().getCard();
            int effectiveCost = Math.max(0, card.cost - discount);

            if (effectiveCost == exactCost && entry.getValue().size() > 0) {
                exactCostCards.add(entry.getKey());
            }
        }
        return exactCostCards;
    }
}
