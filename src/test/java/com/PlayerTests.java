package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTests {
    @Test
    public void testDrawInitialHandMock(){
        //Record
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        EasyMock.expect(mockDeck.size()).andReturn(5).times(5);

        //Replay
        EasyMock.replay(mockDeck);
        player.drawHand();

        //Verify
        assertEquals(5, player.hand.size());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testDrawOneCardMock() {
        //Record
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        EasyMock.expect(mockDeck.size()).andReturn(5);

        //Replay
        EasyMock.replay(mockDeck);
        player.drawOneCard();

        //Verify
        assertEquals(1, player.hand.size());
        EasyMock.verify(mockDeck);
    }
    
    @Test
    public void testGetInitialCoins() {
        //Record
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);

        //Replay
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        EasyMock.expect(mockDeck.size()).andReturn(5).times(5);
        EasyMock.replay(mockDeck);
        player.drawHand();

        //Verify
        assertEquals(5, player.getCoins());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerHasActionCardsWithNoActionCards() {
        //Record
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);

        //Replay
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        EasyMock.expect(mockDeck.size()).andReturn(5).times(5);
        EasyMock.replay(mockDeck);
        player.drawHand();

        //Verify
        assertFalse(player.hasActionCard());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerHasActionCardsWithOneActionCard() {
        //Record
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);

        //Replay
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(4);
        EasyMock.expect(mockDeck.draw()).andReturn(new Smithy());
        EasyMock.expect(mockDeck.size()).andReturn(5).times(5);
        EasyMock.replay(mockDeck);
        player.drawHand();

        //Verify
        assertTrue(player.hasActionCard());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerHasActionCardsWithFullHandActionCard() {
        //Record
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);

        //Replay
        EasyMock.expect(mockDeck.draw()).andReturn(new Moat()).times(5);
        EasyMock.expect(mockDeck.size()).andReturn(5).times(5);
        EasyMock.replay(mockDeck);
        player.drawHand();

        //Verify
        assertTrue(player.hasActionCard());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerCleanupWithZeroCards() {
        Player player = new Player();
        player.hand = new ArrayList<>();
        player.cleanup();
        assertEquals(0, player.hand.size());
        assertEquals(0, player.discardPile.size());
    }

    @Test
    public void testPlayerCleanupWithOneCard() {
        Player player = new Player();
        player.drawOneCard();
        player.cleanup();
        assertEquals(0, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

    @Test
    public void testPlayerCleanupWithFullHand() {
        Player player = new Player();
        player.drawHand();
        player.cleanup();
        assertEquals(0, player.hand.size());
        assertEquals(5, player.discardPile.size());
    }

    @Test
    public void testAddBoughtCard() {
        Player player = new Player();
        Card mockCard = EasyMock.mock(Card.class);
        player.addBoughtCard(mockCard);

        EasyMock.replay(mockCard);

        assertEquals(1, player.discardPile.size());
        EasyMock.verify(mockCard);
    }

    @Test
    public void testDrawHandWhenDeckIsEmpty() {
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);
        player.discardPile.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        EasyMock.expect(mockDeck.size()).andReturn(0);
        EasyMock.expect(mockDeck.size()).andReturn(10).times(4);
        mockDeck.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        mockDeck.shuffle();
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(5);


        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(5, player.hand.size());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testDrawHandWhenDeckHasOneCard() {
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck);
        EasyMock.expect(mockDeck.size()).andReturn(1);
        EasyMock.expect(mockDeck.size()).andReturn(0);
        EasyMock.expect(mockDeck.size()).andReturn(10).times(3);
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        mockDeck.shuffle();

        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(5, player.hand.size());
        EasyMock.verify(mockDeck);
    }
    
    @Test
    public void testGetActionCards() {
        Player player = new Player();
        
        player.hand.add(new Moat());
        player.hand.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        
        assertEquals(1, player.getActionCards().size());
        assertEquals("moat", player.getActionCards().getFirst().name);
    }

    @Test
    public void testRemoveTreasureCardsOfCostOfThree() {
        Player player = new Player();

        player.hand.add(new TreasureCard("gold", 6, Card.CardType.TREASURE, 3));

        assertEquals(3, player.getCoins());
        player.removeTreasureCardsOfCost(3);
        assertEquals(0, player.hand.size());
        assertEquals(0, player.getCoins());
    }

    @Test
    public void testRemoveTreasureCardsOfCostOfTwo() {
        Player player = new Player();

        player.hand.add(new TreasureCard("silver", 4, Card.CardType.TREASURE, 2));

        assertEquals(2, player.getCoins());
        player.removeTreasureCardsOfCost(2);
        assertEquals(0, player.hand.size());
        assertEquals(0, player.getCoins());
    }

    @Test
    public void testRemoveTreasureCardsOfCostOfOne() {
        Player player = new Player();

        player.hand.add(new TreasureCard("copper", 2, Card.CardType.TREASURE, 1));

        assertEquals(1, player.getCoins());
        player.removeTreasureCardsOfCost(1);
        assertEquals(0, player.hand.size());
        assertEquals(0, player.getCoins());
    }

    @Test
    public void testCalculateScore(){
        Player player = new Player();
        assertEquals(3, player.calculateScore());
    }

    @Test
    public void testGetCoinsInHand() {
        Player player = new Player();

        player.deck.add(new TreasureCard("gold", 6, Card.CardType.TREASURE, 3));
        player.deck.add(new Moat());

        player.hand.add(new TreasureCard("copper", 2, Card.CardType.TREASURE, 1));
        player.hand.add(new TreasureCard("silver", 4, Card.CardType.TREASURE, 2));
        player.hand.add(new TreasureCard("gold", 6, Card.CardType.TREASURE, 3));
        player.hand.add(new VictoryCard("province", 8, Card.CardType.VICTORY, 6));
        player.hand.add(new Market());

        assertEquals(6, player.getCoinsInHand());

    }

    @Test
    public void testHasMoatCardWithoutMoatCard() {
        Player player = new Player();
        assertFalse(player.hasMoatCard());
    }
}
