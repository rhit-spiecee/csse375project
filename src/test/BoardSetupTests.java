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

}
