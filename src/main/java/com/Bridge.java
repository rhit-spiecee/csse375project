package com;

public class Bridge extends KingdomCard {
    public Bridge() {
        super(Gui.getString("bridge"), "bridge", 4, 1, 0, Gui.getString("tip.bridge"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.buy++;
        currentPlayer.coins++;
        currentPlayer.bridgeMod++;
    }
}