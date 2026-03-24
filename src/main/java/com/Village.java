package com;

public class Village extends KingdomCard {

    public Village() {
        super(Gui.getString("village"), "village", 3, 0, Gui.getString("tip.village"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action += 2;
    }
}
