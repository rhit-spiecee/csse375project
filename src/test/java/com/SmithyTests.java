package com;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class SmithyTests {

    static class StubPlayer extends Player {
        public StubPlayer(Smithy smithy) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(smithy);
        }
    }

    @Test
    public void testCardBehavior() {
        Smithy smithy = new Smithy("smithy");
        Player player = new StubPlayer(smithy);

        smithy.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(3, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

}

