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
        // Empty deck first
        while (p1.deck.size() > 0) p1.deck.draw();
        p1.discardPile.clear();

        p1.deck.addTop(new Village());
        p1.deck.addTop(new Smithy());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getWishingWellGuess(EasyMock.anyObject())).andReturn(bundle.getString("village"));

        EasyMock.replay(mockBoard, mockGui);

        WishingWell well = new WishingWell(mockBoard);
        int initialSize = p1.hand.size();
        well.useCardPowers(p1);

        // Smithy drawn by drawOneCard, Village drawn by guess
        assertEquals(initialSize + 2, p1.hand.size());
        assertEquals(2, p1.action); // 1 initial + 1 from well
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testWishingWellFailure() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Player p1 = new Player(bundle);
        
        while (p1.deck.size() > 0) p1.deck.draw();
        p1.discardPile.clear();

        p1.deck.addTop(new Copper());
        p1.deck.addTop(new Smithy());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getWishingWellGuess(EasyMock.anyObject())).andReturn("village");

        EasyMock.replay(mockBoard, mockGui);
        WishingWell well = new WishingWell(mockBoard);
        well.useCardPowers(p1);

        // Smithy drawn, Copper guessed wrong (stays on top)
        assertEquals(1, p1.hand.size());
        assertEquals(1, p1.deck.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testWishingWellEmptyDeck() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        // Empty deck and discard pile properly
        while (p1.deck.size() > 0) p1.deck.draw();
        p1.discardPile.clear();
        p1.hand.clear();

        // Add 1 card to deck so drawOneCard succeeds but deck becomes empty for the guess
        p1.deck.addTop(new Copper());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getWishingWellGuess(EasyMock.anyObject()))
                .andReturn(bundle.getString("village"));

        EasyMock.replay(mockBoard, mockGui);

        WishingWell well = new WishingWell(mockBoard);
        well.useCardPowers(p1);

        // 1 copper drawn into hand
        assertEquals(1, p1.hand.size());
        assertEquals(0, p1.deck.size());
        assertEquals(2, p1.action); 

        EasyMock.verify(mockBoard, mockGui);
    }
}