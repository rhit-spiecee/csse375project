package S1G3;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MarketTests {

    static class StubPlayer extends Player {}

    @Test
    public void testCardBehavior() {
        Market market = new Market();
        market.currentPlayer = new StubPlayer();
        market.currentPlayer.coins = 0;
        market.currentPlayer.action = 1;
        market.currentPlayer.buy = 1;
        market.currentPlayer.hand = new ArrayList<Card>();
        market.currentPlayer.hand.add(market);

        market.useActionCard();

        assertEquals(1, market.currentPlayer.coins);
        assertEquals(2, market.currentPlayer.action);
        assertEquals(2, market.currentPlayer.buy);
        assertEquals(2, market.currentPlayer.hand.size());

    }

}
