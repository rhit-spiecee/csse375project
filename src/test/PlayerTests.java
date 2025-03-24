import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTests {
    @Test
    public void testDrawInitialHand() {
        Player player = new Player();
        player.drawHand();
        assertEquals(5, player.hand.size());
    }

    @Test
    public void testDrawOneCard() {
        Player player = new Player();
        player.drawOneCard();
        assertEquals(1, player.hand.size());
    }

}
