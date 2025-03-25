import org.easymock.EasyMock;
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
        //SETUP of kingdom card decks
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

        //SETUP of treasure cards
        assertEquals(46, board.copperDeck.size());
        assertEquals(40, board.silverDeck.size());
        assertEquals(30, board.goldDeck.size());

        //SETUP of Victory cards
        assertEquals(8, board.estateDeck.size());
        assertEquals(8, board.duchyDeck.size());
        assertEquals(8, board.provinceDeck.size());
        assertEquals(10, board.cursedDeck.size());
    }

    @Test
    public void testDeckSizeThreePlayersOnSetup() {
        Board board = new Board(3);
        //SETUP of kingdom card decks
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

        //SETUP of treasure cards
        assertEquals(39, board.copperDeck.size());
        assertEquals(40, board.silverDeck.size());
        assertEquals(30, board.goldDeck.size());

        //SETUP of Victory cards
        assertEquals(12, board.estateDeck.size());
        assertEquals(12, board.duchyDeck.size());
        assertEquals(12, board.provinceDeck.size());
        assertEquals(20, board.cursedDeck.size());
    }

    @Test
    public void testDeckSizeFourPlayersOnSetup() {
        Board board = new Board(4);
        //SETUP of kingdom card decks
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

        //SETUP of treasure cards
        assertEquals(32, board.copperDeck.size());
        assertEquals(40, board.silverDeck.size());
        assertEquals(30, board.goldDeck.size());

        //SETUP of Victory cards
        assertEquals(12, board.estateDeck.size());
        assertEquals(12, board.duchyDeck.size());
        assertEquals(12, board.provinceDeck.size());
        assertEquals(30, board.cursedDeck.size());
    }
    
    @Test
    public void testDrawInitialBoard() {
        GUI gui = EasyMock.mock(GUI.class);
        
        // Record
        EasyMock.expect(gui.getNumPlayers()).andReturn(2);
        
        // Replay
        EasyMock.replay(gui);
        Board board = Board.fromGUI(gui);
        
        // Verify
        EasyMock.verify(gui);
    }
    
    @Test
    public void testGetFirstPlayerMove() {
        // Setup
        GUI gui = EasyMock.mock(GUI.class);
        
        
        // Record
        EasyMock.expect(gui.getNumPlayers()).andReturn(2);
        EasyMock.expect(gui.getPlayerMove(EasyMock.isA(Board.class))).andReturn("Buy");

        // Replay
        EasyMock.replay(gui);
        Board board = Board.fromGUI(gui);
        board.startGame();

        // Verify
        EasyMock.verify(gui);
        
        
    }
    
    @Test
    public void testInitialPlayerNumber() {
        Board board = new Board(2);
        assertEquals(1, board.getCurrentPlayer());
    }

}
