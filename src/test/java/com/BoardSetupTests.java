package com;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardSetupTests {

    private static final List<String> FIXED_TEST_CARDS = Arrays.asList(
            "cellar", "market", "militia", "mine", "moat",
            "remodel", "smithy", "village", "workshop", "woodcutter");

    private Board createFixedBoard(int numPlayers) {
        return new Board(numPlayers, FIXED_TEST_CARDS, 
                ResourceBundle.getBundle(Language.ENGLISH.bundleName));
    }

    @Test
    public void testTwoPlayers() {
        Board board = createFixedBoard(2);
        assertEquals(2, board.numPlayers);
    }

    @Test
    public void testFourPlayers() {
        Board board = createFixedBoard(4);
        assertEquals(4, board.numPlayers);
    }

    @Test
    public void testDeckSizeTwoPlayersOnSetup() {
        Board board = createFixedBoard(2);

        // Kingdom card decks
        assertEquals(10, board.getBoardDeckByName("cellar").size());
        assertEquals(10, board.getBoardDeckByName("market").size());
        assertEquals(10, board.getBoardDeckByName("militia").size());
        assertEquals(10, board.getBoardDeckByName("mine").size());
        assertEquals(10, board.getBoardDeckByName("moat").size());
        assertEquals(10, board.getBoardDeckByName("remodel").size());
        assertEquals(10, board.getBoardDeckByName("smithy").size());
        assertEquals(10, board.getBoardDeckByName("village").size());
        assertEquals(10, board.getBoardDeckByName("woodcutter").size());
        assertEquals(10, board.getBoardDeckByName("workshop").size());

        // Treasure decks
        assertEquals(46, board.getBoardDeckByName("copper").size());
        assertEquals(40, board.getBoardDeckByName("silver").size());
        assertEquals(30, board.getBoardDeckByName("gold").size());

        // Victory decks
        assertEquals(8, board.getBoardDeckByName("estate").size());
        assertEquals(8, board.getBoardDeckByName("duchy").size());
        assertEquals(8, board.getBoardDeckByName("province").size());
        assertEquals(10, board.getBoardDeckByName("cursed").size());
    }

    @Test
    public void testDeckSizeThreePlayersOnSetup() {
        Board board = createFixedBoard(3);

        // Kingdom card decks
        assertEquals(10, board.getBoardDeckByName("cellar").size());
        assertEquals(10, board.getBoardDeckByName("market").size());
        assertEquals(10, board.getBoardDeckByName("militia").size());
        assertEquals(10, board.getBoardDeckByName("mine").size());
        assertEquals(10, board.getBoardDeckByName("moat").size());
        assertEquals(10, board.getBoardDeckByName("remodel").size());
        assertEquals(10, board.getBoardDeckByName("smithy").size());
        assertEquals(10, board.getBoardDeckByName("village").size());
        assertEquals(10, board.getBoardDeckByName("woodcutter").size());
        assertEquals(10, board.getBoardDeckByName("workshop").size());

        // Treasure decks
        assertEquals(39, board.getBoardDeckByName("copper").size());
        assertEquals(40, board.getBoardDeckByName("silver").size());
        assertEquals(30, board.getBoardDeckByName("gold").size());

        // Victory decks
        assertEquals(12, board.getBoardDeckByName("estate").size());
        assertEquals(12, board.getBoardDeckByName("duchy").size());
        assertEquals(12, board.getBoardDeckByName("province").size());
        assertEquals(20, board.getBoardDeckByName("cursed").size());
    }

    @Test
    public void testDeckSizeFourPlayersOnSetup() {
        Board board = createFixedBoard(4);

        // Kingdom card decks
        assertEquals(10, board.getBoardDeckByName("cellar").size());
        assertEquals(10, board.getBoardDeckByName("market").size());
        assertEquals(10, board.getBoardDeckByName("militia").size());
        assertEquals(10, board.getBoardDeckByName("mine").size());
        assertEquals(10, board.getBoardDeckByName("moat").size());
        assertEquals(10, board.getBoardDeckByName("remodel").size());
        assertEquals(10, board.getBoardDeckByName("smithy").size());
        assertEquals(10, board.getBoardDeckByName("village").size());
        assertEquals(10, board.getBoardDeckByName("woodcutter").size());
        assertEquals(10, board.getBoardDeckByName("workshop").size());

        // Treasure decks
        assertEquals(32, board.getBoardDeckByName("copper").size());
        assertEquals(40, board.getBoardDeckByName("silver").size());
        assertEquals(30, board.getBoardDeckByName("gold").size());

        // Victory decks
        assertEquals(12, board.getBoardDeckByName("estate").size());
        assertEquals(12, board.getBoardDeckByName("duchy").size());
        assertEquals(12, board.getBoardDeckByName("province").size());
        assertEquals(30, board.getBoardDeckByName("cursed").size());
    }

    @Test
    public void testInitialPlayerNumber() {
        Board board = createFixedBoard(2);
        assertEquals(0, board.getCurrentPlayerIndex());
    }
    
    @Test
    public void testInitialPlayerList() {
        Board board = createFixedBoard(2);
        assertEquals(2, board.players.size());
    }
    
    @Test
    public void testPlayersHaveInitialHand() {
        Board board = createFixedBoard(2);
        for (int i = 0; i < 2; i++) {
            assertEquals(5, board.players.get(i).hand.size());
        }
    }
}
