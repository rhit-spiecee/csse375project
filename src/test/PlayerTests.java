import org.easymock.EasyMock;
import org.junit.Test;
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

}
