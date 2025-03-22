import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PlayerDeckSetupTests {
    @Test
    public void testDeckSizeTen(){
        Player player = new Player();
        assertEquals(10, player.deckSize());
    }

    @Test
    public void testDeckContents(){
        Player player = new Player();
        assertEquals(Arrays.asList("Estate", "Estate", "Estate", "Copper", "Copper", "Copper", "Copper", "Copper", "Copper", "Copper"), player.getDeck());
    }

}
