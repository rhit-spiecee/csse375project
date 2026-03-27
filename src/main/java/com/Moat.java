package com;

public class Moat extends KingdomCard {

    public Moat() {
        super(Gui.getString("moat"), "moat", 2, 0, 0, Gui.getString("tip.moat"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
    }
}
