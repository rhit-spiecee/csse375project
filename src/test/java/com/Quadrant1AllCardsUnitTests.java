package com;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class Quadrant1AllCardsUnitTests {

    private String cardName;

    public Quadrant1AllCardsUnitTests(String cardName) {
        this.cardName = cardName;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getCardNames() {
        Object[][] data = new Object[Board.ALL_KINGDOM_CARD_IDS.size()][1];
        for (int i = 0; i < Board.ALL_KINGDOM_CARD_IDS.size(); i++) {
            data[i][0] = Board.ALL_KINGDOM_CARD_IDS.get(i);
        }
        return Arrays.asList(data);
    }

    private Gui createBaseMockGui() {
        Gui mockGui = EasyMock.niceMock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2).anyTimes();
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Language.ENGLISH.bundleName)).anyTimes();
        
        // Prevent infinite loops in promptUntilNonEmpty or forceDiscard by returning a valid card if possible
        EasyMock.expect(mockGui.getCardToTrash(EasyMock.anyObject(), EasyMock.anyInt())).andAnswer(() -> {
            java.util.ArrayList<Card> cards = (java.util.ArrayList<Card>) EasyMock.getCurrentArguments()[0];
            return cards.isEmpty() ? "" : cards.get(0).name;
        }).anyTimes();
        
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.anyInt())).andAnswer(() -> {
            java.util.ArrayList<Card> cards = (java.util.ArrayList<Card>) EasyMock.getCurrentArguments()[0];
            return cards.isEmpty() ? "" : cards.get(0).name;
        }).anyTimes();

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andAnswer(() -> {
            java.util.List<String> cards = (java.util.List<String>) EasyMock.getCurrentArguments()[1];
            return cards.isEmpty() ? "" : cards.get(0);
        }).anyTimes();

        EasyMock.expect(mockGui.getCardToPass(EasyMock.anyObject(), EasyMock.anyInt())).andAnswer(() -> {
            java.util.ArrayList<Card> cards = (java.util.ArrayList<Card>) EasyMock.getCurrentArguments()[0];
            return cards.isEmpty() ? "" : cards.get(0).name;
        }).anyTimes();

        EasyMock.expect(mockGui.getBuySelection(EasyMock.anyObject())).andReturn("").anyTimes();
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.anyObject())).andReturn("").anyTimes();
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(1).anyTimes();
        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(EasyMock.anyInt())).andReturn(true).anyTimes();
        EasyMock.expect(mockGui.getIfPlayerWantsToTrash(EasyMock.anyInt())).andReturn(false).anyTimes();
        EasyMock.expect(mockGui.getPawnOptions()).andReturn(new int[]{0, 1}).anyTimes();
        EasyMock.expect(mockGui.getCourtierOptions(EasyMock.anyInt())).andReturn(java.util.Arrays.asList(0, 1)).anyTimes();

        return mockGui;
    }

    @Test(timeout = 5000)
    public void testCardInitializationAndProperties() {
        Gui mockGui = createBaseMockGui();
        EasyMock.replay(mockGui);

        Board board = new Board(2, Arrays.asList(cardName), ResourceBundle.getBundle(Language.ENGLISH.bundleName));
        board.gui = mockGui;

        Card card = board.createKingdomCard(cardName);
        assertNotNull("Card should be instantiable: " + cardName, card);
        assertTrue("Cost should be non-negative", card.cost >= 0);
        assertEquals("Name must match id", cardName, card.name.toLowerCase().replace(" ", ""));
        
        EasyMock.verify(mockGui);
    }

    @Test(timeout = 5000)
    public void testCardActionExecutionBaseState() {
        Gui mockGui = createBaseMockGui();
        EasyMock.replay(mockGui);

        Board board = new Board(2, Arrays.asList(cardName), ResourceBundle.getBundle(Language.ENGLISH.bundleName));
        board.gui = mockGui;

        Card card = board.createKingdomCard(cardName);
        Player p = new Player();
        p.drawHand();

        if (card instanceof KingdomCard) {
            try {
                // Ensure executing an action card doesn't throw a fatal unhandled error in a blank state
                ((KingdomCard) card).useActionCard(p); 
            } catch (Exception e) {
                fail("Action card execution should handle blank state without crashing: " + cardName + ". Exception: " + e.getMessage());
            }
        }
        
        EasyMock.verify(mockGui);
    }
}
