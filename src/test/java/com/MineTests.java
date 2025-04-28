package com;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MineTests {
    static class StubPlayer extends Player {
        public StubPlayer(Mine mine) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(mine);
            hand.add(new TreasureCard("silver", 3, Card.CardType.TREASURE, 2));
        }
    }

    @Test
    public void testCardBehavior() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("silver");
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("gold");

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        Mine mine = new Mine(board);
        Player player = new StubPlayer(mine);

        mine.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }
}
