package com;

public class Market extends KingdomCard {

    public Market() {
        super(Gui.getString("market"), "market", 5, 0, Gui.getString("tip.market"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins++;
        currentPlayer.action++;
        currentPlayer.buy++;
        currentPlayer.drawOneCard();
    }
}
