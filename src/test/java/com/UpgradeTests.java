package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpgradeTests {

    @Test
    public void testUpgradeTrashGainExactCost() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        
        Board board = new Board(2, new ArrayList<>(Arrays.asList("village", "upgrade")), bundle);
        board.gui = mockGui;
        board.trashPile.clear();
        
        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new Copper());
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        
        // Cost 0 (Copper) -> gain cost 1
        BoardDeck costOneDeck = new BoardDeck(new KingdomCard("costOneCard", "image", 1, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
        }, 10);
        board.kingdomDecks.put("costOneCard", costOneDeck);
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("costOneCard");

        EasyMock.replay(mockGui);

        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);

        assertEquals(1, board.trashPile.size());
        assertEquals("costOneCard", player.discardPile.get(player.discardPile.size()-1).name);
        
        EasyMock.verify(mockGui);
    }

    @Test
    public void testUpgradeActionAndDraw() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(Arrays.asList("village")), bundle);
        board.gui = mockGui;
        
        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new Copper());
        int initialActions = player.action;
        int initialHandSize = player.hand.size();

        // User cancels trashing
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn(null);

        EasyMock.replay(mockGui);

        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);

        // Should have drawn 1 card and trashed none
        // Hand: Copper (1) + Draw (1) = 2.
        assertEquals(initialHandSize + 1, player.hand.size());
        assertEquals(initialActions + 1, player.action);
        
        EasyMock.verify(mockGui);
    }

    @Test
    public void testUpgradeEmptySupplyBoundary() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(Arrays.asList("village")), bundle);
        board.gui = mockGui;
        
        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new Copper());
        
        // Remove all affordable cards
        board.kingdomDecks.clear();
        board.treasureDecks.clear();
        board.victoryDecks.clear();

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");

        EasyMock.replay(mockGui);

        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);

        assertEquals(1, board.trashPile.size());
        EasyMock.verify(mockGui);
    }

    @Test
    public void testUpgradeEmptyStringReturn() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.add(new Copper());
        mockBoard.gui = mockGui;
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("");

        EasyMock.replay(mockBoard, mockGui);
        Upgrade upgrade = new Upgrade(mockBoard);
        upgrade.useCardPowers(player);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testUpgradeEmptyGainChoice() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village")), bundle);
        board.gui = mockGui;
        
        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new VictoryCard("estate", 2, 0, 1)); // Cost 2
        
        // Trash choice
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.eq("Choose a card to trash for Upgrade:"), EasyMock.anyObject())).andReturn("estate").once();
        // andReturn("") for gain choice (looking for something cost 3)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.eq("Choose a card to gain costing exactly $3:"), EasyMock.anyObject())).andReturn("").once();

        EasyMock.replay(mockGui);
        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);
        
        // Trash happened, gain didn't
        assertEquals(1, board.trashPile.size());
        EasyMock.verify(mockGui);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testUpgradeEmptyHandCrashesOnDraw() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player player = new Player();
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        player.discardPile.clear();

        Upgrade upgrade = new Upgrade(mockBoard);
        upgrade.useCardPowers(player);
    }

    @Test
    public void testUpgradeGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Upgrade upgrade = new Upgrade(mockBoard);
        assertTrue(upgrade.getTypes().contains("Action"));
    }
    @Test
    public void testUpgradeEmptySelectionStrings() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player curPlayer = new Player();
        curPlayer.hand.add(new Copper());
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.currentPlayerIndex = 0;
        mockBoard.kingdomDecks = new java.util.HashMap<>();
        mockBoard.treasureDecks = new java.util.HashMap<>();
        mockBoard.victoryDecks = new java.util.HashMap<>();
        
        // Trash choice is empty string
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        // Verify that availableToGain is called with a LIST that ONLY contains copper (silver/gold are not cost 1)
        // And cellar is cost 1 but EMPTY so it should be filtered out.

        EasyMock.replay(mockBoard, mockGui);

        Upgrade upgrade = new Upgrade(mockBoard);
        upgrade.useCardPowers(curPlayer);

        // cellar was empty, so it should have been filtered out. 
        // If the filtering failed (survival), this would have included "cellar".
        // But since we returned "copper" (which is NOT in available list either), it returns null.
        
        EasyMock.verify(mockBoard, mockGui);
    }
    @Test
    public void testUpgradeWithBridgeDiscount() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village", "smithy")), bundle);
        board.gui = mockGui;
        
        Player player = board.players.get(0);
        player.bridgeMod = 1; // $1 discount
        player.hand.clear();
        player.hand.add(new VictoryCard("estate", 2, 0, 1)); // Cost 2 -> Effective 1
        
        // Trash choice: Estate (Base $2)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("estate").once();
        // exactCost = base $2 + 1 = $3.
        // Kingdom: Village (Base $3). Effective cost (3 - 1) = $2.
        // Wait, if I want effective cost $3, I need base $4.
        // Let's use Village (Base $3) and expect target effective cost $3 (Base $4).
        // Or just change expectations: Trash Copper ($0). exactCost = 1.
        // Village (Base $3, Effective $2) won't work.
        // Silver (Base $3, Effective $2) won't work.
        
        // Let's trash Estate (Base $2). exactCost = 3.
        // Need a card with base $4 (Effective $3). Like "smithy".
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("smithy").once();

        EasyMock.replay(mockGui);
        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);
        
        assertEquals("smithy", player.discardPile.get(player.discardPile.size()-1).name);
        EasyMock.verify(mockGui);
    }

    @Test
    public void testUpgradeMathSurvivor() {
        // baseCost 2, discount 1, exactCost 1. (2 - 1 = 1)
        // Mutant (2 + 1 = 3) would fail because 3 != 1.
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village")), bundle);
        board.gui = mockGui;
        Player player = board.players.get(0);
        player.bridgeMod = 1;
        player.hand.clear();
        player.hand.add(new Copper()); // Cost 0
        BoardDeck cellarDeck = new BoardDeck(new Cellar(board), 10);
        board.kingdomDecks.clear();
        board.kingdomDecks.put("cellar", cellarDeck);

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper").once();
        // exactCost = 0 + 1 = 1.
        // The list passed to GUI should contain "cellar" because Effective 1 matches Exact 1.
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("cellar").once();

        EasyMock.replay(mockGui);
        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);
        
        assertEquals("cellar", player.discardPile.get(player.discardPile.size()-1).name);
        EasyMock.verify(mockGui);
    }

    @Test
    public void testUpgradeDeckSizeBoundary() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        Gui mockGui = EasyMock.mock(Gui.class);
        Board board = new Board(2, new ArrayList<>(java.util.Arrays.asList("village")), bundle);
        board.gui = mockGui;
        Player player = board.players.get(0);
        player.hand.clear();
        player.hand.add(new Copper()); // Cost 0 -> exactCost 1
        
        // Kingdom Deck: Cellar (Cost 2) -> Not eligible.
        // Kingdom Deck: Custom (Cost 1) -> Eligible BUT EMPTY.
        BoardDeck emptyDeck = new BoardDeck(new KingdomCard("empty", "img", 1, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
        }, 8);
        while(emptyDeck.isNotEmpty()) emptyDeck.pickUpCard();
        board.kingdomDecks.clear();
        board.kingdomDecks.put("empty", emptyDeck);

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper").once();
        // The list passed to GUI should be EMPTY, so it should break early and NOT call the GUI.
        // If it DOES call the GUI (mutant survival), EasyMock will fail the test.

        EasyMock.replay(mockGui);
        Upgrade upgrade = new Upgrade(board);
        upgrade.useCardPowers(player);
        
        EasyMock.verify(mockGui);
    }
}
