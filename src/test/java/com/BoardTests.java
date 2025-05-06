package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class BoardTests {
    @Test
    public void testPlayerCleanupPhaseNoBuy() {
        // Setup
        Gui mockGui = EasyMock.mock(Gui.class);

        // Record
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(1);
        mockGui.updateView(EasyMock.isA(BoardDto.class));

        // Replay
        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);

        // Verify
        board.processTurn();
        assertEquals(5, board.players.getFirst().getHand().size());
        assertEquals(5, board.players.getFirst().discardPile.size());
        assertEquals(1, board.getCurrentPlayerIndex());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testGetAvailableDecksLength() {
        Board board = new Board(2);

        List<String> availableDecks = board.getAllAvailableDecks();

        assertEquals(17, board.getAllAvailableDecks().size());
    }

    @Test
    public void testGetAvailableDecksContents() {
        Board board = new Board(2);

        List<String> availableDecks = board.getAllAvailableDecks();

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
        
        List<String> availableDecks = board.getAllAvailableDecks();

        List<String> expectedDecks = new ArrayList<>(Arrays.asList(
                "market", "militia", "mine", "moat", "remodel",
                "smithy", "village", "workshop", "woodcutter", "copper", "silver",
                "gold", "estate", "duchy", "province", "cursed"));
        assertEquals(expectedDecks.size(), availableDecks.size());
        assertEquals(expectedDecks, availableDecks);
    }

    @Test
    public void testProvinceDeckEmpty(){
        Board board = new Board(2);
        board.victoryDecks.get("province").deck.clear();
        board.checkProvinceDeckLength();
        assertTrue(board.gameOver);
    }

    @Test
    public void testProvinceDeckNotEmpty(){
        Board board = new Board(2);
        board.checkProvinceDeckLength();
        assertFalse(board.gameOver);
    }

    @Test
    public void testWinnerScoreAndOrdering() {
        Gui mockGui = EasyMock.mock(Gui.class);

        Player player1 = EasyMock.mock(Player.class);
        Player player2 = EasyMock.mock(Player.class);
        Player player3 = EasyMock.mock(Player.class);

        EasyMock.expect(player1.calculateScore()).andReturn(18);
        EasyMock.expect(player2.calculateScore()).andReturn(25);
        EasyMock.expect(player3.calculateScore()).andReturn(14);

        mockGui.updateView(isA(BoardDto.class));
        expectLastCall().anyTimes();

        StringBuilder capturedOutput = new StringBuilder();
        mockGui.showErrorPopup(isA(String.class));
        expectLastCall().andAnswer(() -> {
            String msg = (String) getCurrentArguments()[0];
            capturedOutput.append(msg);
            return null;
        });

        EasyMock.replay(mockGui, player1, player2, player3);

        Board board = new Board(3);
        board.gui = mockGui;
        board.players = Arrays.asList(player1, player2, player3);
        board.gameOver = true;

        board.startGame();

        EasyMock.verify(mockGui, player1, player2, player3);

        String output = capturedOutput.toString();

        assertTrue(output.contains("Winner: Player 2 with 25 points"));
        assertTrue(output.contains("1. Player 2 - 25 points"));
        assertTrue(output.contains("2. Player 1 - 18 points"));
        assertTrue(output.contains("3. Player 3 - 14 points"));
    }
}
