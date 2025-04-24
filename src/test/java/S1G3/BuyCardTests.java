package S1G3;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BuyCardTests {

    @Test
    public void testBuyOneCardWithEnoughCoins() {
        // Setup
        GUI mockGui = EasyMock.mock(GUI.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Card card = new TreasureCard("copper", 0, Card.CardType.TREASURE, 1);
        // Record
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(EasyMock.isA(BoardDTO.class))).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(BoardDTO.class))).andReturn(0).times(2);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(1).times(3);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(0).times(2);
        EasyMock.expect(mockGui.getBuySelection()).andReturn("cellar");
        mockGui.showErrorPopup("Player 1 has no buys available");

        EasyMock.expect(mockPlayer.getCoins()).andReturn(2).times(4);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<>(Arrays.asList(card, card))).times(3);
        EasyMock.expect(mockPlayer.getActions()).andReturn(1).times(3);
        mockPlayer.drawHand();
        mockPlayer.addBoughtCard(new Cellar());
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

    @Test
    public void testBuyOneCardWithoutEnoughCoins() {
        // Setup
        GUI mockGui = EasyMock.mock(GUI.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        Card card = new TreasureCard("copper", 0, Card.CardType.TREASURE, 1);

        // Record
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(EasyMock.isA(BoardDTO.class))).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(EasyMock.isA(BoardDTO.class))).andReturn(0).times(3);
        EasyMock.expect(mockGui.getBuySelection()).andReturn("market");
        mockGui.showErrorPopup("Player 1 does not have enough coins for market card.");
        EasyMock.expect(mockGui.getBuySelection()).andReturn("cellar");

        EasyMock.expect(mockPlayer.getCoins()).andReturn(2).times(6);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<>(Arrays.asList(card, card))).times(4);
        EasyMock.expect(mockPlayer.getActions()).andReturn(1).times(4);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(1).times(5);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(0).times(2);
        mockGui.showErrorPopup("Player 1 has no buys available");
        mockPlayer.drawHand();
        mockPlayer.addBoughtCard(new Cellar());
        mockPlayer.cleanup();

        // Replay
        EasyMock.replay(mockGui, mockPlayer);
        Board board = Board.fromGUI(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);

        // Verify
        board.processTurn();
        assertEquals(10, board.kingdomDecks.get("market").size());
        assertEquals(9, board.kingdomDecks.get("cellar").size());
        assertEquals(1, board.currentPlayer);

        EasyMock.verify(mockGui, mockPlayer);
    }
}
