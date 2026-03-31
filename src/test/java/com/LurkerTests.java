package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LurkerTests {

    private static final List<String> FIXED_TEST_CARDS = Arrays.asList(
            "cellar", "market", "militia", "mine", "moat",
            "remodel", "smithy", "village", "workshop", "lurker");

    private Board createFixedBoard(Gui gui) {
        Board board = new Board(2, FIXED_TEST_CARDS, gui.getBundle());
        board.gui = gui;
        return board;
    }

    @Test
    public void testLurkerTrashFromSupply() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockGui.getLurkerOption()).andReturn(0);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(
                EasyMock.eq(bundle.getString("trash.supply.message")),
                EasyMock.anyObject(ArrayList.class))
        ).andReturn(bundle.getString("market"));

        EasyMock.replay(mockGui);

        Board board = createFixedBoard(mockGui);
        Lurker lurker = new Lurker(board);
        Player player = new Player(bundle);
        player.action = 1;
        player.hand.add(lurker);

        lurker.useActionCard(player);

        assertEquals(1, player.getActions());
        assertEquals(1, board.trashPile.size());
        assertEquals(bundle.getString("market"), board.trashPile.get(0).name);

        EasyMock.verify(mockGui);
    }

    @Test
    public void testLurkerGainFromTrash() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockGui.getLurkerOption()).andReturn(1);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(
                EasyMock.eq(bundle.getString("gain.trash.message")),
                EasyMock.anyObject(ArrayList.class))
        ).andAnswer(() -> {
            ArrayList<String> cardsInTrash = (ArrayList<String>) EasyMock.getCurrentArguments()[1];
            assertTrue(cardsInTrash.contains(bundle.getString("village")));
            return bundle.getString("village");
        });

        EasyMock.replay(mockGui);

        Board board = createFixedBoard(mockGui);
        board.trashPile.add(new Village());

        Lurker lurker = new Lurker(board);
        Player player = new Player(bundle);
        player.hand.add(lurker);

        lurker.useActionCard(player);

        assertEquals(2, player.discardPile.size());
        assertEquals(0, board.trashPile.size());
        assertEquals(bundle.getString("village"), player.discardPile.get(0).name);

        EasyMock.verify(mockGui);
    }

    @Test
    public void testLurkerGainFromEmptyTrash() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockGui.getLurkerOption()).andReturn(1);
        mockGui.showErrorPopup(bundle.getString("error.no.kingdom.in.trash"));
        EasyMock.expectLastCall();

        EasyMock.replay(mockGui);

        Board board = createFixedBoard(mockGui);
        Lurker lurker = new Lurker(board);
        Player player = new Player(bundle);
        player.hand.add(lurker);

        lurker.useActionCard(player);

        assertTrue(board.trashPile.isEmpty());
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testLurkerCancelSelectionReturnsEmpty() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockGui.getLurkerOption()).andReturn(0);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(
                EasyMock.anyString(),
                EasyMock.anyObject(ArrayList.class))
        ).andReturn("");

        EasyMock.replay(mockGui);

        Board board = createFixedBoard(mockGui);
        Lurker lurker = new Lurker(board);
        Player player = new Player(bundle);
        player.hand.add(lurker);

        lurker.useActionCard(player);

        assertTrue(board.trashPile.isEmpty());

        EasyMock.verify(mockGui);
    }
}