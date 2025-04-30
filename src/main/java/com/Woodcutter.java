package com;

public class Woodcutter extends KingdomCard {


    public Woodcutter() {
        super("woodcutter", 3, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        currentPlayer.buy++;
    }
}
