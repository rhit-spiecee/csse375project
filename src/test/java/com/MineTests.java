package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

public class MineTests {
    static class StubPlayer extends Player {
        public StubPlayer(Mine mine, String cardType) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(mine);
            hand.add(new TreasureCard(cardType, 3, 2));
        }

    }

    @Test
    public void testCardBehaviorTrashingSilver() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardToTrash(EasyMock.notNull(), EasyMock.anyInt())).andReturn("silver");
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("gold");
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        Mine mine = new Mine(board, "mine");
        Player player = new StubPlayer(mine, "silver");

        mine.useActionCard(player);
        Card gold = new TreasureCard("gold", 6, 3);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(gold.name, player.hand.getFirst().name);
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testCardBehaviorTrashingCopper() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardToTrash(EasyMock.notNull(), EasyMock.anyInt())).andReturn("copper");
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("silver");
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        Mine mine = new Mine(board, "mine");
        Player player = new StubPlayer(mine, "copper");

        mine.useActionCard(player);
        Card silver = new TreasureCard("silver", 4, 2);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(silver.name, player.hand.getFirst().name);
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testCardBehaviorTrashingGold() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardToTrash(EasyMock.notNull(), EasyMock.anyInt())).andReturn("gold");
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("silver");
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        Mine mine = new Mine(board, "mine");
        Player player = new StubPlayer(mine, "gold");

        mine.useActionCard(player);
        Card silver = new TreasureCard("silver", 4, 2);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(silver.name, player.hand.getFirst().name);
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }


}
