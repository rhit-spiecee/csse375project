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
    public void testDeckSizeTwoPlayersOnSetup(){
        Board board = new Board(2);
        assertEquals(10, board.cellarDeck.size());
        assertEquals(10, board.marketDeck.size());
        assertEquals(10, board.militiaDeck.size());
        assertEquals(10, board.mineDeck.size());
        assertEquals(10, board.moatDeck.size());
        assertEquals(10, board.remodelDeck.size());
        assertEquals(10, board.smithyDeck.size());
        assertEquals(10, board.villageDeck.size());
        assertEquals(10, board.woodcutterDeck.size());
        assertEquals(10, board.workshopDeck.size());
    }

}
