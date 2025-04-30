package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BuyCardTests {

    @Test
    public void testBuyOneCardWithEnoughCoins() {
        // Setup
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Card card = new TreasureCard("copper", 0, Card.CardType.TREASURE, 1);
        // Record
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(EasyMock.isA(BoardDto.class))).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(BoardDto.class))).andReturn(0).times(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(BoardDto.class))).andReturn(1).times(1);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(1).times(3);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(0).times(1);
        EasyMock.expect(mockGui.getBuySelection(EasyMock.anyObject())).andReturn("smithy");

        EasyMock.expect(mockPlayer.getCoinsInHand()).andReturn(4);
        EasyMock.expect(mockPlayer.getCoins()).andReturn(4).times(4);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<>(Arrays.asList(card, card))).times(3);
        EasyMock.expect(mockPlayer.getActions()).andReturn(1).times(3);
        mockPlayer.removeTreasureCardsOfCost(4);
        mockPlayer.drawHand();
        mockPlayer.addBoughtCard(new Smithy());
        mockPlayer.cleanup();


        // Replay
        EasyMock.replay(mockGui, mockPlayer);
        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);

        // Verify
        board.processTurn();
        assertEquals(9, board.kingdomDecks.get("smithy").size());
        assertEquals(1, board.currentPlayer);

        EasyMock.verify(mockGui, mockPlayer);
    }
}
