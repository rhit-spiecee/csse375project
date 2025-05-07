package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void testStartGameAndGameOver() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player1 = EasyMock.mock(Player.class);
        Player player2 = EasyMock.mock(Player.class);
        BoardDeck mockDeck = EasyMock.mock(BoardDeck.class);

        EasyMock.expect(player1.calculateScore()).andReturn(18);
        EasyMock.expect(player1.getHand()).andReturn(new ArrayList<>());
        EasyMock.expect(player1.getCoins()).andReturn(0);
        EasyMock.expect(player1.getActions()).andReturn(1);
        EasyMock.expect(player1.getBuys()).andReturn(1);
                
        EasyMock.expect(player2.calculateScore()).andReturn(25);
        
        EasyMock.expect(mockDeck.size()).andReturn(0).anyTimes();
        
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        mockGui.displayGameOverScreen(EasyMock.anyObject());
        
        EasyMock.replay(mockGui, player1, player2, mockDeck);
        
        Board board = Board.fromGui(mockGui);
        board.players = Arrays.asList(player1, player2);
        board.victoryDecks.put("province" , mockDeck);
        
        board.startGame();
        
        EasyMock.verify(mockGui, player1, player2, mockDeck);
    }
    
    @Test
    public void testGetSortedPlayerEntries() {
        Board board = new Board(2);
        
        Player player1 = EasyMock.mock(Player.class);
        Player player2 = EasyMock.mock(Player.class);

        EasyMock.expect(player1.calculateScore()).andReturn(18);
        EasyMock.expect(player2.calculateScore()).andReturn(25);
        
        EasyMock.replay(player1, player2);
        
        board.players = Arrays.asList(player1, player2);
        
        List<PlayerScoreEntry> sortedEntries = board.getSortedPlayerEntries();
        PlayerScoreEntry winner = sortedEntries.get(0);
        PlayerScoreEntry second = sortedEntries.get(1);
        assertEquals(25, winner.score);
        assertEquals(player2, winner.player);
        assertEquals(2, winner.index);
        
        assertEquals(18, second.score);
        assertEquals(player1, second.player);
        assertEquals(1, second.index);

        EasyMock.verify(player1, player2);
    }
    
    @Test
    public void testActionPhaseNoActionsAvailable() {
        Gui mockGui = EasyMock.mock(Gui.class);
        
        Player player1 = EasyMock.mock(Player.class);
        
        EasyMock.expect(player1.getActions()).andReturn(0);
        
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        mockGui.showErrorPopup("Player 1 has no actions available");
        
        EasyMock.replay(mockGui, player1);
        
        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player1);
        
        board.actionPhase();
        
        EasyMock.verify(mockGui, player1);
    }
    
    @Test
    public void testActionPhaseNoActionCardsAvailable() {
        Gui mockGui = EasyMock.mock(Gui.class);

        Player player1 = EasyMock.mock(Player.class);

        EasyMock.expect(player1.getActions()).andReturn(1);
        EasyMock.expect(player1.hasActionCardInHand()).andReturn(false);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        mockGui.showErrorPopup("Player 1 has no action cards");

        EasyMock.replay(mockGui, player1);

        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player1);

        board.actionPhase();

        EasyMock.verify(mockGui, player1);
    }
    
    @Test
    public void testActionPhaseActionCardsAvailable() {
        Gui mockGui = EasyMock.mock(Gui.class);

        Player player1 = EasyMock.mock(Player.class);

        EasyMock.expect(player1.getActions()).andReturn(1);
        EasyMock.expect(player1.hasActionCardInHand()).andReturn(true);
        ArrayList<KingdomCard> actionCards = new ArrayList<>();
        actionCards.add(new Market());
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Market());
        EasyMock.expect(player1.getActionCardsInHand()).andReturn(actionCards).anyTimes();
        EasyMock.expect(player1.getHand()).andReturn(hand);
        player1.drawOneCard();
        player1.discardCard(EasyMock.isA(Card.class));
        EasyMock.expect(player1.getCoins()).andReturn(0);
        EasyMock.expect(player1.getActions()).andReturn(0);
        EasyMock.expect(player1.getBuys()).andReturn(0);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockGui.getActionCardToPlay(new String[] {"market"})).andReturn("market");
        mockGui.updateView(EasyMock.isA(BoardDto.class));

        EasyMock.replay(mockGui, player1);

        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player1);

        board.actionPhase();

        EasyMock.verify(mockGui, player1);
    }
    
    @Test
    public void testBuyPhaseNoBuysAvailable() {
        Gui mockGui = EasyMock.mock(Gui.class);

        Player player1 = EasyMock.mock(Player.class);

        EasyMock.expect(player1.getBuys()).andReturn(0);
        player1.cleanup();
        player1.drawHand();

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(0);
        mockGui.showErrorPopup("Player 1 has no buys available");
        mockGui.updateView(EasyMock.isA(BoardDto.class));

        EasyMock.replay(mockGui, player1);

        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player1);

        board.buyPhase();

        assertEquals(1, board.getCurrentPlayerIndex());
        assertEquals(5, board.getCurrentPlayerHand().size());

        EasyMock.verify(mockGui, player1);
    }

    @Test
    public void testCardMoreThanCoinsInHand() {
        Board board = new Board(2);

        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new Moat());
        newHand.add(new Moat());

        board.getCurrentPlayer().hand = newHand;
        board.getCurrentPlayer().coins = 3;

        board.processBuyPhaseSelection("market");

        assertEquals(1, board.players.getFirst().coins);
        assertEquals(4, board.players.getFirst().discardPile.size());
    }

    @Test
    public void testCardEqualToCoinsInHand() {
        Board board = new Board(2);

        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        newHand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));

        board.getCurrentPlayer().hand = newHand;
        board.getCurrentPlayer().coins = 3;

        board.processBuyPhaseSelection("market");

        assertEquals(3, board.players.getFirst().coins);
        assertEquals(6, board.players.getFirst().discardPile.size());
    }


}
 