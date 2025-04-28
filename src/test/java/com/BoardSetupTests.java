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
        assertEquals(10, board.getDeck("cellar").size());
        assertEquals(10, board.getDeck("market").size());
        assertEquals(10, board.getDeck("militia").size());
        assertEquals(10, board.getDeck("mine").size());
        assertEquals(10, board.getDeck("moat").size());
        assertEquals(10, board.getDeck("remodel").size());
        assertEquals(10, board.getDeck("smithy").size());
        assertEquals(10, board.getDeck("village").size());
        assertEquals(10, board.getDeck("woodcutter").size());
        assertEquals(10, board.getDeck("workshop").size());

        // Treasure decks
        assertEquals(46, board.getDeck("copper").size());
        assertEquals(40, board.getDeck("silver").size());
        assertEquals(30, board.getDeck("gold").size());

        // Victory decks
        assertEquals(8, board.getDeck("estate").size());
        assertEquals(8, board.getDeck("duchy").size());
        assertEquals(8, board.getDeck("province").size());
        assertEquals(10, board.getDeck("cursed").size());
    }

    @Test
    public void testDeckSizeThreePlayersOnSetup() {
        Board board = new Board(3);

        // Kingdom card decks
        assertEquals(10, board.getDeck("cellar").size());
        assertEquals(10, board.getDeck("market").size());
        assertEquals(10, board.getDeck("militia").size());
        assertEquals(10, board.getDeck("mine").size());
        assertEquals(10, board.getDeck("moat").size());
        assertEquals(10, board.getDeck("remodel").size());
        assertEquals(10, board.getDeck("smithy").size());
        assertEquals(10, board.getDeck("village").size());
        assertEquals(10, board.getDeck("woodcutter").size());
        assertEquals(10, board.getDeck("workshop").size());

        // Treasure decks
        assertEquals(39, board.getDeck("copper").size());
        assertEquals(40, board.getDeck("silver").size());
        assertEquals(30, board.getDeck("gold").size());

        // Victory decks
        assertEquals(12, board.getDeck("estate").size());
        assertEquals(12, board.getDeck("duchy").size());
        assertEquals(12, board.getDeck("province").size());
        assertEquals(20, board.getDeck("cursed").size());
    }

    @Test
    public void testDeckSizeFourPlayersOnSetup() {
        Board board = new Board(4);

        // Kingdom card decks
        assertEquals(10, board.getDeck("cellar").size());
        assertEquals(10, board.getDeck("market").size());
        assertEquals(10, board.getDeck("militia").size());
        assertEquals(10, board.getDeck("mine").size());
        assertEquals(10, board.getDeck("moat").size());
        assertEquals(10, board.getDeck("remodel").size());
        assertEquals(10, board.getDeck("smithy").size());
        assertEquals(10, board.getDeck("village").size());
        assertEquals(10, board.getDeck("woodcutter").size());
        assertEquals(10, board.getDeck("workshop").size());

        // Treasure decks
        assertEquals(32, board.getDeck("copper").size());
        assertEquals(40, board.getDeck("silver").size());
        assertEquals(30, board.getDeck("gold").size());

        // Victory decks
        assertEquals(12, board.getDeck("estate").size());
        assertEquals(12, board.getDeck("duchy").size());
        assertEquals(12, board.getDeck("province").size());
        assertEquals(30, board.getDeck("cursed").size());
    }

    @Test
    public void testInitialPlayerNumber() {
        Board board = new Board(2);
        assertEquals(0, board.getCurrentPlayerNumber());
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
