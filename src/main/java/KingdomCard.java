public abstract class KingdomCard {
    Player currentPlayer;

    public KingdomCard(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public abstract void useActionCard();
}
