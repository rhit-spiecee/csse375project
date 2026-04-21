package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SwindlerTests {

    @Test
    public void testSwindlerFullExecution() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        Player p2 = new Player(bundle);
        while(p2.deck.size() > 0) p2.deck.draw();
        p2.deck.addTop(new Copper());

        mockBoard.numPlayers = 2;
        mockBoard.currentPlayerIndex = 0;
        mockBoard.players = new ArrayList<>(Arrays.asList(p1, p2));
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.HashMap<>();

        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(Arrays.asList(bundle.getString("copper")));
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(bundle.getString("copper"));
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq(bundle.getString("copper")), EasyMock.eq(p2));

        EasyMock.replay(mockBoard, mockGui);

        Swindler swindler = new Swindler(mockBoard);
        swindler.useCardPowers(p1);

        assertEquals(1, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSwindlerVictimEmptyDeck() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        Player p2 = new Player(bundle);
        while(p2.deck.size() > 0) p2.deck.draw();
        p2.discardPile.clear();
        p2.discardPile.add(new Copper()); // For recycled cards

        mockBoard.numPlayers = 2;
        mockBoard.currentPlayerIndex = 0;
        mockBoard.players = new ArrayList<>(Arrays.asList(p1, p2));
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.HashMap<>();

        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(Arrays.asList(bundle.getString("copper")));
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(bundle.getString("copper"));
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq(bundle.getString("copper")), EasyMock.eq(p2));

        EasyMock.replay(mockBoard, mockGui);

        Swindler swindler = new Swindler(mockBoard);
        swindler.useCardPowers(p1);

        assertEquals(1, mockBoard.trashPile.size());
        assertEquals(0, p2.deck.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSwindlerEmptyDeckAndDiscardBoundary() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        Player p2 = new Player(bundle);
        while(p2.deck.size() > 0) p2.deck.draw();
        p2.discardPile.clear();

        mockBoard.numPlayers = 2;
        mockBoard.currentPlayerIndex = 0;
        mockBoard.players = new ArrayList<>(Arrays.asList(p1, p2));
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        EasyMock.replay(mockBoard, mockGui);

        Swindler swindler = new Swindler(mockBoard);
        swindler.useCardPowers(p1);

        assertEquals(0, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSwindlerNoReplacementOptions() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        Player p2 = new Player(bundle);
        while(p2.deck.size() > 0) p2.deck.draw();
        p2.deck.addTop(new Copper());

        mockBoard.numPlayers = 2;
        mockBoard.currentPlayerIndex = 0;
        mockBoard.players = new ArrayList<>(Arrays.asList(p1, p2));
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.HashMap<>();

        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(new ArrayList<>()); 

        EasyMock.replay(mockBoard, mockGui);

        Swindler swindler = new Swindler(mockBoard);
        swindler.useCardPowers(p1);

        assertEquals(1, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSwindlerVictimMoatBlock() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        Player p2 = new Player(bundle);
        p2.hand.add(new Moat());
        while(p2.deck.size() > 0) p2.deck.draw();
        p2.deck.addTop(new Copper());

        mockBoard.numPlayers = 2;
        mockBoard.currentPlayerIndex = 0;
        mockBoard.players = new ArrayList<>(Arrays.asList(p1, p2));
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        
        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Swindler swindler = new Swindler(mockBoard);
        swindler.useCardPowers(p1);

        assertEquals(0, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSwindlerGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Swindler swindler = new Swindler(mockBoard);
        java.util.List<String> types = swindler.getTypes();
        assertTrue(types.contains("Attack"));
        assertTrue(types.contains("Action"));
    }
}
