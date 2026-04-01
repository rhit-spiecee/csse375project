package com;

import org.junit.Test;

import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class DiplomatTests {

    @Test
    public void testDiplomatHandSizeBoundary() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Board board = new Board(2, Arrays.asList("diplomat"), bundle);
        Player p1 = board.players.get(0);

        p1.action = 0;
        p1.hand.clear();
        for(int i = 0; i < 3; i++) p1.hand.add(new Copper());

        Diplomat diplomat = new Diplomat(board);
        diplomat.useCardPowers(p1);

        assertEquals(2, p1.action);
    }
}