import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PlayerDeckSetupTests {
    @Test public void testPlayerDeckSizeZero() {
        Player player = new Player();
        assertEquals(0, player.deckSize());
    }

    @Test
    public void testPlayerDeckSizeTen(){
        Player player = new Player();
        player.createStartingDeck();
        assertEquals(10, player.deckSize());
    }

    @Test
    public void testPlayerDeckContents(){
        Player player = new Player();
        assertEquals(Arrays.asList("Estate", "Estate", "Estate", "Copper", "Copper", "Copper", "Copper", "Copper", "Copper", "Copper"), player.createStartingDeck());
    }

}
