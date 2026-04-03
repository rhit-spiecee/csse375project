package com;

import org.junit.Test;

import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class ConspiratorTests {

    @Test
    public void testConspiratorBonusTrigger() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Board board = new Board(2, Arrays.asList("conspirator"), bundle);
        Player p1 = board.players.get(0);

        p1.action = 0;
        p1.actionsPlayedThisTurn = 2;
        Conspirator conspirator = new Conspirator();

        conspirator.useCardPowers(p1);
        assertEquals(1, p1.action);
        assertEquals(6,p1.hand.size());
        assertEquals(2, p1.coins);
    }
}
