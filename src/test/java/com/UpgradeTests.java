package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class UpgradeTests {

    @Test
    public void testUpgradeTrashGainExactCost() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());
        player.action = 1;
        
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.LinkedHashMap<>();
        mockBoard.treasureDecks = new java.util.LinkedHashMap<>();
        mockBoard.victoryDecks = new java.util.LinkedHashMap<>();
        
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(player);
        mockBoard.currentPlayerIndex = 0; // Needed for bridgeMod

        // Expect getCardFromAvailableSelection twice
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        
        // Copper costs 0. Exact cost 1. Let's pretend there's a card named "costOneCard"
        BoardDeck costOneDeck = new BoardDeck(new KingdomCard("costOneCard", "image", 1, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
        }, 10);
        mockBoard.kingdomDecks.put("costOneCard", costOneDeck);
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("costOneCard");
        EasyMock.expect(mockBoard.getBoardDeckByName("costOneCard")).andReturn(costOneDeck);

        EasyMock.replay(mockBoard, mockGui);

        Upgrade upgrade = new Upgrade(mockBoard);
        upgrade.useCardPowers(player);

        assertEquals(2, player.action); // +1 Action
        assertEquals(1, player.hand.size()); // Drew 1 card, trashed Copper (1+1-1=1)
        assertEquals(1, mockBoard.trashPile.size()); // Copper trashed
        assertEquals(1, player.discardPile.size()); // Gained costOneCard to discard
        assertEquals("costOneCard", player.discardPile.get(0).name);
        
        EasyMock.verify(mockBoard, mockGui);
    }
}
