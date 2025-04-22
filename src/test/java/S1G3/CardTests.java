package S1G3;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTests {

    @Test
    public void testEqualsDifferentClass() {
        Card card = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        String notACard = "Not a card";
        assertFalse(card.equals(notACard));
    }

    @Test
    public void testEqualsNullObject() {
        Card card = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        assertFalse(card.equals(null));
    }

    @Test
    public void testEqualsDifferentCost() {
        Card card1 = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        Card card2 = new Card("cellar", 3, Card.CardType.KINGDOM, 0);
        assertFalse(card1.equals(card2));
    }

    @Test
    public void testEqualsDifferentValue() {
        Card card1 = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        Card card2 = new Card("cellar", 2, Card.CardType.KINGDOM, 1);
        assertFalse(card1.equals(card2));
    }

    @Test
    public void testEqualsDifferentType() {
        Card card1 = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        Card card2 = new Card("cellar", 2, Card.CardType.TREASURE, 0);
        assertFalse(card1.equals(card2));
    }

    @Test
    public void testEqualsDifferentName() {
        Card card1 = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        Card card2 = new Card("village", 2, Card.CardType.KINGDOM, 0);
        assertFalse(card1.equals(card2));
    }
}
