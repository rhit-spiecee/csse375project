package com;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ShantyTownTests {

    static class StubPlayer extends Player {
        public StubPlayer() {
            coins = 0;
            action = 1;
            buy = 1;
        }
    }

    @Test
    public void testNoActionCardsInHand_DrawsTwoCards() {
        ShantyTown shantyTown = new ShantyTown();
        Player player = new StubPlayer();
        player.hand.add(shantyTown);

        shantyTown.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(2, player.action);
        assertEquals(1, player.buy);
        assertEquals(2, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

    @Test
    public void testWithActionCardInHand_DoesNotDraw() {
        ShantyTown shantyTown = new ShantyTown();
        Player player = new StubPlayer();
        player.hand.add(shantyTown);
        player.hand.add(new Woodcutter());

        shantyTown.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(2, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

    @Test
    public void testWithMultipleActionCards_DoesNotDraw() {
        ShantyTown shantyTown = new ShantyTown();
        Player player = new StubPlayer();
        player.hand.add(shantyTown);
        player.hand.add(new Woodcutter());
        player.hand.add(new Woodcutter());

        shantyTown.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(2, player.action);
        assertEquals(1, player.buy);
        assertEquals(2, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

    @Test
    public void testWithOnlyNonActionCards_DrawsTwoCards() {
        ShantyTown shantyTown = new ShantyTown();
        Player player = new StubPlayer();
        player.hand.add(shantyTown);
        player.deck.add(new VictoryCard("copper", 0, 1));
        player.deck.add(new VictoryCard("copper", 0, 1));

        shantyTown.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(2, player.action);
        assertEquals(1, player.buy);
        assertEquals(2, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

    @Test
    public void testEmptyHand_DrawsTwoCards() {
        ShantyTown shantyTown = new ShantyTown();
        Player player = new StubPlayer();
        player.hand.add(shantyTown);

        player.deck.add(new VictoryCard("copper", 0, 1));
        player.deck.add(new VictoryCard("copper", 0, 1));

        shantyTown.useActionCard(player);

        assertEquals(2, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }
}