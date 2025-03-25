import org.junit.Test;

import static org.junit.Assert.*;

public class BoardDeckTests {
    @Test
    public void testKingdomDeckSizeOnInit() {
        BoardDeck cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 10);
        assertEquals(10, cellarDeck.size());
    }
}
