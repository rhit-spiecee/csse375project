package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatrolTests {

    @Test
    public void testPatrolBasicDrawAndVictory() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        player.discardPile.clear();
        
        // Items to be revealed (4 victory) - add BOTTOM first
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));

        // Items to be drawn (3 non-victory) - add TOP last
        player.deck.addTop(new Moat());
        player.deck.addTop(new Moat());
        player.deck.addTop(new Moat());

        mockBoard.gui = mockGui;
        EasyMock.replay(mockBoard, mockGui);

        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);

        // drawn 3 (Moats) + 4 revealed victory (Estates) = 7
        assertEquals(7, player.hand.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testPatrolCurseAndRecycle() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        player.discardPile.clear();
        
        // 3 for draw (TOP)
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());
        
        // Empty deck for reveal -> triggers recycle
        // One 'cursed' card, and two non-victory cards to trigger order query
        player.discardPile.add(new MockCursedCard());
        player.discardPile.add(new Village());
        player.discardPile.add(new Village());

        mockBoard.gui = mockGui;
        
        // One choice for order query because 2 non-victory cards in put back list
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("village").once();

        EasyMock.replay(mockBoard, mockGui);

        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);

        // Hand: 3 (draw) + 1 (cursed) = 4
        assertEquals(4, player.hand.size());
        // Deck: 2 non-victory cards put back
        assertEquals(2, player.deck.size()); 
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testPatrolSinglePutBack() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        player.discardPile.clear();
        
        // Bottom (revealed last in loop)
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        // This village will be the only one in cardsToPutBack
        player.deck.addTop(new Village());

        // Top (drawn first)
        player.deck.addTop(new Village()); 
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());

        mockBoard.gui = mockGui;
        EasyMock.replay(mockBoard, mockGui);

        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);

        // Hand: 3 drawn + 3 revealed victory = 6
        assertEquals(6, player.hand.size());
        // Deck: 1 non-victory put back
        assertEquals(1, player.deck.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testPatrolEmptyDeckBoundary() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        player.discardPile.clear();
        
        // Exactly 3 for draw phase. Reveal phase reveals 0.
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());

        mockBoard.gui = mockGui;
        EasyMock.replay(mockBoard, mockGui);

        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);

        assertEquals(3, player.hand.size()); 
        assertEquals(0, player.deck.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testPatrolEmptyDeckAndDiscard() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player player = new Player();
        player.hand.clear();
        // Provide 3 cards in discard for the 3 draws at start
        player.discardPile.add(new Copper());
        player.discardPile.add(new Copper());
        player.discardPile.add(new Copper());
        while(player.deck.size() > 0) player.deck.draw();

        EasyMock.replay(mockBoard);
        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);
        // Hand has the 3 coppers drawn
        assertEquals(3, player.hand.size());
        // Deck and discard are empty, reveal phase reveals nothing
        assertEquals(0, player.deck.size());
        EasyMock.verify(mockBoard);
    }

    @Test
    public void testPatrolNullChoice() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        
        // revealed cards (next 4)
        // 2 victory, 2 non-victory (Village)
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());

        // drawn (3)
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());
        player.deck.addTop(new Village());

        mockBoard.gui = mockGui;
        // 2 non-victory in revealed -> triggers 1 query (size 2 -> size 1)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn(null).once();
        EasyMock.replay(mockBoard, mockGui);
        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);
        // Should have picked index 0 manually, total 2 cards put back
        assertEquals(2, player.deck.size());
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testPatrolExplicitOrder() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        Player player = new Player();
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        
        // revealed cards (next 4)
        Card v1 = new KingdomCard("v1", "img", 3, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
        };
        Card v2 = new KingdomCard("v2", "img", 3, 0, 0, "tip") {
            public void useCardPowers(Player p) {}
        };
        player.deck.addTop(v1); // index 1 in revealed list [v2, v1]
        player.deck.addTop(v2); // index 0 in revealed list [v2, v1] (stack behavior)
        
        // need 3 more to satisfy draws (3 revealed cards total expected in deck now = 5)
        for(int i=0; i<3; i++) player.deck.addTop(new Village());

        mockBoard.gui = mockGui;
        // revealedCards will be [v2, v1]
        // User chooses "v1" (the 2nd one)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("v1").once();

        EasyMock.replay(mockBoard, mockGui);
        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);
        
        // v1 was chosen first, added to deck. Then v2 (the last remaining) is added. 
        // v2 is on top.
        assertEquals("v2", player.deck.draw().name);
        assertEquals("v1", player.deck.draw().name);
        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testPatrolDeckBoundary() {
        Board mockBoard = EasyMock.mock(Board.class);
        Player player = new Player();
        player.hand.clear();
        while(player.deck.size() > 0) player.deck.draw();
        
        // 3 for initial draws + 5 for reveal test
        // Use Victory cards to avoid order query (they go straight to hand)
        for(int i=0; i<8; i++) player.deck.addTop(new VictoryCard("estate", 1, 0, 1));
        
        mockBoard.gui = null; // Should not be called

        EasyMock.replay(mockBoard);
        Patrol patrol = new Patrol(mockBoard);
        patrol.useCardPowers(player);
        
        // 3 drawn. 5 left. 4 revealed (all Victory). 1 left.
        // Hand size: 3 (initial) + 4 (revealed victory) = 7
        assertEquals(7, player.hand.size());
        assertEquals(1, player.deck.size());
        EasyMock.verify(mockBoard);
    }

    @Test
    public void testPatrolGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Patrol patrol = new Patrol(mockBoard);
        assertTrue(patrol.getTypes().contains("Action"));
    }

    static class MockCursedCard extends KingdomCard {
        public MockCursedCard() {
            super("cursed", "image", 0, 0, 0, "tip");
        }
        @Override
        public void useCardPowers(Player p) {}
    }
}
