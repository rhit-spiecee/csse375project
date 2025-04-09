import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BoardTests {
    @Test
    public void testPlayerCleanupPhaseNoBuy() {
        // Setup
        GUI mockGui = EasyMock.mock(GUI.class);

        // Record
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(EasyMock.isA(BoardDTO.class))).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(BoardDTO.class))).andReturn(1);

        // Replay
        EasyMock.replay(mockGui);
        Board board = Board.fromGUI(mockGui);

        // Verify
        board.processTurn();
        assertEquals(5, board.players.getFirst().hand.size());
        assertEquals(5, board.players.getFirst().discardPile.size());
        assertEquals(1, board.currentPlayer);

        EasyMock.verify(mockGui);
    }

    @Test
    public void testGetAvailableDecksLength() {
        Board board = new Board(2);

        List<String> availableDecks = board.getAvailableDecks();

        assertEquals(17, board.getAvailableDecks().size());
    }

    @Test
    public void testGetAvailableDecksContents() {
        Board board = new Board(2);

        List<String> availableDecks = board.getAvailableDecks();

        List<String> expectedDecks = new ArrayList<>(Arrays.asList(
                "cellar", "market", "militia", "mine", "moat", "remodel",
                "smithy", "village", "workshop", "woodcutter", "copper", "silver",
                "gold", "estate", "duchy", "province", "cursed"));
        assertEquals(expectedDecks.size(), availableDecks.size());
        assertEquals(expectedDecks, availableDecks);
    }
    
    @Test
    public void testGetAvailableDecksOneEmpty() {
        Board board = new Board(2);
        for (int i = 0; i < 10; i++) {
            board.kingdomDecks.get("cellar").buyCard();
        }
        
        List<String> availableDecks = board.getAvailableDecks();

        List<String> expectedDecks = new ArrayList<>(Arrays.asList(
                "market", "militia", "mine", "moat", "remodel",
                "smithy", "village", "workshop", "woodcutter", "copper", "silver",
                "gold", "estate", "duchy", "province", "cursed"));
        assertEquals(expectedDecks.size(), availableDecks.size());
        assertEquals(expectedDecks, availableDecks);
    }
}
