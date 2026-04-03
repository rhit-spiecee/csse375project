package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class PatrolTests {

    @Test
    public void testPatrolDrawsVictoryAndCurse() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.deck.getCards().clear(); // Clear initial deck
        
        // Put cards on deck (from bottom to top)
        player.deck.addTop(new Village()); // Will be put back
        player.deck.addTop(new VictoryCard("estate", 2, 0, 1)); // Goes to hand
        player.deck.addTop(new VictoryCard("cursed", 0, 0, -1)); // Goes to hand
        player.deck.addTop(new Copper()); // Will be put back

        // Ensure there are also cards for the +3 cards portion
        player.deck.addTop(new Copper());
        player.deck.addTop(new Copper());
        player.deck.addTop(new Copper());

        mockBoard.gui = mockGui;

        // Copper and Village need to be put back. Gui asks order. User picks Copper first, then Village.
        // Wait, Copper and Village are revealed.
        // First choice: Copper
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");

        EasyMock.replay(mockBoard, mockGui);

        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);

        // +3 Cards = 3 Coppers in hand
        // Revealed: Copper, Cursed, Estate, Village
        // Cursed and Estate go to hand.
        // Copper and Village go back.
        // Hand size: 3 (from draw) + 2 (from reveal) = 5
        assertEquals(5, player.hand.size());
        
        // Deck should have 12 cards: 10 original, plus Copper and Village that were put back.
        assertEquals(12, player.deck.size());
        assertEquals("village", player.deck.draw().name); // Top card
        assertEquals("copper", player.deck.draw().name); // Bottom card

        EasyMock.verify(mockBoard, mockGui);
    }
}
