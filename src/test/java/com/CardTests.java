package com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
    }
}
