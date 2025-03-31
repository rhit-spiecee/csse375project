import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTests {
    @Test
    public void testDrawInitialHandMock(){
        //Record
        PlayerDeck deck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(deck);
        EasyMock.expect(deck.draw()).andReturn(new Card("copper", 0, Card.CardType.TREASURE, 1)).times(5);

        //Replay
        EasyMock.replay(deck);
        player.drawHand();

        //Verify
        assertEquals(5, player.hand.size());
        EasyMock.verify(deck);
    }

    @Test
    public void testDrawOneCardMock() {
        //Record
        PlayerDeck deck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(deck);
        EasyMock.expect(deck.draw()).andReturn(new Card("copper", 0, Card.CardType.TREASURE, 1));

        //Replay
        EasyMock.replay(deck);
        player.drawOneCard();

        //Verify
        assertEquals(1, player.hand.size());
        EasyMock.verify(deck);
    }
    
    @Test
    public void testGetInitialCoins() {
        //Record
        PlayerDeck deck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(deck);

        //Replay
        EasyMock.expect(deck.draw()).andReturn(new Card("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        EasyMock.replay(deck);
        player.drawHand();

        //Verify
        assertEquals(5, player.getCoins());
        EasyMock.verify(deck);
    }

    @Test
    public void testPlayerHasActionCardsWithNoActionCards() {
        //Record
        PlayerDeck deck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(deck);

        //Replay
        EasyMock.expect(deck.draw()).andReturn(new Card("copper", 0, Card.CardType.TREASURE, 1)).times(5);
        EasyMock.replay(deck);
        player.drawHand();

        //Verify
        assertFalse(player.hasActionCard());
        EasyMock.verify(deck);
    }

    @Test
    public void testPlayerHasActionCardsWithOneActionCard() {
        //Record
        PlayerDeck deck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(deck);

        //Replay
        EasyMock.expect(deck.draw()).andReturn(new Card("copper", 0, Card.CardType.TREASURE, 1)).times(4);
        EasyMock.expect(deck.draw()).andReturn(new Card("cellar", 2, Card.CardType.KINGDOM, 0));
        EasyMock.replay(deck);
        player.drawHand();

        //Verify
        assertTrue(player.hasActionCard());
        EasyMock.verify(deck);
    }

    @Test
    public void testPlayerHasActionCardsWithFullHandActionCard() {
        //Record
        PlayerDeck deck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(deck);

        //Replay
        EasyMock.expect(deck.draw()).andReturn(new Card("moat", 2, Card.CardType.KINGDOM, 0)).times(5);
        EasyMock.replay(deck);
        player.drawHand();

        //Verify
        assertTrue(player.hasActionCard());
        EasyMock.verify(deck);
    }

    @Test
    public void testPlayerCleanupWithZeroCards() {
        Player player = new Player();
        player.hand = new ArrayList<>();   // TODO: Ask if okay
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

}
