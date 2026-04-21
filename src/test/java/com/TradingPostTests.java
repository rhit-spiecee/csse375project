package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class TradingPostTests {

    @Test
    public void testTradingPostTrashTwoGainSilver() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());
        player.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn("copper").times(2);
        
        mockBoard.transferCardFromDeckToPlayer(EasyMock.anyString(), EasyMock.eq(player));
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        TradingPost tp = new TradingPost(mockBoard);
        tp.useCardPowers(player);

        assertEquals(2, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTradingPostCancelForcesTrash() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());
        player.hand.add(new VictoryCard("estate", 2, 0, 1));

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        // One selection returns null, triggering force trash of hand.get(0)
        // Then second selection returns null, triggering force trash of hand.get(0)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(null).times(2);

        // Expect Silver gain because 2 cards were trashed
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq("silver"), EasyMock.eq(player));
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        TradingPost tp = new TradingPost(mockBoard);
        tp.useCardPowers(player);

        // Should have force-trashed 2 cards even with null returns
        assertEquals(2, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTradingPostEmptyHandLoop() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        // Hand is empty. Loop should break early.

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        EasyMock.replay(mockBoard, mockGui);

        TradingPost tp = new TradingPost(mockBoard);
        tp.useCardPowers(player);

        assertEquals(0, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTradingPostEmptyStringReturn() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.add(new Copper());
        player.hand.add(new Copper());
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        
        // Return empty string for choice - should force trash first card
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("").times(2);

        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq("silver"), EasyMock.eq(player));
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);
        TradingPost tp = new TradingPost(mockBoard);
        tp.useCardPowers(player);
        assertEquals(2, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTradingPostThreeCards() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.add(new Copper());
        player.hand.add(new Copper());
        player.hand.add(new Copper());
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        
        // Trash exactly 2 cards, verify message changes (2 more then 1 more)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.eq("Choose 2 more card(s) to trash for Trading Post:"), EasyMock.anyObject())).andReturn("copper").once();
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.eq("Choose 1 more card(s) to trash for Trading Post:"), EasyMock.anyObject())).andReturn("copper").once();

        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq("silver"), EasyMock.eq(player));
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);
        TradingPost tp = new TradingPost(mockBoard);
        tp.useCardPowers(player);
        assertEquals(2, mockBoard.trashPile.size());
        assertEquals(1, player.hand.size());
        EasyMock.verify(mockBoard, mockGui);
    }
}
