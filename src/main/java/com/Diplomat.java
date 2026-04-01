package com;
public class Diplomat extends KingdomCard {
    public Diplomat(Board board) {
        super(Gui.getString("diplomat"), "diplomat", 4, 0, 0, Gui.getString("tip.diplomat"));
    }
    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.drawOneCard();
        if (currentPlayer.hand.size() <= 5) {
            currentPlayer.action += 2;
        }
    }
}