package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ResourceBundle;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NoblesTests {

    @Test
    public void testNoblesChooseCards() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.action = 1;
        int initialHandSize = player.hand.size();

        mockBoard.gui = mockGui;
        // True = +3 Cards
        EasyMock.expect(mockGui.getNoblesChoice()).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Nobles nobles = new Nobles(mockBoard);
        nobles.useCardPowers(player);

        assertEquals(1, player.action); // Action unchanged
        assertEquals(initialHandSize + 3, player.hand.size()); // +3 cards
        
        // Check VP
        assertEquals(2, nobles.getVictoryPoints(Collections.emptyList()));
        assertTrue(nobles.getTypes().contains("Action"));
        assertTrue(nobles.getTypes().contains("Victory"));

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testNoblesChooseActions() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.action = 1;
        int initialHandSize = player.hand.size();

        mockBoard.gui = mockGui;
        // False = +2 Actions
        EasyMock.expect(mockGui.getNoblesChoice()).andReturn(false);

        EasyMock.replay(mockBoard, mockGui);

        Nobles nobles = new Nobles(mockBoard);
        nobles.useCardPowers(player);

        assertEquals(3, player.action); // +2 Actions
        assertEquals(initialHandSize, player.hand.size()); // Cards unchanged

        EasyMock.verify(mockBoard, mockGui);
    }
}
