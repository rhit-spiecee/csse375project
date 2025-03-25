import org.junit.Test;

import static org.junit.Assert.*;

public class BoardDeckTests {
    @Test
    public void testLowerLegalSizeOnInit() {
        BoardDeck cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 8);
        assertEquals(8, cellarDeck.size());
    }

    @Test
    public void testLowerIllegalSizeOnInit() {
        assertThrows(RuntimeException.class, () -> {
            new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 7);
        });
    }

    @Test
    public void testUpperLegalSizeOnInit() {
        BoardDeck cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 60);
        assertEquals(60, cellarDeck.size());
    }

    @Test
    public void testUpperIllegalSizeOnInit() {
        assertThrows(RuntimeException.class, () -> {
            new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 61);
        });
    }
}
