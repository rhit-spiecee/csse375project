package com;

public class Mine extends KingdomCard {
    Board board;

    public Mine(Board board) {
        super(Gui.getString("mine"), 5, 0, Gui.getString("tip.mine"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        Card trashedCard = board.trashTreasureCard(currentPlayer);
        board.gainTreasureCard(currentPlayer, trashedCard);
    }
}
