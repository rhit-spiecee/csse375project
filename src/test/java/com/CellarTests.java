package com;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CellarTests {
    static class StubPlayer extends Player {
        public StubPlayer(Cellar cellar) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(cellar);
            hand.add(new TreasureCard("copper", 6, Card.CardType.TREASURE, 3));
            hand.add(new TreasureCard("copper", 6, Card.CardType.TREASURE, 3));
            hand.add(new TreasureCard("copper", 6, Card.CardType.TREASURE, 3));
        }
    }

    @Test
    public void testCardBehavior() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(0).times(3);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("copper").times(3);
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(1);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("province").times(3);

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        Cellar cellar = new Cellar(board);
        Player player = new StubPlayer(cellar);

        cellar.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(3, player.hand.size());
        assertEquals(4, player.discardPile.size());

        EasyMock.verify(mockGui);
    }
}
