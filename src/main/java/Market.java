public class Market extends KingdomCard {

    public Market() {
        super("market", 5,0);
    }

    @Override
    public void useActionCard() {
        currentPlayer.coins++;
        currentPlayer.action++;
        currentPlayer.buy++;
        currentPlayer.drawOneCard();
    }
}
