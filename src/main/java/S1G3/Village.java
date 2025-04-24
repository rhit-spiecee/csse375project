package S1G3;

public class Village extends KingdomCard {

    public Village() {
        super("village", 3, 0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action += 2;
    }
}
