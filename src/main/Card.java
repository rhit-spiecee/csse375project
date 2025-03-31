public class Card {

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

}
