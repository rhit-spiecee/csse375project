package com;

public class Mine extends KingdomCard {
    Board board;

    public Mine(Board board, String name) {
        super(name, 5, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        Card trashedCard = board.trashTreasureCard(currentPlayer);
        board.gainTreasureCard(currentPlayer, trashedCard);
    }
}
