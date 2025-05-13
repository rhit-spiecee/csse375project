package com;

public class Remodel extends KingdomCard {
    Board board;

    public Remodel(Board board, String name) {
        super(name, 4, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        Card trashedCard = board.trashAnyCard(currentPlayer);
        board.gainAnyCard(currentPlayer, trashedCard.cost + 2);
    }
}
