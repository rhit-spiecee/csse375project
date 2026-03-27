package com;

public class Militia extends KingdomCard {
    private final Board board;

    public Militia(Board board) {
        super(Gui.getString("militia"), "militia", 4, 2, 0, Gui.getString("tip.militia"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        board.forceMilitiaDiscard();
    }
}
