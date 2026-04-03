package com;

public class WishingWell extends KingdomCard {
    private Board board;

    public WishingWell(Board board) {
        super(Gui.getString("wishingwell"), "wishingwell", 3, 0, 0, Gui.getString("tip.wishingwell"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action++;

        String guess = board.gui.getWishingWellGuess(Board.ALL_KINGDOM_CARD_IDS);

        if (currentPlayer.deck.size() > 0) {
            Card top = currentPlayer.deck.draw();
            if (top.name.equalsIgnoreCase(guess)) {
                currentPlayer.hand.add(top);
            } else {
                currentPlayer.deck.addTop(top);
            }
        }
    }
}