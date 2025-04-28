package com;

public class Smithy extends KingdomCard {

    public Smithy() {
        super("smithy", 4, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        for (int i = 0; i < 3; i++) {
            currentPlayer.drawOneCard();
        }
    }
}
