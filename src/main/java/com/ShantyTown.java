package com;

public class ShantyTown extends KingdomCard {
    public ShantyTown() {
        super(Gui.getString("shantytown"), 3, 0, Gui.getString("tip.shantytown"));
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.action += 2;
        boolean hasAction = currentPlayer.getHand().stream()
                .anyMatch(c -> c instanceof KingdomCard && c != this);
        if (!hasAction) {
            currentPlayer.drawOneCard();
            currentPlayer.drawOneCard();
        }
    }
}
