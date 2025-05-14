package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarketTests {

    static class StubPlayer extends Player {
        public StubPlayer(Market market) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(market);
        }
    }

    @Test
    public void testCardBehavior() {
        Market market = new Market("market");
        Player player = new StubPlayer(market);

        market.useActionCard(player);

        assertEquals(1, player.coins);
        assertEquals(1, player.action);
        assertEquals(2, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

}
