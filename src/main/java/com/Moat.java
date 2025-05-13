package com;

public class Moat extends KingdomCard {

    public Moat(String name) {
        super(name, 2, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
    }
}
