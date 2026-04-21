package com;

import java.util.Objects;

public abstract class Card {

    final String name;
    final String imageId;
    final int cost;
    final int coinValue;
    final int victoryPoints;
    final String gameTip;

    public Card(String name, String imageId, int cost, int coinValue, int victoryPoints, String gameTip) {
        this.name = name;
        this.imageId = imageId;
        this.cost = cost;
        this.coinValue = coinValue;
        this.victoryPoints = victoryPoints;
        this.gameTip = gameTip;
    }

    public String getGameTip() {
        return gameTip;
    }

    public java.util.List<String> getTypes() {
        java.util.List<String> types = new java.util.ArrayList<>();
        if (this instanceof KingdomCard) {
            types.add("Action");
        }
        if (this instanceof TreasureCard) {
            types.add("Treasure");
        }
        if (this instanceof VictoryCard) {
            types.add("Victory");
        } else if (this.victoryPoints != 0) {
            types.add("Victory");
        }
        return types;
    }

    public int getVictoryPoints(java.util.List<Card> allCards) {
        return victoryPoints;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        Card card = (Card) object;
        boolean sameCost = (cost == card.cost);
        boolean sameCoin = (coinValue == card.coinValue);
        boolean sameVictory = (victoryPoints == card.victoryPoints);
        boolean sameName = Objects.equals(name, card.name);
        
        if (!sameCost) {
            return false;
        }
        if (!sameCoin) {
            return false;
        }
        if (!sameVictory) {
            return false;
        }
        if (!sameName) {
            return false;
        }
        
        return true;
    }
}
