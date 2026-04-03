package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class IronworksTests {

    @Test
    public void testIronworksGainAction() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        BoardDeck mockDeck = EasyMock.mock(BoardDeck.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        p1.action = 0;
        mockBoard.gui = mockGui;
        mockBoard.kingdomDecks = new java.util.HashMap<>();
        mockBoard.numPlayers = 2;

        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(Arrays.asList(bundle.getString("village")));
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(bundle.getString("village"));
        EasyMock.expect(mockBoard.getBoardDeckByName(bundle.getString("village"))).andReturn(mockDeck);
        EasyMock.expect(mockDeck.getCard()).andReturn(new Village());
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq(bundle.getString("village")), EasyMock.eq(p1));

        EasyMock.replay(mockBoard, mockGui, mockDeck);

        Ironworks iron = new Ironworks(mockBoard);
        iron.useCardPowers(p1);

        assertEquals(1, p1.action);
        EasyMock.verify(mockBoard, mockGui, mockDeck);
    }

    @Test
    public void testIronworksGainCoin() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        BoardDeck mockDeck = EasyMock.mock(BoardDeck.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        p1.coins = 0;
        mockBoard.gui = mockGui;
        mockBoard.kingdomDecks = new java.util.HashMap<>();
        mockBoard.numPlayers = 2;

        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(Arrays.asList(bundle.getString("copper")));
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(bundle.getString("copper"));
        EasyMock.expect(mockBoard.getBoardDeckByName(bundle.getString("copper"))).andReturn(mockDeck);
        EasyMock.expect(mockDeck.getCard()).andReturn(new Copper());
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq(bundle.getString("copper")), EasyMock.eq(p1));

        EasyMock.replay(mockBoard, mockGui, mockDeck);

        Ironworks iron = new Ironworks(mockBoard);
        iron.useCardPowers(p1);

        assertEquals(1, p1.getCoins());
        EasyMock.verify(mockBoard, mockGui, mockDeck);
    }

    @Test
    public void testIronworksGainCard() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        BoardDeck mockDeck = EasyMock.mock(BoardDeck.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        p1.coins = 0;
        mockBoard.gui = mockGui;
        mockBoard.kingdomDecks = new java.util.HashMap<>();
        mockBoard.numPlayers = 2;

        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(Arrays.asList(bundle.getString("estate")));
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(bundle.getString("estate"));
        EasyMock.expect(mockBoard.getBoardDeckByName(bundle.getString("estate"))).andReturn(mockDeck);
        EasyMock.expect(mockDeck.getCard()).andReturn(new VictoryCard("estate", 1,1,1));
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq(bundle.getString("estate")), EasyMock.eq(p1));

        EasyMock.replay(mockBoard, mockGui, mockDeck);

        Ironworks iron = new Ironworks(mockBoard);
        iron.useCardPowers(p1);

        assertEquals(1, p1.hand.size());
        EasyMock.verify(mockBoard, mockGui, mockDeck);
    }
}