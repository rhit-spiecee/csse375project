package com;

public class Courtyard extends KingdomCard {
    public Courtyard() {
        super(Gui.getString("courtyard"), 2, 0, Gui.getString("tip.courtyard"));
    }

    @Override
    public void useCardPowers(Player currentPlayer){
        for (int i = 0; i < 3; i++){
            currentPlayer.drawOneCard();

        }
    }
}
