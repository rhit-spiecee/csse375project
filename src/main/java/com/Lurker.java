package com;

public class Lurker extends KingdomCard {
    Board board;

    public Lurker(Board board) {
        super(Gui.getString("lurker"), "lurker", 2, 0, 0, Gui.getString("tip.lurker"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.action += 1;

        int choice = board.gui.getLurkerOption();
        if (choice == 0) {
            board.trashKingdomCardFromSupply();
        } else if (choice == 1) {
            board.gainKingdomCardFromTrash(currentPlayer);
        }
    }
}