package com;

public class Workshop extends KingdomCard {
    Board board;

    public Workshop(Board board) {
        super("workshop", 3, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        board.gainAnyCard(currentPlayer, 4);
    }
}
