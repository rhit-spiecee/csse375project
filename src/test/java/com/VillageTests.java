package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VillageTests {
    static class StubPlayer extends Player {
        public StubPlayer(Card card) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(card);
        }
    }

    @Test
    public void testCardBehavior() {
        Village village = new Village("village");
        Player player = new StubPlayer(village);

        village.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(2, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }
}
