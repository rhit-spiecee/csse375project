package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class BaronTests {

    @Test
    public void testBaronDiscardChoice() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        p1.hand.add(new VictoryCard("estate", 2, 0, 1));

        mockBoard.gui = mockGui;
        mockBoard.bundle = bundle;
        EasyMock.expect(mockGui.getBaronChoice()).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Baron baron = new Baron(mockBoard);
        baron.useCardPowers(p1);

        assertEquals(4, p1.coins);
        EasyMock.verify(mockBoard, mockGui);
    }
}