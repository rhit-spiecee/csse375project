**Project Overview:**

Our team is tasked to make a multiplayer desktop version of the board game Dominion. The game will allow 2-4 players to play and will cycle through for each players' turns. We will be limiting our initial cards to the Dominion Donald X Vaccarino base game. We will limit our kingdom cards to the first game set specified in the recommended sets of 10 in the official rulebook.

**Testing**

For starting a game for number of players test the following inputs for number of players:



* 1 player -> Don’t allow start
* 2 player-> Allow game start
* 4 player -> Allow game start
* 5 player -> Don’t Allow

For the beginning of a new game, when handling each player's initial deck, each player is given ten cards in their deck with seven of them being copper cards and three being estate cards.

For starting deck of each player check the following:



* Check deck size is empty -> Should not be empty
* Check deck size of one card -> Should not be 1 card
* Check deck size equals 10 cards -> Should be true
    * Check three are estate cards 	// Unsure if BVA accounts for this
    * Check seven copper cards		// Unsure if BVA accounts for this
* Check deck is full -> Could theoretically have up to like 350 cards

For board setup:



* For 4 players
    * Tests
        * Check exactly 0 cards of each estate, duchy, province card types
        * Check exactly 12 cards of each estate, duchy, province card types
        * Check exactly 24 estates, 12 duchy, and 12 province card types
        * Check exactly 25 estates, 13 duchy, and 13 province card types
        * Check for 0 cursed cards -> Don’t want
        * Check for 1 cursed card -> Don’t want
        * Check for 30 cursed cards -> This is what we want
        * Check for 31 cursed cards -> Don’t want
* For 3 players
    * Check exactly 0 cards of each estate, duchy, province card types
    * Check exactly 12 cards of each estate, duchy, province card types
    * Check exactly 24 estates, 12 duchy, and 12 province card types
    * Check exactly 25 estates, 13 duchy, and 13 province card types
    * Check for 0 cursed cards
    * Check for 1 cursed card
    * Check for 20 cursed cards
    * Check for 30 cursed cards
    * Check for 31 cursed cards
* For 2 players
    * Check exactly 0 cards of each estate, duchy, province card types
    * Check exactly 8 cards of each estate, duchy, province card types
    * Check exactly 24 estates, 12 duchy, and 12 province card types
    * Check exactly 25 estates, 13 duchy, and 13 province card types
    * Check for 0 cursed cards
    * Check for 1 cursed card
    * Check for 10 cursed cards
    * Check for 30 cursed cards
    * Check for 31 cursed cards
* For all other card types
    * Check card type deck is empty
    * Check card type deck is size one
    * Check card type deck is required length
        * Copper card type need 60 - [7 * (number of player)]
        * Silver card type need 40
        * Gold card type need 30
        * Kingdom card type need 10
    * Check card type deck is full
* Player 1 will always start

Draw Phase:



* Players’ deck is shuffled
    * Tests
        * Test deck is actually shuffled (not same order every time) //Potential Mock
* Player draws cards from deck
    * Tests
        * Check player draws -1 cards -> Never allowed
        * Check player draws 0 cards -> Never allowed
        * Check player draws 1 cards -> Expected when playing an action card that specifies +X cards
        * Check player draws 5 cards -> Expected at the start the game
        * Check player draws 6 cards -> Could be allowed when using Cellar

          // What are our boundaries for this? Potentially separate tests?

        * Check player draws MAX_SIZE cards -> For Cellar action card
* If the deck has less than the number of cards being drawn, draw the remaining cards, shuffle the player’s discard pile and draw cards until you have 5 total.
    * Tests
* If the deck has the same size as the number of cards being drawn, draw remaining cards, then shuffle the deck for the next turn.
    * Tests

Action Phase:



* Check that up to one action card is played
* Check player will not have any action to play in his first two turns
* Check if player can play another action card because of modification from another action card
* When a player plays
    * Any kingdom card
        * Tests
            * 0 actions available
            * 1 action available
            * 2 actions available
    * Any +1 action card
        * Tests
            * Player plays -1 more action cards
            * Player plays 0 more action cards
            * Player plays 1 more action card
            * Player plays 2 more action cards
    * Any +2 action card
        * Tests
            * Player plays -1 more action cards
            * Player plays 0 more action cards
            * Player plays 2 more action card
            * Player plays 3 more action cards
    * Cellar (+1 action, discard as many cards as they’d like, receive a new card for every card discarded)
        * Tests
            * 0 cards discarded
            * 1 card discarded
            * All cards discarded
            * Hand becomes empty
    * Market (+1 card, +1 action, +1 buy, +1 coin)
        * Tests
            * Unsure
    * Militia (+2 coins, other players discard down to 3 cards)
        * Tests
            * Opponent already has 3 or less cards in hand
            * Opponent has exactly 4 cards in hand
            * Opponent has 5 or more cards in hand
    * Mine (trash a treasure card, gain a treasure card up to 3 more coins than the card they discarded)
        * Tests
            * Trash Copper
            * Trash Silver
            * Trash Gold
            * Trash a non-treasure
            * Trash with no other cards in hand
    * Moat (Blocks another players attack card if this card is revealed before the attack, +2 cards)
        * Tests
            * Play before an attack
            * Play after an attack is over
            * Play on your turn
            * Play on another player’s turn
    * Remodel (Trash a card, +1 card costing up to 2 more coins then trashed card)
        * Tests
            * Trash Copper
            * Trash highest value card
            * Trash without any other cards in hand
    * Smithy (+3 cards)
        * Tests
            * Players deck is empty
            * Players deck has 1 card
            * Players deck has 3 cards
            * Players deck has 4 cards
    * Village (+1 card, +2 action)
        * Tests
            * Players deck is empty
            * Players deck has 1 card
            * Players deck has 2 cards
    * Woodcutter (+1 buy, +2 coins)
        * Tests
            * Player has 2 more coins than before
            * Player buys 0 cards
            * Player buys 2 cards
            * Player buys 3 cards
    * Workshop (+1 card costing up to 4 coins)
        * Tests
            * Try to pick up Copper (costs 0 coins)
            * Try to pick up card that costs 1 coin
            * Try to pick up card that costs 4 coins
            * Try to pick up card that costs 5 coins

Buy Phase:



* Player can receive one card by paying the price on the card
    * Tests (assuming player has ability to buy)
        * Player tries to buy copper (0 coins) ->Always allowed
        * Player tries to buy a card costing 1 coin (with at least 1 coin) ->Allowed if player has at least 1 coin
        * Player tries to buy a card costing 2 coins (with at least 2 coins) ->Allowed if player has at least 2 coins
        * Player tries to buy a card that costs as much coins as they have -> Always allowed
        * Player tries to buy a card they can’t afford -> Never allowed
* Check if player can buy another card because of modification from an action card
    * Any +1 buy card
        * Tests
            * Player tries to buy 0 more cards -> Always allowed
            * Player tries to buy 1 more card -> Always allowed if they have money
            * Player tries to buy 2 more cards -> Never allowed
* Purchased card is immediately placed into the player’s discard pile
    * Tests
        * Purchased card is in players hand and put into the discard pile -> Duplicate card
        * Player receives 0 new cards after purchase
        * Player receives 1 new card after purchase
        * Player receives MAX_NUMBER of new cards after purchase

Clean-up Phase:



* All cards in the players hand and any cards used that round are placed in the player’s discard pile
    * Tests
        * Test when player has 0 cards left in hand
        * Test when player has 1 card left in hand
        * Test when player has 2 cards left in hand
* The next player then repeats the steps in the Draw Phase

End Game:



* Check the supply pile of province cards is empty
    * Tests (cross-checking)
        * Supply pile of province cards has 0 left ->Game Over
        * Supply pile of province cards has 1 left -> Continue Playing
        * Supply pile of province cards has 2 left -> Continue Playing
        * Supply pile of province cards has 12 left -> Continue Playing
* Check any three supply piles are empty (every supply card needs to be tested once)
    * Tests for each supply pile
        * Supply pile has 0 left -> Game Over
        * Supply pile has 1 left -> Continue Playing
        * Supply pile has 2 left -> Continue Playing
        * Supply pile has 10 left -> Continue Playing
* Determining Winner (Player with most points wins)
    * Tests
        * Player X has the most points -> Player X wins
        * N players with the highest score are tied -> Player with fewer turns taken wins
        * N players with the highest score are tied and took same number of turns -> Tie Game