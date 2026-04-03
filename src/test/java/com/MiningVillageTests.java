package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MiningVillageTests {

    @Test
    public void testMiningVillageActionNoTrash() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.action = 1;
        int initialHandSize = player.hand.size();

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getMiningVillageTrashChoice()).andReturn(false);

        EasyMock.replay(mockBoard, mockGui);

        MiningVillage mv = new MiningVillage(mockBoard);
        mv.useCardPowers(player);

        assertEquals(3, player.action); // 1 initial + 2
        assertEquals(initialHandSize + 1, player.hand.size());
        assertEquals(0, player.coins);
        
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMiningVillageActionWithTrash() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        // Add Mining Village to hand so it can trash itself
        MiningVillage mvCard = new MiningVillage(mockBoard);
        player.hand.add(mvCard);
        player.coins = 0;
        player.action = 1;

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        EasyMock.expect(mockGui.getMiningVillageTrashChoice()).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        mvCard.useCardPowers(player);

        assertEquals(2, player.coins);
        assertEquals(1, player.hand.size()); // Initial 1 mv + 1 (draw) - 1 (trash) = 1
        assertEquals(3, player.action);
        assertEquals(1, mockBoard.trashPile.size());
        assertEquals(mvCard, mockBoard.trashPile.get(0));

        // Verify it was removed from hand
        assertFalse(player.hand.contains(mvCard));

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMiningVillageCorrectDiscardBehavior() {
        // This test verifies that if the card trashes itself, KingdomCard won't add it to discardPile
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        MiningVillage mvCard = new MiningVillage(mockBoard);
        player.hand.add(mvCard);
        
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        // If trash choice is true
        EasyMock.expect(mockGui.getMiningVillageTrashChoice()).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        // This is what Board.playActionCard does:
        mvCard.useActionCard(player);

        // Verification
        assertTrue(mockBoard.trashPile.contains(mvCard));
        assertFalse(player.discardPile.contains(mvCard));
        assertFalse(player.hand.contains(mvCard));

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMiningVillageCorrectDiscardBehaviorNoTrash() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        MiningVillage mvCard = new MiningVillage(mockBoard);
        player.hand.add(mvCard);
        
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        // If trash choice is false
        EasyMock.expect(mockGui.getMiningVillageTrashChoice()).andReturn(false);

        EasyMock.replay(mockBoard, mockGui);

        mvCard.useActionCard(player);

        // Verification
        assertFalse(mockBoard.trashPile.contains(mvCard));
        assertTrue(player.discardPile.contains(mvCard));
        assertFalse(player.hand.contains(mvCard));

        EasyMock.verify(mockBoard, mockGui);
    }
}
