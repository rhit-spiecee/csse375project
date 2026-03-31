package com;

public class Pawn extends KingdomCard {
    Board board;

    public Pawn(Board board) {
        super(Gui.getString("pawn"), "pawn", 2, 0, 0, Gui.getString("tip.pawn"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        int[] choices = board.gui.getPawnOptions();

        for (int choice : choices) {
            switch (choice) {
                case 0:
                    currentPlayer.drawOneCard();
                    break;
                case 1:
                    currentPlayer.action += 1;
                    break;
                case 2:
                    currentPlayer.buy += 1;
                    break;
                case 3:
                    currentPlayer.coins += 1;
                    break;
            }
        }
    }
}