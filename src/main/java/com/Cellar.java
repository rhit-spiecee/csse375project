package com;

public class Cellar extends KingdomCard {
    Board board;

    public Cellar(Board board, String name) {
        super(name, 2, 0);
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
