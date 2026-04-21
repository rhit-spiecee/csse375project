package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TorturerTests {

    @Test
    public void testTorturerAttackCurse() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();

        Player targetPlayer = new Player(bundle);
        targetPlayer.hand.clear();
        targetPlayer.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(targetPlayer);

        // False = gain a Curse
        EasyMock.expect(mockGui.getTorturerChoice(1)).andReturn(false);
        mockBoard.transferCardFromDeckToPlayer("cursed", targetPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        Torturer torturer = new Torturer(mockBoard);
        torturer.useCardPowers(curPlayer);

        assertEquals(3, curPlayer.hand.size()); // +3 Cards
        
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTorturerMoatBlock() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        Player targetPlayer = new Player(bundle);
        targetPlayer.hand.add(new Moat());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(targetPlayer);

        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Torturer torturer = new Torturer(mockBoard);
        torturer.useCardPowers(curPlayer);

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTorturerDiscardTwoNormal() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        Player targetPlayer = new Player(bundle);
        targetPlayer.hand.add(new Copper());
        targetPlayer.hand.add(new Silver());
        targetPlayer.hand.add(new Gold());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(targetPlayer);

        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(false).anyTimes();
        EasyMock.expect(mockGui.getTorturerChoice(1)).andReturn(true);
        // User selects copper first, then silver
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(1))).andReturn("copper");
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(1))).andReturn("silver");

        EasyMock.replay(mockBoard, mockGui);

        Torturer torturer = new Torturer(mockBoard);
        torturer.useCardPowers(curPlayer);

        assertEquals(1, targetPlayer.hand.size());
        assertEquals("gold", targetPlayer.hand.get(0).name);

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTorturerDiscardTwoEmptyMidway() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        Player targetPlayer = new Player(bundle);
        targetPlayer.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(targetPlayer);

        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(false).anyTimes();
        EasyMock.expect(mockGui.getTorturerChoice(1)).andReturn(true);
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(1))).andReturn("copper");

        EasyMock.replay(mockBoard, mockGui);

        Torturer torturer = new Torturer(mockBoard);
        torturer.useCardPowers(curPlayer);

        assertEquals(0, targetPlayer.hand.size());

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTorturerDiscardTwoFallback() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        Player targetPlayer = new Player(bundle);
        targetPlayer.hand.add(new Copper());
        targetPlayer.hand.add(new Silver());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(targetPlayer);

        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(false).anyTimes();
        EasyMock.expect(mockGui.getTorturerChoice(1)).andReturn(true);
        // Fallback for empty string
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(1))).andReturn("");
        // Fallback for null
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.eq(1))).andReturn(null);

        EasyMock.replay(mockBoard, mockGui);

        Torturer torturer = new Torturer(mockBoard);
        torturer.useCardPowers(curPlayer);

        assertEquals(0, targetPlayer.hand.size());

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testTorturerGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Torturer torturer = new Torturer(mockBoard);
        java.util.List<String> types = torturer.getTypes();
        assertTrue(types.contains("Attack"));
        assertTrue(types.contains("Action"));
    }
}
