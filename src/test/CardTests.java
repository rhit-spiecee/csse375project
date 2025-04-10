import org.junit.Test;

import static org.junit.Assert.*;

public class CardTests {

    @Test
    public void testEqualsDifferentClass() {
        Card card = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        String notACard = "Not a card";
        assertFalse(card.equals(notACard));
    }

    public void testEqualsSameClassDifferentValues() {
        Card card1 = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        Card card2 = new Card("militia", 6, Card.CardType.KINGDOM, 0);
        assertFalse(card1.equals(card2));
    }
}
