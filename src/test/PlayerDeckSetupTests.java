import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerDeckSetupTests {
    @Test
    public void testDeckSizeTen(){
        Player player = new Player();
        assertEquals(10, player.deckSize());
    }

}
