package com;

public class Cellar extends KingdomCard {
    Board board;

    public Cellar(Board board) {
        super("cellar", 2, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        int numCardsDiscarded = board.discardAnyNumberOfCards(currentPlayer);
        board.gainCards(numCardsDiscarded, currentPlayer);
    }
}
