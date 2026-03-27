package com;

public class Remodel extends KingdomCard {
    Board board;

    public Remodel(Board board) {
        super(Gui.getString("remodel"), "remodel", 4, 0, 0, Gui.getString("tip.remodel"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        Card trashedCard = board.trashAnyCard(currentPlayer);
        board.gainAnyCard(currentPlayer, trashedCard.cost + 2);
    }
}
