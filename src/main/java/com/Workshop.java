package com;

public class Workshop extends KingdomCard {
    Board board;

    public Workshop(Board board) {
        super(Gui.getString("workshop"), "workshop", 3, 0, Gui.getString("tip.workshop"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        board.gainAnyCard(currentPlayer, 4);
    }
}
