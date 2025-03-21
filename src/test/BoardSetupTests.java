import org.junit.Test;

import static org.junit.Assert.*;

public class BoardSetupTests {

    @Test
    public void testOnePlayer() {
        Board board = new Board();
        assertFalse(board.startGame(1));
    }

    @Test
    public void testTwoPlayers() {
        Board board = new Board();
        assertTrue(board.startGame(2));
    }

    @Test
    public void testFourPlayers() {
        Board board = new Board();
        assertTrue(board.startGame(4));
    }

    @Test
    public void testFivePlayers() {
        Board board = new Board();
        assertFalse(board.startGame(5));
    }

    @Test
    public void testTwoPlayerCursedCard() {
        Board board = new Board();
        assertTrue(board.startGame(2));
        assertEquals(10, board.getCursedDeck().size());
    }
}
