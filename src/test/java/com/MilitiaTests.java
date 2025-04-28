package com;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MilitiaTests {
    static class StubPlayer extends Player {
        public StubPlayer(Militia militia) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(militia);
        }
    }

    @Test
    public void testCardBehavior() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.notNull())).andReturn("copper").times(2);
        
        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        Militia militia = new Militia(board);
        Player player = new StubPlayer(militia);

        militia.useActionCard(player);

        assertEquals(2, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(0, player.hand.size());
        assertEquals(1, player.discardPile.size());
        
        EasyMock.verify(mockGui);
    }
}
