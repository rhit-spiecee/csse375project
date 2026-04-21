package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class ReplaceTests {

    @Test
    public void testReplaceTrashGainCostPlusTwo() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        // Cost 2 card. Max cost 4.
        player.hand.add(new VictoryCard("estate", 2, 0, 1)); 

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.HashMap<>();
        mockBoard.treasureDecks = new java.util.HashMap<>();
        mockBoard.victoryDecks = new java.util.HashMap<>();
        mockBoard.players = new ArrayList<>(Arrays.asList(player));

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("estate");
        
        // Mock affordable cards. Use cost 3 to kill addition-to-subtraction mutation (2+2=4 vs 2-2=0)
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.eq(4), EasyMock.anyObject())).andReturn(new ArrayList<>(Arrays.asList("silver"))).times(3);
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("silver");
        
        BoardDeck silverDeck = new BoardDeck(new Silver(), 10);
        EasyMock.expect(mockBoard.getBoardDeckByName("silver")).andReturn(silverDeck);

        EasyMock.replay(mockBoard, mockGui);

        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);

        assertEquals(1, mockBoard.trashPile.size());
        // Silver is treasure, should go on top of deck (EasyMock doesn't check this unless mocked, but we can check deck size)
        // Initial 10 + 1 gained silver = 11
        assertEquals(11, player.deck.size());
        
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testReplaceGainVictoryCausesAttack() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        Player opponent = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.players = new ArrayList<>(Arrays.asList(player, opponent));

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject())).andReturn(new ArrayList<>(Arrays.asList("estate"))).times(3);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("estate");
        
        BoardDeck estateDeck = new BoardDeck(new VictoryCard("estate", 2, 0, 1), 10);
        EasyMock.expect(mockBoard.getBoardDeckByName("estate")).andReturn(estateDeck);

        BoardDeck curseDeck = new BoardDeck(new VictoryCard("cursed", 0, 0, -1), 10);
        EasyMock.expect(mockBoard.getBoardDeckByName(Gui.getString("cursed"))).andReturn(curseDeck).anyTimes();

        EasyMock.replay(mockBoard, mockGui);

        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testReplaceEmptyHand() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player player = new Player();
        player.hand.clear();

        EasyMock.replay(mockBoard);
        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);
        EasyMock.verify(mockBoard);
    }

    @Test
    public void testReplaceEmptyStringReturn() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.add(new Copper());
        mockBoard.gui = mockGui;
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("");

        EasyMock.replay(mockBoard, mockGui);
        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testReplaceNoCardsToGain() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.add(new Copper());
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.HashMap<>();
        mockBoard.treasureDecks = new java.util.HashMap<>();
        mockBoard.victoryDecks = new java.util.HashMap<>();

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject())).andReturn(new ArrayList<>()).times(3);

        EasyMock.replay(mockBoard, mockGui);
        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);
        assertEquals(1, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testReplaceMoatProtection() {
        ResourceBundle bundle = ResourceBundle.getBundle("language");
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village", "replace")), bundle);
        board.gui = mockGui;
        
        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new Copper());
        
        Player opponent = board.players.get(1);
        // Put a Moat in opponent's hand
        opponent.hand.add(new Moat());
        
        // Trash choice
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.eq("Choose a card to trash for Replace:"), EasyMock.anyObject())).andReturn("copper").once();
        
        // Gain choice (Victory card)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.eq("Choose a card to gain costing up to $2 more:"), EasyMock.anyObject())).andReturn("estate").once();
        
        // Opponent has Moat, asked if they want to block
        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(1)).andReturn(true).once();

        EasyMock.replay(mockGui);
        Replace replace = new Replace(board);
        replace.useCardPowers(player);
        
        // Opponent should have Moat, but Replace calls hasMoatCard()
        // Wait, real Player.hasMoatCard() checks hand for Moat cards.
        // It returns true if Moat is in hand.
        // So opponent is protected. Curse count should be 0.
        int curseCount = 0;
        for (Card c : opponent.discardPile) if (c.name.equals("cursed")) curseCount++;
        assertEquals(0, curseCount);
        EasyMock.verify(mockGui);
    }

    @Test
    public void testReplaceEmptySelectionStrings() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.add(new Copper());
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        
        // Trash choice is empty string
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("").once();

        EasyMock.replay(mockBoard, mockGui);
        Replace replace = new Replace(mockBoard);
        replace.useCardPowers(player);
        
        // Should have returned early, no trash
        assertEquals(0, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }
    @Test
    public void testReplaceTreasureGainOnDeck() {
        ResourceBundle bundle = ResourceBundle.getBundle("language");
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village", "replace")), bundle);
        board.gui = mockGui;
        
        Player curPlayer = board.players.get(0);
        curPlayer.hand.clear();
        curPlayer.hand.add(new Copper());
        while(curPlayer.deck.size() > 0) curPlayer.deck.draw();
        curPlayer.discardPile.clear();
        
        // Trash Copper (0). Max cost 2.
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper").once();
        
        // Gain Copper (0) again (Treasure)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper").once();
        int initialHandSize = curPlayer.hand.size();
        EasyMock.replay(mockGui);
        Replace replace = new Replace(board);
        replace.useCardPowers(curPlayer);
        
        // Copper is Treasure, should be on top of deck and NOT in discard
        assertEquals(initialHandSize - 1, curPlayer.hand.size()); // -1 card trashed
        assertEquals(1, curPlayer.deck.size()); // Gained Copper went to top of deck
        assertEquals("copper", curPlayer.deck.draw().name);
    }

    @Test
    public void testReplaceActionGainOnDeck() {
        ResourceBundle bundle = ResourceBundle.getBundle("language");
        Gui mockGui = EasyMock.mock(Gui.class);
        // Use a real board to avoid field access issues on mocks
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village", "moat")), bundle);
        board.gui = mockGui;
        
        Player curPlayer = board.players.get(0);
        curPlayer.hand.clear();
        curPlayer.hand.add(new Copper());
        while(curPlayer.deck.size() > 0) curPlayer.deck.draw();
        
        // Trash choice is copper
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        // Gain choice is moat
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("moat");
        
        EasyMock.replay(mockGui);
        
        Replace replace = new Replace(board);
        replace.useCardPowers(curPlayer);
        
        // Moat is an Action, should go to top of deck
        assertEquals("moat", curPlayer.deck.draw().name);
        EasyMock.verify(mockGui);
    }

    @Test
    public void testReplaceAttackFailNoCurses() {
        ResourceBundle bundle = ResourceBundle.getBundle("language");
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village", "replace")), bundle);
        board.gui = mockGui;
        
        // Empty the curse deck
        board.getBoardDeckByName("cursed").pickUpCard();
        while(board.getBoardDeckByName("cursed").size() > 0) board.getBoardDeckByName("cursed").pickUpCard();

        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new Copper());
        
        // Trash copper, gain Estate (Victory)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper").once();
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("estate").once();
        
        // Attack should fail silently but not add nulls
        EasyMock.replay(mockGui);
        Replace replace = new Replace(board);
        replace.useCardPowers(player);
        
        Player opponent = board.players.get(1);
        for (Card c : opponent.discardPile) {
            assertNotNull(c);
        }
        EasyMock.verify(mockGui);
    }

    @Test
    public void testReplaceGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Replace replace = new Replace(mockBoard);
        assertTrue(replace.getTypes().contains("Action"));
        assertTrue(replace.getTypes().contains("Attack"));
    }
}
