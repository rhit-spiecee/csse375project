package com;

public class TreasureCard extends Card {
    public TreasureCard(String key, int cost, int coinValue, int victoryPoints) {
        super(Gui.getString(key), key, cost, coinValue, victoryPoints, Gui.getString("tip." + key));
    }
}
