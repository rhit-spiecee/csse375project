package com;

public abstract class KingdomCard extends Card {
    public KingdomCard(String name, int cost, int value, String gameTip) {
        super(name, cost, value, gameTip);
    }

    public void useActionCard(Player currentPlayer) {
        useCardPowers(currentPlayer);
        currentPlayer.action--;
        currentPlayer.discardCard(this);
    }

    public abstract void useCardPowers(Player currentPlayer);
}
