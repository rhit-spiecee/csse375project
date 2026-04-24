package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class SecretPassageTests {

    @Test
    public void testSecretPassageAction() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper()); // Card to put back
        player.action = 1;
        int initialDeckSize = player.deck.size();

        mockBoard.gui = mockGui;
        
        // Mock hand names for getCardFromAvailableSelection
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        // Mock position in deck (e.g., index 0 = bottom)
        EasyMock.expect(mockGui.getSecretPassagePosition(initialDeckSize - 2)).andReturn(0);

        EasyMock.replay(mockBoard, mockGui);

        SecretPassage sp = new SecretPassage(mockBoard);
        sp.useCardPowers(player);

        assertEquals(2, player.action);
        // Hand: initially 1, draw 2, remove 1 = 2
        assertEquals(2, player.hand.size());
        // Deck: initially initialDeckSize, draw 2, add 1 = initialDeckSize - 1
        assertEquals(initialDeckSize - 1, player.deck.size());
        
        // Verify card at index 0 is Copper
        assertEquals("copper", player.deck.getCards().get(0).name);

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSecretPassagePutTop() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        Copper copper = new Copper();
        player.hand.add(copper);
        
        mockBoard.gui = mockGui;
        int deckSizeAfterDraw = player.deck.size() - 2;

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        // Put at the very top (index = current deck size)
        EasyMock.expect(mockGui.getSecretPassagePosition(deckSizeAfterDraw)).andReturn(deckSizeAfterDraw);

        EasyMock.replay(mockBoard, mockGui);

        SecretPassage sp = new SecretPassage(mockBoard);
        sp.useCardPowers(player);

        // Top of deck should be copper
        assertEquals("copper", player.deck.draw().name);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testSecretPassageNotInHand() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.clear();
        player.hand.add(new Copper());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("nonexistent");

        EasyMock.replay(mockBoard, mockGui);

        SecretPassage sp = new SecretPassage(mockBoard);
        sp.useCardPowers(player);

        EasyMock.verify(mockBoard, mockGui);
    }
}
