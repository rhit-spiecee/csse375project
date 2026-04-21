package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MinionTests {

    @Test
    public void testMinionChoiceCoin() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.action = 1;
        player.coins = 0;

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(player);

        // True = +$2
        EasyMock.expect(mockGui.getMinionChoice()).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Minion minion = new Minion(mockBoard);
        minion.useCardPowers(player);

        assertEquals(2, player.action); // +1 Action
        assertEquals(2, player.coins); // +$2

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMinionChoiceAttack() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.add(new Copper());
        curPlayer.action = 1;

        Player otherPlayerAffected = new Player(bundle);
        for(int i = 0; i < 5; i++) otherPlayerAffected.hand.add(new Copper()); // 5 cards

        Player otherPlayerNotAffected = new Player(bundle);
        for(int i = 0; i < 4; i++) otherPlayerNotAffected.hand.add(new Copper()); // 4 cards

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(otherPlayerAffected);
        mockBoard.players.add(otherPlayerNotAffected);

        // False = Discard Hand, draw 4
        EasyMock.expect(mockGui.getMinionChoice()).andReturn(false);

        EasyMock.replay(mockBoard, mockGui);

        Minion minion = new Minion(mockBoard);
        minion.useCardPowers(curPlayer);

        assertEquals(2, curPlayer.action); // +1 Action
        assertEquals(4, curPlayer.hand.size()); // Drew 4
        assertEquals(1, curPlayer.discardPile.size()); // Discarded 1

        assertEquals(4, otherPlayerAffected.hand.size()); // Drew 4
        assertEquals(5, otherPlayerAffected.discardPile.size()); // Discarded 5

        assertEquals(4, otherPlayerNotAffected.hand.size()); // Unaffected, still 4
        assertEquals(0, otherPlayerNotAffected.discardPile.size()); // Unaffected

        assertTrue(minion.getTypes().contains("Action"));
        assertTrue(minion.getTypes().contains("Attack"));

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testMinionAttackWithMoat() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.action = 1;

        Player victim = new Player(bundle);
        victim.hand.add(new Moat());
        for(int i = 0; i < 5; i++) victim.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(victim);

        EasyMock.expect(mockGui.getMinionChoice()).andReturn(false);
        // Victim blocks
        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(true);

        EasyMock.replay(mockBoard, mockGui);

        Minion minion = new Minion(mockBoard);
        minion.useCardPowers(curPlayer);

        assertEquals(6, victim.hand.size()); // Unaffected
        assertEquals(0, victim.discardPile.size());

        EasyMock.verify(mockBoard, mockGui);
    }
}
