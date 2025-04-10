import org.junit.Test;

import static org.junit.Assert.*;

public class CardTests {

    @Test
    public void testEqualsDifferentClass() {
        Card card = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        String notACard = "Not a card";
        assertFalse(card.equals(notACard));
    }
}
