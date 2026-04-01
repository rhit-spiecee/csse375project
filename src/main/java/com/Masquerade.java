package com;

public class Masquerade extends KingdomCard {
    private Board board;

    public Masquerade(Board board) {
        super(Gui.getString("masquerade"), "masquerade", 3, 0, 0, Gui.getString("tip.masquerade"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
        board.executeMasquerade(currentPlayer);
    }
}