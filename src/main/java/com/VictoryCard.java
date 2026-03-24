package com;

public class VictoryCard extends Card {
    public VictoryCard(String key, int cost, int value) {
        super(Gui.getString(key), key, cost, value, Gui.getString("tip." + key));
    }
}
