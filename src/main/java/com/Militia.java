package com;

public class Militia extends KingdomCard {
    private final Board board;

    public Militia(Board board, String name) {
        super(name, 4, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        board.forceMilitiaDiscard();
    }
}
