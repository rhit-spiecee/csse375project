package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class CellarTests {

    private static final List<String> FIXED_TEST_CARDS = Arrays.asList(
            "cellar", "market", "militia", "mine", "moat",
            "remodel", "smithy", "village", "workshop", "woodcutter");

    private Board createFixedBoard(Gui gui) {
        Board board = new Board(gui.getNumPlayers(), FIXED_TEST_CARDS, gui.getBundle());
        board.gui = gui;
        return board;
    }

    static class StubPlayer extends Player {
        public StubPlayer(Cellar cellar) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(cellar);
            hand.add(new TreasureCard("copper", 6, 3, 0));
            hand.add(new TreasureCard("copper", 6, 3, 0));
            hand.add(new TreasureCard("copper", 6, 3, 0));
        }
    }

    @Test
    public void testCardBehavior() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(0).times(3);
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.notNull(), EasyMock.anyInt())).andReturn("");
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.notNull(), EasyMock.anyInt())).andReturn("copper").times(3);
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(1);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Language.ENGLISH.bundleName));

        EasyMock.replay(mockGui);
        Board board = createFixedBoard(mockGui);
        Cellar cellar = new Cellar(board);
        Player player = new StubPlayer(cellar);

        cellar.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(1, player.action);
        assertEquals(1, player.buy);
        assertEquals(3, player.hand.size());
        assertEquals(4, player.discardPile.size());

        EasyMock.verify(mockGui);
    }
}
