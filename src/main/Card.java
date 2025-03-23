public class Card {

    public enum CardType {
        TREASURE,
        VICTORY,
        KINGDOM
    }

    private String name;
    private int cost;
    private CardType cardType;
    private int value;

    public Card(String name, int cost, CardType cardType, int value) {
        this.name = name;
        this.cost = cost;
        this.cardType = cardType;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getValue() {
        return value;
    }

}
