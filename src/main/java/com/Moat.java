package com;

public class Moat extends KingdomCard {

    public Moat() {
        super("moat", 2, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
    }
}
