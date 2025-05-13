package com;

public class Market extends KingdomCard {

    public Market(String name) {
        super(name, 5, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins++;
        currentPlayer.action++;
        currentPlayer.buy++;
        currentPlayer.drawOneCard();
    }
}
