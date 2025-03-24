import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTests {
    @Test
    public void testDrawInitialHand() {
        Player player = new Player();
        PlayerDeck deck = new PlayerDeck();
        player.drawHand();
        assertEquals(5, player.hand.size());
    }
}
