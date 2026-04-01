package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class BaronTests {

    @Test
    public void testBaronDiscardChoice() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        mockBoard.bundle = bundle;
        mockBoard.gui = mockGui;

        Player p1 = new Player(bundle);
        p1.hand.add(new VictoryCard("estate", 2, 0, 1));

        EasyMock.expect(mockGui.getBaronChoice()).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Baron baron = new Baron(mockBoard);
        int initialBuys = p1.getBuys();
        baron.useCardPowers(p1);
        assertEquals(initialBuys + 1, p1.getBuys());
        assertEquals(4, p1.getCoins());

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testBaronGainEstateInsteadOfCoins() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        mockBoard.bundle = bundle;
        mockBoard.gui = mockGui;

        Player p1 = new Player(bundle);
        p1.hand.add(new VictoryCard("estate", 2, 0, 1));

        EasyMock.expect(mockGui.getBaronChoice()).andReturn(false);

        mockBoard.transferCardFromDeckToPlayer(bundle.getString("estate"), p1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        Baron baron = new Baron(mockBoard);
        int initialBuys = p1.getBuys();
        baron.useCardPowers(p1);

        assertEquals(initialBuys + 1, p1.getBuys());
        assertEquals(0, p1.getCoins());

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testBaronNoEstateInHand() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        mockBoard.bundle = bundle;
        mockBoard.gui = mockGui;

        Player p1 = new Player(bundle);
        p1.hand.add(new VictoryCard("province", 8, 0, 6));

        mockBoard.transferCardFromDeckToPlayer(bundle.getString("estate"), p1);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        Baron baron = new Baron(mockBoard);
        int initialBuys = p1.getBuys();
        baron.useCardPowers(p1);

        assertEquals(initialBuys + 1, p1.getBuys());

        EasyMock.verify(mockBoard, mockGui);
    }
}