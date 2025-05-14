package com;

public class Woodcutter extends KingdomCard {


    public Woodcutter(String name) {
        super(name, 3, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        currentPlayer.buy++;
    }
}
