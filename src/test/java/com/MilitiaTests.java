package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.ResourceBundle;

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
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.notNull(), EasyMock.eq(1))).andReturn("");
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.notNull(), EasyMock.eq(1))).andReturn("copper").times(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
        Militia militia = new Militia(board);
        Player player = new StubPlayer(militia);

        militia.useActionCard(player);

        assertEquals(2, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(0, player.hand.size());
        assertEquals(1, player.discardPile.size());
        assertEquals(3, board.players.getLast().hand.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testPlayerBlockMilitia() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayerOne = EasyMock.mock(Player.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(true);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        mockPlayerOne.discardCard(EasyMock.isA(KingdomCard.class));

        EasyMock.replay(mockGui, mockPlayerOne);

        Board board = Board.setupBoardFromGui(mockGui);
        Militia militia = new Militia(board);
        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new Moat());
        newHand.add(new Copper());
        newHand.add(new Copper());
        newHand.add(new Copper());

        board.players.getLast().hand = newHand;
        militia.useActionCard(mockPlayerOne);

        assertEquals(4, board.players.getLast().getHand().size());
        assertEquals(newHand, board.players.getLast().getHand());

        EasyMock.verify(mockGui, mockPlayerOne);
    }

    @Test
    public void testPlayerMilitiaDiscardNone() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayerOne = EasyMock.mock(Player.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        mockPlayerOne.discardCard(EasyMock.isA(KingdomCard.class));

        EasyMock.replay(mockGui, mockPlayerOne);

        Board board = Board.setupBoardFromGui(mockGui);
        Militia militia = new Militia(board);
        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new Copper());
        newHand.add(new Copper());
        newHand.add(new Copper());

        board.players.getLast().hand = newHand;
        militia.useActionCard(mockPlayerOne);

        assertEquals(3, board.players.getLast().getHand().size());
        assertEquals(newHand, board.players.getLast().getHand());

        EasyMock.verify(mockGui, mockPlayerOne);

    }
}
