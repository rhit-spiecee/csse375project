package com;

public class Nobles extends KingdomCard {
    private Board board;

    public Nobles(Board board) {
        super(Gui.getString("nobles"), "nobles", 6, 0, 2, Gui.getString("tip.nobles"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        boolean choiceCards = board.gui.getNoblesChoice();
        
        if (choiceCards) {
            currentPlayer.drawOneCard();
            currentPlayer.drawOneCard();
            currentPlayer.drawOneCard();
        } else {
            currentPlayer.action += 2;
        }
    }
}
