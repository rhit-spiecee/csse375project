package com;

public class Cellar extends KingdomCard {
    Board board;

    public Cellar(Board board) {
        super(Gui.getString("cellar"), "cellar", 2, 0, Gui.getString("tip.cellar"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.action++;
        int numCardsDiscarded = board.discardAnyNumberOfCards(currentPlayer);
        for (int i = 0; i < numCardsDiscarded; i++) {
            currentPlayer.drawOneCard();
        }
    }
}
