package com;

public class TreasureCard extends Card {
    public TreasureCard(String key, int cost, int value) {
        super(Gui.getString(key), cost, value, Gui.getString("tip." + key));
    }
}
