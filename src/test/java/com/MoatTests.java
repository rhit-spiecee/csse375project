package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoatTests {
    static class StubPlayer extends Player {
        public StubPlayer(Moat moat) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(moat);
        }
    }

    @Test
    public void testCardBehavior() {
        Moat moat = new Moat();
        Player player = new StubPlayer(moat);

        moat.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(2, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }
}
