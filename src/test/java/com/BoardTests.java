package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.lang.reflect.Array;
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

        assertFalse(board.gameOver);

        board.startGame();

        assertTrue(board.gameOver);
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
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Moat());
        player1.hand = hand;

        EasyMock.expect(player1.getActions()).andReturn(0);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        mockGui.showErrorPopup("Player 1 has no actions available");

        EasyMock.replay(mockGui, player1);

        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player1);

        board.actionPhase();

        assertEquals(0, board.getCurrentPlayerIndex());
        assertEquals(1, board.players.getFirst().hand.size());

        EasyMock.verify(mockGui, player1);
    }

    @Test
    public void testActionPhaseNoActionCardsAvailable() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player1 = EasyMock.mock(Player.class);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        player1.hand = hand;

        EasyMock.expect(player1.getActions()).andReturn(1);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        EasyMock.expect(player1.hasActionCardInHand()).andReturn(false);
        mockGui.showErrorPopup("Player 1 has no action cards");

        EasyMock.replay(mockGui, player1);

        Board board = Board.fromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player1);

        board.actionPhase();

        assertEquals(0, board.getCurrentPlayerIndex());
        assertEquals(1, board.players.getFirst().hand.size());

        EasyMock.verify(mockGui, player1);
    }

    
    @Test
    public void testGetCardByNameEmptyCardList() {
        Board board = new Board(2);
        ArrayList<KingdomCard> cards = new ArrayList<>();

        assertThrows(RuntimeException.class, () -> board.getCardByName(cards, ""));
    }

    @Test
    public void testGetCardByName() {
        Board board = new Board(2);
        ArrayList<KingdomCard> cards = new ArrayList<>();
        cards.add(new Moat());

        assertEquals(new Moat(), board.getCardByName(cards, "moat"));
    }

    @Test
    public void testGetActionCardToPlay() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.anyObject())).andReturn("market");

        EasyMock.replay(mockGui);

        Board board = Board.fromGui(mockGui);
        Market market = new Market();
        board.getCurrentPlayer().hand.add(market);
        String[] check = new String[1];
        check[0] = "market";

        assertEquals(check[0], board.getAvailableActionCardsInHand()[0]);
        assertEquals(market, board.getActionCardToPlay());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testGetActionCardToPlayNoActionCards() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.anyObject())).andReturn("market");

        EasyMock.replay(mockGui);

        Board board = Board.fromGui(mockGui);
        String[] check = new String[0];

        assertEquals(check.length, board.getAvailableActionCardsInHand().length);
        assertThrows(RuntimeException.class, board::getActionCardToPlay);

        EasyMock.verify(mockGui);
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

    @Test
    public void testActionPhase(){
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.anyObject())).andReturn("woodcutter");
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        board.players.getFirst().hand.add(new Woodcutter());
        board.actionPhase();

        assertEquals(5, board.players.getFirst().hand.size());
        EasyMock.verify(mockGui);
    }

    @Test
    public void testTwoBuysInARow(){
        Gui mockGui = EasyMock.niceMock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);

        EasyMock.replay(mockGui);
        Board board = Board.fromGui(mockGui);
        board.getCurrentPlayer().buy = 2;
        board.processBuyPhaseSelection("copper");
        board.processBuyPhaseSelection("copper");
        assertEquals(2, board.getCurrentPlayer().discardPile.size());
        assertEquals(0, board.getCurrentPlayer().buy);

        EasyMock.verify(mockGui);
    }

    @Test
    public void testUnknownBoardDeckName() {
        Board board = new Board(2);
        assertThrows(RuntimeException.class, () -> board.getBoardDeckByName(""));
    }

    @Test
    public void testUnknownCardToGain() {
        Board board = new Board(2);
        assertThrows(RuntimeException.class, () -> board.transferCardFromDeckToPlayer("", board.getCurrentPlayer()));
    }
}
 