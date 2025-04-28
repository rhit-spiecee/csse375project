package com;

public abstract class KingdomCard extends Card {
    public KingdomCard(String name, int cost, int value) {
        super(name, cost, CardType.KINGDOM, value);
    }

    public void useActionCard(Player currentPlayer) {
        useCardPowers(currentPlayer);
        currentPlayer.action--;
        currentPlayer.discardCard(this);
    }

    public abstract void useCardPowers(Player currentPlayer);
}
