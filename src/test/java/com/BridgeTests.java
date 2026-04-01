package com;

import org.junit.Test;
import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class BridgeTests {
    @Test
    public void testBridgePowers() {
        Player p1 = new Player();
        Bridge bridge = new Bridge();

        int initialBuys = p1.getBuys();
        int initialBridgeMod = p1.bridgeMod;

        bridge.useCardPowers(p1);

        assertEquals(initialBuys + 1, p1.getBuys());
        assertEquals(initialBridgeMod + 1, p1.bridgeMod);
    }
}