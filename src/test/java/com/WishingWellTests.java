package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class WishingWellTests {

    @Test
    public void testWishingWellSuccess() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        p1.deck.getCards().clear();
        p1.deck.addTop(new Village());
        p1.deck.addTop(new Smithy());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getWishingWellGuess(EasyMock.anyObject())).andReturn(bundle.getString("village"));

        EasyMock.replay(mockBoard, mockGui);

        WishingWell well = new WishingWell(mockBoard);
        int initialSize = p1.hand.size();
        well.useCardPowers(p1);

        assertEquals(initialSize + 2, p1.hand.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testWishingWellFailure() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Player p1 = new Player(bundle);
        p1.deck.getCards().clear();
        p1.deck.addTop(new Copper());
        p1.deck.addTop(new Smithy());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getWishingWellGuess(EasyMock.anyObject())).andReturn("village");

        EasyMock.replay(mockBoard, mockGui);
        WishingWell well = new WishingWell(mockBoard);
        well.useCardPowers(p1);

        assertEquals(1, p1.hand.size());
        assertEquals(11, p1.deck.size());
        EasyMock.verify(mockBoard, mockGui);
    }
}