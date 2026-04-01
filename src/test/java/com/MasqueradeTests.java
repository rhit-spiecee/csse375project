package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class MasqueradeTests {

    @Test
    public void testMasqueradeCardPowerTriggersBoard() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        mockPlayer.drawOneCard();
        EasyMock.expectLastCall().times(2);

        mockBoard.executeMasquerade(mockPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockPlayer);

        Masquerade masquerade = new Masquerade(mockBoard);
        masquerade.useCardPowers(mockPlayer);

        EasyMock.verify(mockBoard, mockPlayer);
    }

    @Test
    public void testExecuteMasqueradeLogic() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Board board = new Board(2, Arrays.asList("masquerade"), bundle);
        board.gui = mockGui;
        board.trashPile = new ArrayList<>();

        Player p1 = board.players.get(0);
        Player p2 = board.players.get(1);

        p1.hand.clear();
        p2.hand.clear();
        p1.hand.add(new Copper());
        p2.hand.add(new VictoryCard("estate", 2, 0, 1));

        EasyMock.expect(mockGui.getCardToPass(EasyMock.anyObject(), EasyMock.eq(0))).andReturn(bundle.getString("copper"));
        EasyMock.expect(mockGui.getCardToPass(EasyMock.anyObject(), EasyMock.eq(1))).andReturn(bundle.getString("estate"));

        EasyMock.expect(mockGui.getIfPlayerWantsToTrash(0)).andReturn(true);
        EasyMock.expect(mockGui.getCardToTrash(EasyMock.anyObject(), EasyMock.eq(0))).andReturn(bundle.getString("estate"));

        EasyMock.replay(mockGui);

        board.executeMasquerade(p1);

        assertEquals("P2 should now have the copper", bundle.getString("copper"), p2.hand.get(0).name);
        assertEquals("P1 should have trashed the received estate", 1, board.trashPile.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testExecuteMasqueradeEmptyHands() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Board board = new Board(2, Arrays.asList("masquerade"), bundle);
        board.gui = mockGui;

        for (Player p : board.players) p.hand.clear();

        EasyMock.replay(mockGui);

        board.executeMasquerade(board.players.get(0));

        assertEquals(0, board.players.get(0).hand.size());
        assertEquals(0, board.players.get(1).hand.size());

        EasyMock.verify(mockGui);
    }
}