package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

public class CellarTests {
    static class StubPlayer extends Player {
        public StubPlayer(Cellar cellar) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(cellar);
            hand.add(new TreasureCard("copper", 6, 3));
            hand.add(new TreasureCard("copper", 6, 3));
            hand.add(new TreasureCard("copper", 6, 3));
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
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
        Cellar cellar = new Cellar(board, "cellar");
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
