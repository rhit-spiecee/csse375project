package com;

public abstract class KingdomCard extends Card {
    public KingdomCard(String name, String imageId, int cost, int value, String gameTip) {
        super(name, imageId, cost, value, gameTip);
    }

    public void useActionCard(Player currentPlayer) {
        useCardPowers(currentPlayer);
        currentPlayer.action--;
        currentPlayer.discardCard(this);
    }

    public abstract void useCardPowers(Player currentPlayer);
}
