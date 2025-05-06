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
        assertEquals(10, board.getDeckByName("cellar").size());
        assertEquals(10, board.getDeckByName("market").size());
        assertEquals(10, board.getDeckByName("militia").size());
        assertEquals(10, board.getDeckByName("mine").size());
        assertEquals(10, board.getDeckByName("moat").size());
        assertEquals(10, board.getDeckByName("remodel").size());
        assertEquals(10, board.getDeckByName("smithy").size());
        assertEquals(10, board.getDeckByName("village").size());
        assertEquals(10, board.getDeckByName("woodcutter").size());
        assertEquals(10, board.getDeckByName("workshop").size());

        // Treasure decks
        assertEquals(46, board.getDeckByName("copper").size());
        assertEquals(40, board.getDeckByName("silver").size());
        assertEquals(30, board.getDeckByName("gold").size());

        // Victory decks
        assertEquals(8, board.getDeckByName("estate").size());
        assertEquals(8, board.getDeckByName("duchy").size());
        assertEquals(8, board.getDeckByName("province").size());
        assertEquals(10, board.getDeckByName("cursed").size());
    }

    @Test
    public void testDeckSizeThreePlayersOnSetup() {
        Board board = new Board(3);

        // Kingdom card decks
        assertEquals(10, board.getDeckByName("cellar").size());
        assertEquals(10, board.getDeckByName("market").size());
        assertEquals(10, board.getDeckByName("militia").size());
        assertEquals(10, board.getDeckByName("mine").size());
        assertEquals(10, board.getDeckByName("moat").size());
        assertEquals(10, board.getDeckByName("remodel").size());
        assertEquals(10, board.getDeckByName("smithy").size());
        assertEquals(10, board.getDeckByName("village").size());
        assertEquals(10, board.getDeckByName("woodcutter").size());
        assertEquals(10, board.getDeckByName("workshop").size());

        // Treasure decks
        assertEquals(39, board.getDeckByName("copper").size());
        assertEquals(40, board.getDeckByName("silver").size());
        assertEquals(30, board.getDeckByName("gold").size());

        // Victory decks
        assertEquals(12, board.getDeckByName("estate").size());
        assertEquals(12, board.getDeckByName("duchy").size());
        assertEquals(12, board.getDeckByName("province").size());
        assertEquals(20, board.getDeckByName("cursed").size());
    }

    @Test
    public void testDeckSizeFourPlayersOnSetup() {
        Board board = new Board(4);

        // Kingdom card decks
        assertEquals(10, board.getDeckByName("cellar").size());
        assertEquals(10, board.getDeckByName("market").size());
        assertEquals(10, board.getDeckByName("militia").size());
        assertEquals(10, board.getDeckByName("mine").size());
        assertEquals(10, board.getDeckByName("moat").size());
        assertEquals(10, board.getDeckByName("remodel").size());
        assertEquals(10, board.getDeckByName("smithy").size());
        assertEquals(10, board.getDeckByName("village").size());
        assertEquals(10, board.getDeckByName("woodcutter").size());
        assertEquals(10, board.getDeckByName("workshop").size());

        // Treasure decks
        assertEquals(32, board.getDeckByName("copper").size());
        assertEquals(40, board.getDeckByName("silver").size());
        assertEquals(30, board.getDeckByName("gold").size());

        // Victory decks
        assertEquals(12, board.getDeckByName("estate").size());
        assertEquals(12, board.getDeckByName("duchy").size());
        assertEquals(12, board.getDeckByName("province").size());
        assertEquals(30, board.getDeckByName("cursed").size());
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
