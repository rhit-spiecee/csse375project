package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class BuyCardTests {

    private static final List<String> FIXED_TEST_CARDS = Arrays.asList(
            "cellar", "market", "militia", "mine", "moat",
            "remodel", "smithy", "village", "workshop", "woodcutter");

    private Board createFixedBoard(Gui gui) {
        Board board = new Board(gui.getNumPlayers(), FIXED_TEST_CARDS, gui.getBundle());
        board.gui = gui;
        return board;
    }

    @Test
    public void testBuyOneCardWithEnoughCoins() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Card card = new Copper();

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(0).times(1);
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(1).times(1);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(1).times(1);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(0).times(1);
        EasyMock.expect(mockGui.getBuySelection(EasyMock.anyObject())).andReturn("smithy");

        EasyMock.expect(mockPlayer.getCoinsInHand()).andReturn(4);
        EasyMock.expect(mockPlayer.getCoins()).andReturn(4).times(2);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<>(Arrays.asList(card, card))).times(1);
        EasyMock.expect(mockPlayer.getActions()).andReturn(1).times(1);
        mockPlayer.removeTreasureCardsOfCost(4);
        mockPlayer.drawHand();
        Card smithy = new Smithy();
        mockPlayer.addBoughtCard(smithy);
        mockPlayer.cleanup();
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Language.ENGLISH.bundleName));

        EasyMock.replay(mockGui, mockPlayer);
        Board board = createFixedBoard(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);

        board.processTurn();
        assertEquals(9, board.kingdomDecks.get("smithy").size());
        assertEquals(1, board.currentPlayerIndex);

        EasyMock.verify(mockGui, mockPlayer);
    }
}
