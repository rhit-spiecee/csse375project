package com;

public class Woodcutter extends KingdomCard {


    public Woodcutter() {
        super(Gui.getString("woodcutter"), "woodcutter", 3, 2, 0, Gui.getString("tip.woodcutter"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        currentPlayer.buy++;
    }
}
