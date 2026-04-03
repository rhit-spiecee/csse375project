package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class StewardTests {

    @Test
    public void testStewardDrawCards() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Gui mockGui = EasyMock.mock(Gui.class);

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getStewardOption()).andReturn(0);
        mockPlayer.drawOneCard();
        EasyMock.expectLastCall().times(2);

        EasyMock.replay(mockBoard, mockPlayer, mockGui);

        Steward steward = new Steward(mockBoard);
        steward.useCardPowers(mockPlayer);

        EasyMock.verify(mockBoard, mockPlayer, mockGui);
    }

    @Test
    public void testStewardGainCoins() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.coins = 0;

        mockBoard.gui = mockGui;
        EasyMock.expect(mockGui.getStewardOption()).andReturn(1);

        EasyMock.replay(mockBoard, mockGui);

        Steward steward = new Steward(mockBoard);
        steward.useCardPowers(player);

        assertEquals(2, player.coins);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testStewardTrashCards() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());
        player.hand.add(new Copper());
        player.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.players = new ArrayList<>(java.util.Arrays.asList(player));

        EasyMock.expect(mockGui.getStewardOption()).andReturn(2);
        EasyMock.expect(mockGui.getCardToTrash(EasyMock.anyObject(), EasyMock.eq(0))).andReturn(bundle.getString("copper")).times(2);

        EasyMock.replay(mockBoard, mockGui);

        Steward steward = new Steward(mockBoard);
        steward.useCardPowers(player);

        assertEquals(1, player.hand.size());
        assertEquals(2, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }
}