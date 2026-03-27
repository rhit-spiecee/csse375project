package com;

public class VictoryCard extends Card {
    public VictoryCard(String key, int cost, int coinValue, int victoryPoints) {
        super(Gui.getString(key), key, cost, coinValue, victoryPoints, Gui.getString("tip." + key));
    }
}
