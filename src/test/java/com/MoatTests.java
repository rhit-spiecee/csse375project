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
        int initialHandSize = player.hand.size();

        moat.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        // Hand size: initial(1) - 1(moat played) + 2(moat power) = 2
        assertEquals(initialHandSize + 1, player.hand.size());
        assertEquals(1, player.discardPile.size());
        assertEquals(moat, player.discardPile.get(0));
    }

    @Test
    public void testMoatTypes() {
        Moat moat = new Moat();
        java.util.List<String> types = moat.getTypes();
        
        assertEquals(2, types.size());
        assertEquals("Action", types.get(0));
        assertEquals("Reaction", types.get(1));
    }
}
