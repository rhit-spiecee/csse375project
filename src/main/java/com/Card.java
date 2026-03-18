package com;

import java.util.Objects;

public abstract class Card {

    final String name;
    final int cost;
    final int value;
    final String gameTip;

    public Card(String name, int cost, int value, String gameTip) {
        this.name = name;
        this.cost = cost;
        this.value = value;
        this.gameTip = gameTip;
    }

    public String getGameTip() {
        return gameTip;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Card card = (Card) object;
        return cost == card.cost 
                && value == card.value
                && Objects.equals(name, card.name)
                && Objects.equals(gameTip, card.gameTip);
    }
}
