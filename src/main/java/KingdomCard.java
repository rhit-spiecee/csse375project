public abstract class KingdomCard extends Card{
    Player currentPlayer;
    public KingdomCard(String name, int cost, int value) {
        super(name, cost, CardType.KINGDOM, value);
    }

    public abstract void useActionCard();
}
