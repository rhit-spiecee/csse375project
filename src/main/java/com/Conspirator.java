package com;

public class Conspirator extends KingdomCard {

    public Conspirator() {
        super(Gui.getString("conspirator"), "conspirator",
                4, 2, 0, Gui.getString("tip.conspirator"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        if (currentPlayer.actionsPlayedThisTurn >= 2) {
            currentPlayer.drawOneCard();
            currentPlayer.action++;
        }
    }
}