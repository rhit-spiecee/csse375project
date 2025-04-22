package S1G3;

import java.util.Objects;

public abstract class Card {

    public enum CardType {
        TREASURE,
        VICTORY,
        KINGDOM
    }

    final String name;
    final int cost;
    final CardType type;
    final int value;

    public Card(String name, int cost, CardType cardType, int value) {
        this.name = name;
        this.cost = cost;
        this.type = cardType;
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Card card = (Card) object;
        return cost == card.cost && value == card.value && Objects.equals(name, card.name) && type == card.type;
    }
}
