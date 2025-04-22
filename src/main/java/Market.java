public class Market extends KingdomCard {

    public Market(Player currentPlayer) {
        super(currentPlayer);
    }

    @Override
    public void useActionCard() {
        currentPlayer.coins++;
        currentPlayer.action++;
        currentPlayer.buy++;
        currentPlayer.drawOneCard();
    }
}
