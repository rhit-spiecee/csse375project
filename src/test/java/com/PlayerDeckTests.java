package com;

import org.junit.Test;
import java.util.ResourceBundle;
import java.util.NoSuchElementException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlayerDeckTests {

    @Test
    public void testPlayerDeckShuffle() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        // Create multiple decks and check that at least one is different (statistical check for shuffle)
        // This is the best we can do without mocking Collections.shuffle which is static.
        PlayerDeck deck1 = new PlayerDeck(bundle);
        PlayerDeck deck2 = new PlayerDeck(bundle);
        
        // Probability of two 10-card decks being same after shuffle is 1/10! (very small)
        // If they are different, shuffle() was likely called.
        boolean different = false;
        for(int i=0; i<10; i++) {
            if (!deck1.getCards().get(i).name.equals(deck2.getCards().get(i).name)) {
                different = true;
                break;
            }
        }
        // This might fail once in 3.6 million runs, acceptable for killing the mutation.
        assertTrue("Decks should be shuffled differently", different);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPlayerDeckDrawEmpty() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        while(deck.size() > 0) deck.draw();
        deck.draw();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPlayerDeckAddAtIndexNegative() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        deck.addAtIndex(-1, new Copper());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPlayerDeckAddAtIndexPastSize() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        deck.addAtIndex(deck.size() + 1, new Copper());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPlayerDeckAddAtIndexFull() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        // Fill to MAX_DECK_SIZE (308)
        while(deck.size() < PlayerDeck.MAX_DECK_SIZE) {
            deck.add(new Copper());
        }
        deck.addAtIndex(0, new Copper());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPlayerDeckAddFull() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        while(deck.size() < PlayerDeck.MAX_DECK_SIZE) {
            deck.add(new Copper());
        }
        deck.add(new Copper());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPlayerDeckAddTopFull() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        while(deck.size() < PlayerDeck.MAX_DECK_SIZE) {
            deck.add(new Copper());
        }
        deck.addTop(new Copper());
    }

    @Test
    public void testCalculateDeckScore() {
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        PlayerDeck deck = new PlayerDeck(bundle);
        // Initially 3 Estates (1pt each) = 3
        assertEquals(3, deck.calculateDeckScore());
    }
}
