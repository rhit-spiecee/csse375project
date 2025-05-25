package com;

import java.util.Objects;

public abstract class Card {

    final String name;
    final int cost;
    final int value;

    public Card(String name, int cost, int value) {
        this.name = name;
        this.cost = cost;
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Card card = (Card) object;
        return cost == card.cost 
                && value == card.value
                && Objects.equals(name, card.name);
    }
}
