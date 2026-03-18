package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BoardTests {
    @Test
    public void testPlayerCleanupPhaseNoBuy() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(1);
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);

        board.processTurn();
        assertEquals(5, board.players.getFirst().getHand().size());
        assertEquals(5, board.players.getFirst().discardPile.size());
        assertEquals(1, board.getCurrentPlayerIndex());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testGetAvailableDecksLength() {
        Board board = new Board(2);

        assertEquals(17, getAllAvailableDecks(board).size());
    }

    @Test
    public void testGetAvailableDecksContents() {
        Board board = new Board(2);

        List<String> availableDecks = getAllAvailableDecks(board);

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
            board.kingdomDecks.get("cellar").pickUpCard();
        }

        List<String> availableDecks = getAllAvailableDecks(board);

        List<String> expectedDecks = new ArrayList<>(Arrays.asList(
                "market", "militia", "mine", "moat", "remodel",
                "smithy", "village", "workshop", "woodcutter", "copper", "silver",
                "gold", "estate", "duchy", "province", "cursed"));
        assertEquals(expectedDecks.size(), availableDecks.size());
        assertEquals(expectedDecks, availableDecks);
    }

    @Test
    public void testProvinceDeckEmpty() {
        Board board = new Board(2);
        board.victoryDecks.get("province").deck.clear();
        board.checkProvinceDeckLength();
        assertTrue(board.isGameOver());
    }

    @Test
    public void testProvinceDeckNotEmpty() {
        Board board = new Board(2);
        board.checkProvinceDeckLength();
        assertFalse(board.isGameOver());
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
        player1.cleanup();
        player1.drawHand();

        EasyMock.expect(player2.calculateScore()).andReturn(25);
        EasyMock.expect(player2.getHand()).andReturn(new ArrayList<>());
        EasyMock.expect(player2.getCoins()).andReturn(0);
        EasyMock.expect(player2.getActions()).andReturn(1);
        EasyMock.expect(player2.getBuys()).andReturn(1);

        EasyMock.expect(mockDeck.isNotEmpty()).andReturn(true).times(2);
        EasyMock.expect(mockDeck.isNotEmpty()).andReturn(false).times(5);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(1);
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        mockGui.displayGameOverScreen(EasyMock.anyObject(), EasyMock.eq(false));
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui, player1, player2, mockDeck);

        Board board = Board.setupBoardFromGui(mockGui);
        board.players = Arrays.asList(player1, player2);
        board.victoryDecks.put("province", mockDeck);

        board.startGame();

        assertTrue(board.isGameOver());
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
    public void testGetSortedPlayerEntriesWithTie() {
        Board board = new Board(2);

        Player player1 = EasyMock.mock(Player.class);
        Player player2 = EasyMock.mock(Player.class);
        Player player3 = EasyMock.mock(Player.class);

        EasyMock.expect(player1.calculateScore()).andReturn(25);
        EasyMock.expect(player2.calculateScore()).andReturn(25);
        EasyMock.expect(player3.calculateScore()).andReturn(25);

        EasyMock.replay(player1, player2, player3);

        board.players = Arrays.asList(player1, player2, player3);

        List<PlayerScoreEntry> sortedEntries = board.getSortedPlayerEntries();
        PlayerScoreEntry winner = sortedEntries.get(0);
        PlayerScoreEntry second = sortedEntries.get(1);
        PlayerScoreEntry third = sortedEntries.get(2);
        assertEquals(25, winner.score);
        assertEquals(player3, winner.player);
        assertEquals(3, winner.index);

        assertEquals(25, second.score);
        assertEquals(player2, second.player);
        assertEquals(2, second.index);

        assertEquals(25, third.score);
        assertEquals(player1, third.player);
        assertEquals(1, third.index);

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
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui, player1);

        Board board = Board.setupBoardFromGui(mockGui);
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
        hand.add(new TreasureCard("copper", 0, 1));
        player1.hand = hand;

        EasyMock.expect(player1.getActions()).andReturn(1);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        EasyMock.expect(player1.hasActionCardInHand()).andReturn(false);
        mockGui.showErrorPopup("Player 1 has no action cards");
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui, player1);

        Board board = Board.setupBoardFromGui(mockGui);
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
        cards.add(new Moat());

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
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);

        Board board = Board.setupBoardFromGui(mockGui);
        Market market = new Market();
        board.getCurrentPlayer().hand.add(market);
        String[] check = new String[1];
        check[0] = "market";

        assertEquals(check[0], board.getAvailableActionCardsInHand()[0]);
        assertEquals("market", board.getActionCardToPlay());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testGetActionCardToPlayNoActionCards() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);

        Board board = Board.setupBoardFromGui(mockGui);
        String[] check = new String[0];

        assertEquals(check.length, board.getAvailableActionCardsInHand().length);
        assertThrows(RuntimeException.class,
                () -> board.getCardByName(board.players.getFirst().getActionCardsInHand(), null));

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
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui, player1);

        Board board = Board.setupBoardFromGui(mockGui);
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
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new Moat());
        newHand.add(new Moat());

        board.getCurrentPlayer().hand = newHand;
        board.getCurrentPlayer().coins = 3;

        board.processBuyPhaseSelection("market");

        assertEquals(1, board.getCurrentPlayerCoins());
        assertEquals(4, board.players.getFirst().discardPile.size());
    }

    @Test
    public void testCardEqualToCoinsInHand() {
        Board board = new Board(2);

        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));

        board.getCurrentPlayer().hand = newHand;
        board.getCurrentPlayer().coins = 3;

        board.processBuyPhaseSelection("market");

        assertEquals(3, board.players.getFirst().coins);
        assertEquals(6, board.players.getFirst().discardPile.size());
    }

    @Test
    public void testCardLessThanCoinsInHand() {
        Board board = new Board(2);

        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new TreasureCard("copper", 0, 1));
        newHand.add(new TreasureCard("copper", 0, 1));

        board.getCurrentPlayer().hand = newHand;
        board.getCurrentPlayer().coins = 0;

        board.processBuyPhaseSelection("moat");

        assertEquals(0, board.players.getFirst().getCoins());
        assertEquals(3, board.players.getFirst().discardPile.size());
    }

    @Test
    public void testActionPhase() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.anyObject())).andReturn("woodcutter");
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
        board.players.getFirst().hand.add(new Woodcutter());
        board.actionPhase();

        assertEquals(5, board.players.getFirst().hand.size());
        EasyMock.verify(mockGui);
    }

    @Test
    public void testTwoBuysInARow() {
        Gui mockGui = EasyMock.niceMock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
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

    @Test
    public void testGetCardsBelowCostWhenDeckEmpty() {
        Board board = new Board(2);
        Map<String, BoardDeck> decks = new HashMap<>();
        decks.put("estate", new BoardDeck(new TreasureCard("estate", 2, 0), 8));
        for (int i = 0; i < 8; i++) {
            decks.get("estate").pickUpCard();
        }
        List<String> returnCards;
        returnCards = board.getCardsInDeckBelowCostOf(2, decks);

        assertEquals(0, returnCards.size());
    }

    @Test
    public void testGetCardsBelowCostWhenDeckIsBelowCost() {
        Board board = new Board(2);
        Map<String, BoardDeck> decks = new HashMap<>();
        decks.put("estate", new BoardDeck(new TreasureCard("estate", 2, 0), 8));
        List<String> returnCards;
        returnCards = board.getCardsInDeckBelowCostOf(3, decks);

        assertEquals(1, returnCards.size());
    }

    @Test
    public void testGetCardsBelowCostWhenDeckIsSameCost() {
        Board board = new Board(2);
        Map<String, BoardDeck> decks = new HashMap<>();
        decks.put("estate", new BoardDeck(new TreasureCard("estate", 2, 0), 8));
        List<String> returnCards;
        returnCards = board.getCardsInDeckBelowCostOf(2, decks);

        assertEquals(1, returnCards.size());
    }

    @Test
    public void testGetCardsBelowCostWhenDeckIsOverCost() {
        Board board = new Board(2);
        Map<String, BoardDeck> decks = new HashMap<>();
        decks.put("estate", new BoardDeck(new TreasureCard("estate", 2, 0), 8));
        List<String> returnCards;
        returnCards = board.getCardsInDeckBelowCostOf(1, decks);

        assertEquals(0, returnCards.size());
    }

    @Test
    public void testDiscardAnyNumbersOfCardsWhenHandSizeOne() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(0);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        mockGui.showErrorPopup("You have no more cards to discard");

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
        Player player = new Player();
        int discardedCards;

        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(new Moat());
        player.hand = newHand;

        discardedCards = board.discardAnyNumberOfCards(player);

        assertEquals(0, discardedCards);
        assertEquals(1, player.hand.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testDiscardAnyNumbersOfCardsWhenHandEmpty() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getDiscardOption()).andReturn(0);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        mockGui.showErrorPopup("You have no more cards to discard");

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
        Player player = new Player();
        int discardedCards;

        player.hand = new ArrayList<>();

        discardedCards = board.discardAnyNumberOfCards(player);

        assertEquals(0, discardedCards);
        assertEquals(0, player.hand.size());

        EasyMock.verify(mockGui);
    }

    @Test
    public void testWinConditionThreePilesEmptyWithAllThreeEmpty() {
        Board board = new Board(2);

        board.kingdomDecks.get("moat").deck.clear();
        board.treasureDecks.get("silver").deck.clear();
        board.victoryDecks.get("duchy").deck.clear();

        assertTrue(board.haveThreeEmptySupplyPiles());
    }

    @Test
    public void testWinConditionThreePilesEmptyWithJustTwoEmpty() {
        Board board = new Board(2);

        board.kingdomDecks.get("moat").deck.clear();
        board.victoryDecks.get("duchy").deck.clear();

        assertFalse(board.haveThreeEmptySupplyPiles());
    }

    @Test
    public void testWinConditionThreePilesEmptyWithFourEmpty() {
        Board board = new Board(2);

        board.kingdomDecks.get("woodcutter").deck.clear();
        board.kingdomDecks.get("militia").deck.clear();
        board.treasureDecks.get("copper").deck.clear();
        board.victoryDecks.get("estate").deck.clear();

        assertTrue(board.haveThreeEmptySupplyPiles());
    }

    @Test
    public void testStartGameWithThreePilesEmpty() {
        Gui mockGui = EasyMock.mock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        mockGui.displayGameOverScreen(EasyMock.anyObject(), EasyMock.eq(true));

        EasyMock.replay(mockGui);

        Board board = Board.setupBoardFromGui(mockGui);

        board.kingdomDecks.get("moat").deck.clear();
        board.treasureDecks.get("silver").deck.clear();
        board.victoryDecks.get("duchy").deck.clear();

        board.startGame();

        assertTrue(board.isGameOver());
        EasyMock.verify(mockGui);
    }

    @Test
    public void testPopulateDTO() {
        Board board = new Board(2);

        BoardDto boardDto = board.getDto();

        assertEquals(board.kingdomDecks, boardDto.kingdomDecks);
        assertEquals(board.treasureDecks, boardDto.treasureDecks);
        assertEquals(board.victoryDecks, boardDto.victoryDecks);
        assertEquals(board.currentPlayerIndex, boardDto.currentPlayerNumber);
        assertEquals(board.getCurrentPlayerHand(), boardDto.currentPlayerHand);
        assertEquals(board.getCurrentPlayerCoins(), boardDto.currentPlayerCoins);
        assertEquals(board.getCurrentPlayerActions(), boardDto.currentPlayerActions);
        assertEquals(board.getCurrentPlayerBuys(), boardDto.currentPlayerBuys);
    }

    @Test
    public void testGainTreasureCardCopper() {
        ArrayList<String> cardNames = new ArrayList<>();
        cardNames.add("copper");
        cardNames.add("silver");

        Gui mockGui = EasyMock.mock((Gui.class));
        Player player1 = EasyMock.mock((Player.class));
        player1.hand = new ArrayList<Card>();

        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(
                mockGui.getCardFromAvailableSelection("Enter name of a treasure card you want to gain", cardNames))
                .andReturn("copper");

        EasyMock.replay(mockGui, player1);
        Board board = Board.setupBoardFromGui(mockGui);
        board.players.clear();
        board.players.add(player1);

        assertEquals(2, board.gainTreasureCard(player1, new TreasureCard("copper", 0, 1)).size());

        EasyMock.verify(mockGui, player1);
    }

    @Test
    public void testWinOffBuyPhase() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(0).times(2);
        EasyMock.expect(mockPlayer.getBuys()).andReturn(100).times(4);
        EasyMock.expect(mockPlayer.getCoins()).andReturn(100).anyTimes();
        EasyMock.expect(mockGui.getBuySelection(EasyMock.isA(List.class))).andReturn("");
        EasyMock.expect(mockGui.getBuySelection(EasyMock.isA(List.class))).andReturn("copper");
        mockPlayer.addBoughtCard(EasyMock.isA(TreasureCard.class));
        EasyMock.expect(mockPlayer.getCoinsInHand()).andReturn(100);
        mockPlayer.removeTreasureCardsOfCost(0);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<Card>()).times(2);
        EasyMock.expect(mockPlayer.getActions()).andReturn(1).times(2);
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        mockGui.updateView(EasyMock.isA(BoardDto.class));

        EasyMock.replay(mockGui, mockPlayer);
        Board board = Board.setupBoardFromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);
        board.kingdomDecks.get("moat").deck.clear();
        board.victoryDecks.get("duchy").deck.clear();
        board.treasureDecks.get("copper").deck.clear();
        board.treasureDecks.get("copper").deck.add(new TreasureCard("copper", 0, 1));

        board.buyPhase();

        assertTrue(board.isGameOver());

        EasyMock.verify(mockGui, mockPlayer);
    }

    @Test
    public void testPassEmptyStringInActionPhase() {
        Gui mockGui = EasyMock.mock(Gui.class);

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(0);
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.isA(String[].class))).andReturn("");
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);

        EasyMock.replay(mockGui);
        Player player = new Player();
        Board board = Board.setupBoardFromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(player);
        player.hand.clear();
        player.hand.add(new Moat());
        player.hand.add(new Moat());
        board.actionPhase();

        EasyMock.verify(mockGui);

        assertEquals(2, player.hand.size());
    }

    @Test
    public void testSkipThroughTurn() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player mockPlayer = EasyMock.mock(Player.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        EasyMock.expect(mockGui.showBuyOption(0)).andReturn(1);
        EasyMock.expect(mockGui.getActionSelection(0)).andReturn(1);
        EasyMock.expect(mockPlayer.getHand()).andReturn(new ArrayList<Card>()).anyTimes();
        EasyMock.expect(mockPlayer.getCoins()).andReturn(100).anyTimes();
        EasyMock.expect(mockPlayer.getBuys()).andReturn(100).anyTimes();
        EasyMock.expect(mockPlayer.getActions()).andReturn(100).anyTimes();
        mockGui.updateView(EasyMock.isA(BoardDto.class));
        EasyMock.expectLastCall().anyTimes();
        mockPlayer.cleanup();
        mockPlayer.drawHand();

        EasyMock.replay(mockGui, mockPlayer);
        Board board = Board.setupBoardFromGui(mockGui);
        board.players.removeFirst();
        board.players.addFirst(mockPlayer);
        board.actionPhase();
        board.buyPhase();

        assertEquals(1, board.getCurrentPlayerIndex());

        EasyMock.verify(mockGui, mockPlayer);
    }

    @Test
    public void testForceMilitiaDiscardButDecline() {
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            player.hand.add(new Moat());
        }

        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE));
        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(0)).andReturn(false);
        EasyMock.expect(mockGui.getCardToDiscard(player.hand, 0)).andReturn("moat").times(2);

        EasyMock.replay(mockGui);
        Board board = Board.setupBoardFromGui(mockGui);
        board.currentPlayerIndex = 1;
        board.players.removeFirst();
        board.players.addFirst(player);
        board.players.get(1).hand.add(new Moat());
        board.forceMilitiaDiscard();

        EasyMock.verify(mockGui);
        assertEquals(3, player.hand.size());
    }

    private List<String> getAllAvailableDecks(Board board) {
        ArrayList<String> availableDecks = new ArrayList<>();
        availableDecks.addAll(board.getAvailableDecks(board.kingdomDecks));
        availableDecks.addAll(board.getAvailableDecks(board.treasureDecks));
        availableDecks.addAll(board.getAvailableDecks(board.victoryDecks));
        return availableDecks;
    }
}
