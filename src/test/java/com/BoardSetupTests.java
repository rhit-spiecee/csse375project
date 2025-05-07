package com;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardSetupTests {

    @Test
    public void testOnePlayer() {
        assertThrows(RuntimeException.class, () -> new Board(1));
    }

    @Test
    public void testTwoPlayers() {
        Board board = new Board(2);
        assertEquals(2, board.numPlayers);
    }

    @Test
    public void testFourPlayers() {
        Board board = new Board(4);
        assertEquals(4, board.numPlayers);
    }

    @Test
    public void testFivePlayers() {
        assertThrows(RuntimeException.class, () -> new Board(5));
    }

    @Test
    public void testDeckSizeTwoPlayersOnSetup() {
        Board board = new Board(2);

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
        Board board = new Board(3);

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
        Board board = new Board(4);

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
        Board board = new Board(2);
        assertEquals(0, board.getCurrentPlayerIndex());
    }
    
    @Test
    public void testInitialPlayerList() {
        Board board = new Board(2);
        assertEquals(2, board.players.size());
    }
    
    @Test
    public void testPlayersHaveInitialHand() {
        Board board = new Board(2);
        for (int i = 0; i < 2; i++) {
            assertEquals(5, board.players.get(i).hand.size());
        }
    }
}
