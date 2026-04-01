package com;

public abstract class KingdomCard extends Card {
    public KingdomCard(String name, String imageId, int cost, int coinValue, int victoryPoints, String gameTip) {
        super(name, imageId, cost, coinValue, victoryPoints, gameTip);
    }

    public void useActionCard(Player currentPlayer) {
        useCardPowers(currentPlayer);
        currentPlayer.action--;
        currentPlayer.actionsPlayedThisTurn++;
        currentPlayer.discardCard(this);
    }

    public abstract void useCardPowers(Player currentPlayer);
}
