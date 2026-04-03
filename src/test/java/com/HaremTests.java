package com;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HaremTests {

    @Test
    public void testHaremProperties() {
        Harem harem = new Harem();
        
        assertEquals("harem", harem.name);
        assertEquals(6, harem.cost);
        assertEquals(2, harem.coinValue);
        assertEquals(2, harem.getVictoryPoints(Collections.emptyList()));
        
        // Harem should be identified as both Treasure and Victory
        assertTrue(harem.getTypes().contains("Treasure"));
        assertTrue(harem.getTypes().contains("Victory"));
    }
}
