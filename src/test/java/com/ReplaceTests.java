package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class ReplaceTests {

    @Test
    public void testReplaceGainAction() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.LinkedHashMap<>();
        mockBoard.treasureDecks = new java.util.LinkedHashMap<>();
        mockBoard.victoryDecks = new java.util.LinkedHashMap<>();
        
        BoardDeck villageDeck = new BoardDeck(new Village(), 10);
        mockBoard.kingdomDecks.put("village", villageDeck);

        // Expect getCardFromAvailableSelection twice
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        
        // Copper costs 0. Replace lets you get up to 2. Village costs 3, so we can't test Village... wait, Copper costs 0. Village costs 3.
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(2, mockBoard.kingdomDecks)).andReturn(new ArrayList<>(java.util.Collections.singletonList("moat")));
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(2, mockBoard.treasureDecks)).andReturn(new ArrayList<>());
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(2, mockBoard.victoryDecks)).andReturn(new ArrayList<>());
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("moat");
        
        BoardDeck moatDeck = new BoardDeck(new Moat(), 10);
        EasyMock.expect(mockBoard.getBoardDeckByName("moat")).andReturn(moatDeck);

        EasyMock.replay(mockBoard, mockGui);

        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);

        assertEquals(1, mockBoard.trashPile.size()); // Copper trashed
        assertEquals("copper", mockBoard.trashPile.get(0).name);
        
        // Moat (Action-Reaction) should be on top of deck (10 original cards + 1 added = 11)
        assertEquals(11, player.deck.size());
        assertEquals("moat", player.deck.draw().name);
        
        EasyMock.verify(mockBoard, mockGui);
    }
}
