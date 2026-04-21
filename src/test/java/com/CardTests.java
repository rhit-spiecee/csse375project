package com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class CardTests {

    @Test
    public void testGetGameTip() {
        Card market = new Market();
        Card smithy = new Smithy();
        
        assertEquals("Provides $+1, +1 Buy, +1 Action, and +1 Card.", market.getGameTip());
        assertEquals("Draw 3 cards.", smithy.getGameTip());
    }

    @Test
    public void testGenericCardTips() {
        Card copper = new Copper();
        Card estate = new VictoryCard("estate", 2, 0, 1);
        
        assertEquals("Worth $+1.", copper.getGameTip());
        assertEquals("Worth 1 Victory Point.", estate.getGameTip());
    }

    @Test
    public void testEqualsWithTips() {
        Card card1 = new Copper();
        Card card2 = new Copper();
        Card card3 = new Silver();
        
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(card1, null);
        assertNotEquals(card1, "not a card");
    }

    @Test
    public void testEqualsDetailed() {
        // Different name
        Card card1 = new VictoryCard("estate", 2, 0, 1);
        Card card2 = new VictoryCard("duchy", 5, 0, 3);
        assertNotEquals(card1, card2);

        // Different cost
        Card card3 = new VictoryCard("estate", 3, 0, 1);
        assertNotEquals(card1, card3);

        // Different coinValue (using TreasureCard)
        Card card4 = new Copper(); // Cost 0, Val 1
        Card card5 = new TreasureCard("silver", 3, 2, 0); 
        assertNotEquals(card4, card5);

        // Different victoryPoints
        Card card6 = new VictoryCard("estate", 2, 0, 2);
        assertNotEquals(card1, card6);
    }

    @Test
    public void testGetTypesDiverse() {
        Card estate = new VictoryCard("estate", 2, 0, 1);
        Card mill = new Mill(null); // Action-Victory
        Card silver = new Silver(); // Treasure
        
        assertTrue(estate.getTypes().contains("Victory"));
        assertFalse(estate.getTypes().contains("Action"));
        
        assertTrue(mill.getTypes().contains("Action"));
        assertTrue(mill.getTypes().contains("Victory"));
        
        assertTrue(silver.getTypes().contains("Treasure"));
    }
}
