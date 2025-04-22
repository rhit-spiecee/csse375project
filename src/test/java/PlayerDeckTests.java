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
            if (card.name.equals("copper")) {
                numCopper++;
            } else if (card.name.equals("estate")) {
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

    @Test
    public void testDrawWhenOneCardLeft() {
        PlayerDeck deck = new PlayerDeck();

        assertEquals(10, deck.size());

        emptyDeck(deck);

        deck.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));

        assertEquals(1, deck.size());

        Card card = deck.draw();

        assertEquals(0, deck.size());
        assertEquals("copper", card.name);

    }

    @Test
    public void testAddWhenEmpty() {
        PlayerDeck deck = new PlayerDeck();

        assertEquals(10, deck.size());

        emptyDeck(deck);

        deck.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));

        assertEquals(1, deck.size());
    }

    @Test
    public void testAddWhenFull() {
        PlayerDeck deck = new PlayerDeck();
        
        assertEquals(10, deck.size());
        
        while (deck.size() < PlayerDeck.MAX_DECK_SIZE) {
            deck.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1));
        }
        
        assertEquals(308, deck.size());
        assertThrows(IndexOutOfBoundsException.class, () -> deck.add(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1)));
    }

    private void emptyDeck(PlayerDeck deck) {
        while (deck.size() > 0) {
            deck.draw();
        }
    }

}
