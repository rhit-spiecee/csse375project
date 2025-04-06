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
        GUI gui = EasyMock.mock(GUI.class);

        // Record
        EasyMock.expect(gui.getNumPlayers()).andReturn(2);
        EasyMock.expect(gui.getActionSelection(EasyMock.isA(String.class))).andReturn(1);
        EasyMock.expect(gui.getBuySelection(EasyMock.isA(String.class), EasyMock.anyObject())).andReturn(17);

        // Replay
        EasyMock.replay(gui);
        Board board = Board.fromGUI(gui);

        // Verify
        board.processTurn();
        assertEquals(0, board.players.getFirst().hand.size());
        assertEquals(5, board.players.getFirst().discardPile.size());
        assertEquals(1, board.currentPlayer);

        EasyMock.verify(gui);
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
}
