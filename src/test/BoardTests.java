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
        EasyMock.expect(mockGui.getActionSelection(EasyMock.isA(String.class))).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(String.class))).andReturn(1);

        // Replay
        EasyMock.replay(mockGui);
        Board board = Board.fromGUI(mockGui);

        // Verify
        board.processTurn();
        assertEquals(0, board.players.getFirst().hand.size());
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
    
    @Test
    public void testBuyOneCardWithEnoughCoins() {
        // Setup
        GUI mockGui = EasyMock.mock(GUI.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Card card = new Card("copper", 0, Card.CardType.TREASURE, 1);
        // Record
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(EasyMock.isA(String.class))).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(String.class))).andReturn(0);
        EasyMock.expect(mockGui.getBuySelection()).andReturn("cellar");
        
        EasyMock.expect(mockPlayer.getCoins()).andReturn(2).times(2);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<>(Arrays.asList(card, card))).times(2);
        EasyMock.expect(mockPlayer.getActions()).andReturn(1).times(2);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(1).times(2);
        mockPlayer.addBoughtCard(new Card("cellar", 2, Card.CardType.KINGDOM, 0));
        mockPlayer.cleanup();
        
        // Replay
        EasyMock.replay(mockGui, mockPlayer);
        Board board = Board.fromGUI(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);
        
        // Verify
        board.processTurn();
        assertEquals(9, board.kingdomDecks.get("cellar").size());
        assertEquals(1, board.currentPlayer);

        EasyMock.verify(mockGui, mockPlayer);
    }
}
