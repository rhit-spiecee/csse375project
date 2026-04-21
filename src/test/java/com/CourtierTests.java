package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourtierTests {

    @Test
    public void testCourtierRevealOneType() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        curPlayer.hand.add(new Copper());

        mockBoard.gui = mockGui;

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper").once();
        // Choose option 1 (+1 Buy)
        EasyMock.expect(mockGui.getCourtierOptions(1)).andReturn(new ArrayList<>(Arrays.asList(1))).once();

        EasyMock.replay(mockBoard, mockGui);

        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);

        assertEquals(2, curPlayer.buy); 
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierRevealThreeTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        
        Card complexCard = new KingdomCard("complex", "img", 5, 1, 1, "tip") {
            public void useCardPowers(Player p) {}
            public java.util.List<String> getTypes() {
                return new ArrayList<>(Arrays.asList("Action", "Treasure", "Victory"));
            }
        };
        curPlayer.hand.add(complexCard);

        mockBoard.gui = mockGui;

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("complex").once();
        // Choose options 0 (+1 Action), 2 (+$3), 3 (Gain Gold)
        EasyMock.expect(mockGui.getCourtierOptions(3)).andReturn(new ArrayList<>(Arrays.asList(0, 2, 3))).once();

        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq("gold"), EasyMock.eq(curPlayer));
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);

        assertEquals(2, curPlayer.action); 
        assertEquals(3, curPlayer.coins); 
        
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierFourTypesCap() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        
        Card complexCard = new KingdomCard("vcomplex", "img", 5, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
            public java.util.List<String> getTypes() {
                // Exactly 5 types
                return new ArrayList<>(Arrays.asList("Action", "Treasure", "Victory", "Reaction", "Duration"));
            }
        };
        curPlayer.hand.add(complexCard);

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("vcomplex").once();
        // Should request 4 options despite 5 types
        EasyMock.expect(mockGui.getCourtierOptions(4)).andReturn(new ArrayList<>()).once();

        EasyMock.replay(mockBoard, mockGui);
        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierZeroTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        Card noTypeCard = new Card("none", "img", 0, 0, 0, "tip") {
            public java.util.List<String> getTypes() { return new ArrayList<>(); }
        };
        curPlayer.hand.add(noTypeCard);

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("none").once();

        EasyMock.replay(mockBoard, mockGui);
        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierCancelReveal() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        curPlayer.hand.add(new Copper());

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("").once();

        EasyMock.replay(mockBoard, mockGui);
        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);
        EasyMock.verify(mockBoard, mockGui);
    }
    @Test
    public void testCourtierGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Courtier courtier = new Courtier(mockBoard);
        java.util.List<String> types = courtier.getTypes();
        assertTrue(types.contains("Action"));
    }

    @Test
    public void testCourtierFourTypesExact() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player curPlayer = new Player();
        curPlayer.hand.clear();
        Card fourTypeCard = new KingdomCard("4type", "img", 5, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
            public java.util.List<String> getTypes() {
                return new ArrayList<>(java.util.Arrays.asList("Action", "Treasure", "Victory", "Reaction"));
            }
        };
        curPlayer.hand.add(fourTypeCard);
        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("4type").once();
        // Should request 4 options
        EasyMock.expect(mockGui.getCourtierOptions(4)).andReturn(new ArrayList<>()).once();

        EasyMock.replay(mockBoard, mockGui);
        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierEmptyHand() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player curPlayer = new Player();
        curPlayer.hand.clear();

        EasyMock.replay(mockBoard);
        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(curPlayer);
        EasyMock.verify(mockBoard);
    }
}
