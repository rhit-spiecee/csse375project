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
    
    @Test
    public void testBuyCardInvalidSize() {
        BoardDeck deck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 8);
        assertEquals(8, deck.size());
        
        for (int i = 0; i < 8; i++) {
            deck.deck.pop();
        }
        
        assertEquals(0, deck.size());
        
        assertThrows(RuntimeException.class, () -> {
            Card card = deck.buyCard();
        });
    }
    
    @Test
    public void testBuyCard() {
        Card card = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        BoardDeck deck = new BoardDeck(card, 8);
        Card boughtCard = deck.buyCard();
        
        assertEquals(7, deck.size());
        assertEquals(card, boughtCard);
    }

    @Test
    public void testGetCard() {
        Card card = new Card("cellar", 2, Card.CardType.KINGDOM, 0);
        BoardDeck deck = new BoardDeck(card, 8);
        assertEquals(card, deck.getCard());
    }
        
}
