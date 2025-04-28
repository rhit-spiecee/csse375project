package com;

public class Village extends KingdomCard {

    public Village() {
        super("village", 3, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action += 2;
    }
}
