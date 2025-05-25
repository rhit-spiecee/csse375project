package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

public class BuyCardTests {

    @Test
    public void testBuyOneCardWithEnoughCoins() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Card card = new TreasureCard("copper", 0, 1);

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
        mockPlayer.addBoughtCard(new Smithy("smithy"));
        mockPlayer.cleanup();
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui, mockPlayer);
        Board board = Board.setupBoardFromGUI(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);

        board.processTurn();
        assertEquals(9, board.kingdomDecks.get("smithy").size());
        assertEquals(1, board.currentPlayerIndex);

        EasyMock.verify(mockGui, mockPlayer);
    }
}
