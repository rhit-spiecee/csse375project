package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class PawnTests {

    private static final List<String> FIXED_TEST_CARDS = Arrays.asList(
            "cellar", "market", "militia", "mine", "moat",
            "remodel", "smithy", "village", "workshop", "pawn");

    private Board createFixedBoard(Gui gui) {
        Board board = new Board(2, FIXED_TEST_CARDS, gui.getBundle());
        board.gui = gui;
        return board;
    }

    @Test
    public void testPawnActionAndCoin() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockGui.getPawnOptions()).andReturn(new int[]{1, 3});

        EasyMock.replay(mockGui);

        Board board = createFixedBoard(mockGui);
        Pawn pawn = new Pawn(board);
        Player player = new Player(bundle);
        player.action = 1;
        player.coins = 0;
        player.hand.add(pawn);

        pawn.useActionCard(player);

        assertEquals(1, player.getActions());
        assertEquals(1, player.getCoins());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testPawnCardAndBuy() {
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockGui.getPawnOptions()).andReturn(new int[]{0, 2});

        EasyMock.replay(mockGui);

        Board board = createFixedBoard(mockGui);
        Pawn pawn = new Pawn(board);
        Player player = new Player(bundle);
        player.buy = 1;
        int initialHandSize = player.hand.size();
        player.hand.add(pawn);

        pawn.useActionCard(player);

        assertEquals(initialHandSize + 1, player.hand.size());
        assertEquals(2, player.getBuys());

        EasyMock.verify(mockGui);
    }
}
