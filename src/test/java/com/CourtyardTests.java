package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CourtyardTests {

    static class StubPlayer extends Player {
        public StubPlayer(Courtyard courtyard) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(courtyard);
        }
    }

    @Test
    public void testCardBehavior() {
        Courtyard courtyard = new Courtyard();
        Player player = new StubPlayer(courtyard);

        courtyard.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(3, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

}
