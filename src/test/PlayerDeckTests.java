import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PlayerDeckTests {

    @Test
    public void testDeckSizeOnInit() {
        PlayerDeck deck = new PlayerDeck();
        assertEquals(10, deck.size());
    }

    @Test
    public void testDeckContentsOnInit() {
        PlayerDeck deck = new PlayerDeck();

        assertEquals(10, deck.size());

        int numCopper = 0;
        int numEstate = 0;

        for (int i = 0; i < 10; i++) {
            Card card = deck.draw();
            if (card.getName().equals("copper")) {
                numCopper++;
            } else if (card.getName().equals("estate")) {
                numEstate++;
            }
        }

        assertEquals(7, numCopper);
        assertEquals(3, numEstate);

    }
    
    @Test
    public void testDrawWhenEmpty() {
        PlayerDeck deck = new PlayerDeck();

        assertEquals(10, deck.size());

        emptyDeck(deck);
        
        assertThrows(NoSuchElementException.class, () -> deck.draw());
    }
    
    private void emptyDeck(PlayerDeck deck) {
        while (deck.size() > 0) {
            deck.draw();
        }
    }

}
