package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class MillTests {

    @Test
    public void testMillActionNoDiscard() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.action = 1;
        int initialHandSize = player.hand.size();

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getMillDiscardChoice()).andReturn(false);

        EasyMock.replay(mockBoard, mockGui);

        Mill mill = new Mill(mockBoard);
        mill.useCardPowers(player);

        assertEquals(2, player.action);
        assertEquals(initialHandSize + 1, player.hand.size());
        assertEquals(0, player.coins);
        
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMillActionDiscardTwo() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());
        player.hand.add(new Copper());
        player.coins = 0;
        player.action = 1;

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>(Arrays.asList(player));

        EasyMock.expect(mockGui.getMillDiscardChoice()).andReturn(true);
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(0))).andReturn("copper").times(2);

        EasyMock.replay(mockBoard, mockGui);

        Mill mill = new Mill(mockBoard);
        mill.useCardPowers(player);

        assertEquals(2, player.coins);
        // Initial 2 + 1 (draw) - 2 (discard) = 1
        assertEquals(1, player.hand.size());
        assertEquals(2, player.action);

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMillActionDiscardOnlyOne() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());
        player.coins = 0;

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>(Arrays.asList(player));

        EasyMock.expect(mockGui.getMillDiscardChoice()).andReturn(true);
        // First discard
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(0))).andReturn("copper");
        // Second discard attempt - hand will have 1 card (the one just drawn)
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(0))).andReturn("");

        EasyMock.replay(mockBoard, mockGui);

        Mill mill = new Mill(mockBoard);
        mill.useCardPowers(player);

        assertEquals(0, player.coins); // Should not get coins if only 1 discarded
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMillVictoryPoints() {
        Mill mill = new Mill(null);
        assertEquals(2, mill.getVictoryPoints(new ArrayList<>()));
    }
}
