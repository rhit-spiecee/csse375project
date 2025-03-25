import org.junit.Test;

import static org.junit.Assert.*;

public class BoardDeckTests {
    @Test
    public void testKingdomDeckSizeOnInit() {
        BoardDeck cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 8);
        assertEquals(8, cellarDeck.size());
    }

    @Test
    public void testIllegalSizeOnInit() {
        assertThrows(RuntimeException.class, () -> {
            new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 7);
        });
    }
}
