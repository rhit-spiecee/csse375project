package S1G3;

import java.util.ArrayList;

public class Market extends KingdomCard {

    public Market() {
        super("market", 5,0);
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins++;
        currentPlayer.action++;
        currentPlayer.buy++;
        currentPlayer.drawOneCard();
    }
}
