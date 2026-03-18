package com;

public class Smithy extends KingdomCard {

    public Smithy() {
        super(Gui.getString("smithy"), 4, 0, Gui.getString("tip.smithy"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        for (int i = 0; i < 3; i++) {
            currentPlayer.drawOneCard();
        }
    }
}
