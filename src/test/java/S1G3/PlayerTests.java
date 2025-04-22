package S1G3;

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
        EasyMock.expect(mockDeck.size()).andReturn(5);

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
        EasyMock.expect(mockDeck.size()).andReturn(5);
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
        EasyMock.expect(mockDeck.size()).andReturn(5);
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
        EasyMock.expect(mockDeck.draw()).andReturn(new Cellar());
        EasyMock.expect(mockDeck.size()).andReturn(5);
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
        EasyMock.expect(mockDeck.size()).andReturn(5);
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
        EasyMock.expect(mockDeck.size()).andReturn(0).times(2);
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
        EasyMock.expect(mockDeck.size()).andReturn(1).times(3);
        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        mockDeck.shuffle();


        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(5, player.hand.size());
        EasyMock.verify(mockDeck);
    }



}
