package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class PlayerTests {
    @Test
    public void testDrawInitialHandMock() {
        int handSize = Player.INITIAL_HAND_SIZE;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));
        EasyMock.expect(mockDeck.draw()).andReturn(new Copper()).times(handSize);
        EasyMock.expect(mockDeck.size()).andReturn(handSize).times(handSize);

        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(handSize, player.getHand().size());
        assertEquals(Player.INITIAL_ACTIONS, player.getActions());
        assertEquals(Player.INITIAL_BUYS, player.getBuys());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testDrawOneCardMock() {
        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));
        EasyMock.expect(mockDeck.draw()).andReturn(new Copper());
        EasyMock.expect(mockDeck.size()).andReturn(Player.INITIAL_HAND_SIZE);

        EasyMock.replay(mockDeck);
        player.drawOneCard();

        assertEquals(1, player.hand.size());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testGetInitialCoins() {
        int handSize = Player.INITIAL_HAND_SIZE;
        int value = 1;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));

        EasyMock.expect(mockDeck.draw()).andReturn(new TreasureCard("copper", 0, value, 0)).times(handSize);
        EasyMock.expect(mockDeck.size()).andReturn(handSize).times(handSize);
        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(handSize * value, player.getCoins());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerHasActionCardsWithNoActionCards() {
        int handSize = Player.INITIAL_HAND_SIZE;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));

        EasyMock.expect(mockDeck.draw()).andReturn(new Copper()).times(handSize);
        EasyMock.expect(mockDeck.size()).andReturn(handSize).times(handSize);
        EasyMock.replay(mockDeck);
        player.drawHand();

        assertFalse(player.hasActionCardInHand());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerHasActionCardsWithOneActionCard() {
        int handSize = Player.INITIAL_HAND_SIZE;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));

        EasyMock.expect(mockDeck.draw()).andReturn(new Copper()).times(handSize - 1);
        EasyMock.expect(mockDeck.draw()).andReturn(new Smithy());
        EasyMock.expect(mockDeck.size()).andReturn(handSize).times(handSize);
        EasyMock.replay(mockDeck);
        player.drawHand();

        assertTrue(player.hasActionCardInHand());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerHasActionCardsWithFullHandActionCard() {
        int handSize = Player.INITIAL_HAND_SIZE;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));

        EasyMock.expect(mockDeck.draw()).andReturn(new Moat()).times(handSize);
        EasyMock.expect(mockDeck.size()).andReturn(handSize).times(handSize);
        EasyMock.replay(mockDeck);
        player.drawHand();

        assertTrue(player.hasActionCardInHand());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testPlayerCleanupWithZeroCards() {
        Player player = new Player();
        player.hand = new ArrayList<>();
        player.cleanup();
        assertEquals(0, player.hand.size());
        assertEquals(0, player.discardPile.size());
    }

    @Test
    public void testPlayerCleanupWithOneCard() {
        Player player = new Player();
        player.drawOneCard();
        player.cleanup();
        assertEquals(0, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

    @Test
    public void testPlayerCleanupWithFullHand() {
        Player player = new Player();
        player.drawHand();
        player.cleanup();
        assertEquals(0, player.hand.size());
        assertEquals(Player.INITIAL_HAND_SIZE, player.discardPile.size());
    }

    @Test
    public void testAddBoughtCard() {
        Player player = new Player();
        Card mockCard = EasyMock.mock(Card.class);
        player.addBoughtCard(mockCard);

        EasyMock.replay(mockCard);

        assertEquals(1, player.discardPile.size());
        EasyMock.verify(mockCard);
    }

    @Test
    public void testDrawHandWhenDeckIsEmpty() {
        int handSize = Player.INITIAL_HAND_SIZE;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));
        player.discardPile.add(new Copper());
        EasyMock.expect(mockDeck.size()).andReturn(0);
        EasyMock.expect(mockDeck.size()).andReturn(10).times(handSize - 1);
        mockDeck.add(new Copper());
        mockDeck.shuffle();
        EasyMock.expect(mockDeck.draw()).andReturn(new Copper()).times(handSize);

        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(handSize, player.hand.size());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testDrawHandWhenDeckHasOneCard() {
        int handSize = Player.INITIAL_HAND_SIZE;

        PlayerDeck mockDeck = EasyMock.mock(PlayerDeck.class);
        Player player = new Player(mockDeck, ResourceBundle.getBundle(Language.ENGLISH.bundleName));
        EasyMock.expect(mockDeck.size()).andReturn(1);
        EasyMock.expect(mockDeck.size()).andReturn(0);
        EasyMock.expect(mockDeck.size()).andReturn(10).times(handSize - 2);
        EasyMock.expect(mockDeck.draw()).andReturn(new Copper()).times(handSize);
        mockDeck.shuffle();

        EasyMock.replay(mockDeck);
        player.drawHand();

        assertEquals(handSize, player.hand.size());
        EasyMock.verify(mockDeck);
    }

    @Test
    public void testGetActionCards() {
        Player player = new Player();

        player.hand.add(new Moat());
        player.hand.add(new Copper());

        assertEquals(1, player.getActionCardsInHand().size());
        assertEquals("moat", player.getActionCardsInHand().getFirst().name);
    }

    @Test
    public void testRemoveTreasureCardsOfCostOfThree() {
        Player player = new Player();

        player.hand.add(new Gold());

        assertEquals(3, player.getCoins());
        player.removeTreasureCardsOfCost(3);
        assertEquals(0, player.hand.size());
        assertEquals(0, player.getCoins());
    }

    @Test
    public void testRemoveTreasureCardsOfCostOfTwo() {
        Player player = new Player();

        player.hand.add(new TreasureCard("silver", 4, 2, 0));

        assertEquals(2, player.getCoins());
        player.removeTreasureCardsOfCost(2);
        assertEquals(0, player.hand.size());
        assertEquals(0, player.getCoins());
    }

    @Test
    public void testRemoveTreasureCardsOfCostOfOne() {
        Player player = new Player();

        player.hand.add(new TreasureCard("copper", 2, 1, 0));

        assertEquals(1, player.getCoins());
        player.removeTreasureCardsOfCost(1);
        assertEquals(0, player.hand.size());
        assertEquals(0, player.getCoins());
    }

    @Test
    public void testCalculateScore() {
        Player player = new Player();
        assertEquals(3, player.calculateScore());
    }

    @Test
    public void testGetCoinsInHand() {
        Player player = new Player();

        player.deck.add(new Gold());
        player.deck.add(new Moat());

        player.hand.add(new TreasureCard("copper", 2, 1, 0));
        player.hand.add(new TreasureCard("silver", 4, 2, 0));
        player.hand.add(new Gold());
        player.hand.add(new VictoryCard("estate", 2, 0, 1));
        player.hand.add(new Market());

        assertEquals(6, player.getCoinsInHand());

    }

    @Test
    public void testHasMoatCardWithoutMoatCard() {
        Player player = new Player();
        assertFalse(player.hasMoatCard());
    }

    @Test
    public void testHasMoatCardWithMoatCardInDeck() {
        Player player = new Player();

        player.deck.add(new Moat());

        player.hand.add(new TreasureCard("copper", 2, 1, 0));
        player.hand.add(new VictoryCard("province", 8, 0, 6));
        player.hand.add(new Market());

        assertFalse(player.hasMoatCard());
    }

    @Test
    public void testHasMoatCardWithMoatCardInHand() {
        Player player = new Player();

        player.hand.add(new Moat());
        assertTrue(player.hasMoatCard());
    }

    @Test
    public void testDiscardCardWithEmptyHand() {
        Player player = new Player();
        assertFalse(player.discardCard("copper"));
    }

    @Test
    public void testDiscardCardWithCardInHand() {
        Player player = new Player();
        player.hand.add(new Moat());
        assertTrue(player.discardCard("moat"));
    }

    @Test
    public void testTrashCardWithNoCardsInHand() {
        Player player = new Player();
        assertNull(player.trashCard("moat"));
    }

    @Test
    public void testTrashCardWithCardInHand() {
        Player player = new Player();
        Moat moat = new Moat();
        player.hand.add(moat);

        ArrayList<Card> newHandAfterTrashing = new ArrayList<>(player.hand);
        newHandAfterTrashing.remove(moat);

        assertEquals(moat, player.trashCard("moat"));
        assertEquals(newHandAfterTrashing, player.hand);
    }

    @Test
    public void testTrashCardWithCardInDeck() {
        Player player = new Player();
        Moat moat = new Moat();
        player.deck.add(moat);

        ArrayList<Card> newHandAfterTrashing = new ArrayList<>(player.hand);
        assertNull(player.trashCard("moat"));
        assertEquals(newHandAfterTrashing, player.hand);
    }

    @Test
    public void testGetCoinsAfterRemovingCardWithoutTreasureCards() {
        Player player = new Player();

        int initialCoins = 4;
        String treasureCardType = "copper";

        assertEquals(initialCoins, player.getCoinsAfterRemovingCard(initialCoins, initialCoins, treasureCardType));
    }

    @Test
    public void testGetCoinsAfterRemovingCardWithTreasureCards() {
        Player player = new Player();
        player.hand.add(new TreasureCard("copper", 2, 1, 0));

        assertEquals(0, player.getCoinsAfterRemovingCard(1, 1, "copper"));
    }

    @Test
    public void testRecycleCards() {
        Player player = new Player();

        player.emptyRemainingDeck();
        assertEquals(0, player.deck.size());
        assertEquals(10, player.hand.size());
        assertEquals(0, player.discardPile.size());

        player.discardPile.add(new TreasureCard("gold", 6, 1, 0));
        player.discardPile.add(new TreasureCard("silver", 4, 2, 0));

        player.recycleCards();
        assertEquals(2, player.deck.size());
        assertEquals(10, player.hand.size());
        assertEquals(0, player.discardPile.size());
    }

    @Test
    public void testHasTreasureCardTypeWithoutTreasureCard() {
        Player player = new Player();
        assertEquals(-1, player.hasTreasureCardType("copper"));
    }

    @Test
    public void testHasTreasureCardTypeWithTreasureCard() {
        Player player = new Player();
        player.hand.add(new TreasureCard("copper", 2, 1, 0));
        assertEquals(0, player.hasTreasureCardType("copper"));
    }

    @Test
    public void testHasTreasureCardTypeWithFullHand() {
        Player player = new Player();
        for (int i = 0; i < 4; i++) {
            player.hand.add(new Moat());
        }
        player.hand.add(new TreasureCard("copper", 2, 1, 0));
        assertEquals(4, player.hasTreasureCardType("copper"));
    }

    @Test
    public void testHasTreasureCardTypeWithFullHandFirstElementIsTreasureCard() {
        Player player = new Player();
        player.hand.add(new TreasureCard("copper", 2, 1, 0));
        for (int i = 0; i < 3; i++) {
            player.hand.add(new Moat());
        }
        player.hand.add(new TreasureCard("copper", 2, 1, 0));
        assertEquals(0, player.hasTreasureCardType("copper"));
    }

    @Test
    public void testGetCardsInHandExceptOneWithNoCardsToExclude() {
        Player player = new Player();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new TreasureCard("copper", 2, 1, 0));
        hand.add(new TreasureCard("copper", 2, 1, 0));
        hand.add(new Village());
        hand.add(new Woodcutter());
        hand.add(new VictoryCard("estate", 2, 0, 1));

        player.hand = new ArrayList<>(hand);

        assertEquals(hand, player.getCardsInHandExceptOne("silver"));
    }

    @Test
    public void testGetCardsInHandExceptOneWithOneCardToExclude() {
        Player player = new Player();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new TreasureCard("silver", 4, 2, 0));
        hand.add(new TreasureCard("copper", 2, 1, 0));
        hand.add(new Village());
        hand.add(new Woodcutter());
        hand.add(new VictoryCard("estate", 2, 0, 1));

        player.hand = new ArrayList<>(hand);
        hand.removeFirst();

        assertEquals(hand, player.getCardsInHandExceptOne("silver"));
    }

    @Test
    public void testGetCardsInHandExceptOneWithMultipleCardsToExclude() {
        Player player = new Player();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new TreasureCard("silver", 4, 2, 0));
        hand.add(new TreasureCard("silver", 4, 2, 0));
        hand.add(new Village());
        hand.add(new Woodcutter());
        hand.add(new VictoryCard("estate", 2, 0, 1));

        player.hand = new ArrayList<>(hand);
        hand.removeFirst();

        assertEquals(hand, player.getCardsInHandExceptOne("silver"));
    }

    @Test
    public void testGetTreasureCardsInHand() {
        Player player = new Player();

        Card silver = new TreasureCard("silver", 4, 2, 0);
        Card gold = new Gold();

        ArrayList<Card> treasureCards = new ArrayList<>();
        treasureCards.add(silver);
        treasureCards.add(gold);

        ArrayList<Card> hand = new ArrayList<>(treasureCards);
        hand.add(new Woodcutter());
        hand.add(new Moat());
        hand.add(new VictoryCard("province", 8, 0, 6));

        player.hand = hand;
        ArrayList<Card> treasureCardsInHand = player.getTreasureCardsInHand();

        assertEquals(treasureCards, treasureCardsInHand);
    }
}
