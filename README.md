**Project Overview:**

Our team is tasked to make a multiplayer desktop version of the board game Dominion. The game will allow 2-4 players to play and will cycle through for each players' turns. We will be limiting our initial cards to the Dominion Donald X Vaccarino base game. We will limit our kingdom cards to the first game set specified in the recommended sets of 10 in the official rulebook.

**Testing**

For starting a game for number of players test the following inputs for number of players:



- [x] 1 player -> Don’t allow start
- [x] 2 player-> Allow game start
- [x] 4 player -> Allow game start
- [x] 5 player -> Don’t Allow

For the beginning of a new game, when handling each player's initial deck, each player is given ten cards in their deck with seven of them being copper cards and three being estate cards.


For starting deck of each player check the following:

- [x] Check deck size is empty -> Should not be empty
- [x] Check deck size of one card -> Should not be 1 card
- [x] Check deck size equals 10 cards -> Should be true
    - [x] Check three are estate cards 	// Unsure if BVA accounts for this
    - [x] Check seven copper cards		// Unsure if BVA accounts for this
- [x] Check deck is full -> Could theoretically have up to like 350 cards



For board setup:

- [x] For 4 players
    - [x] Tests
        - [x] Check exactly 12 cards of each estate, duchy, province card types
        - [x] Check for 30 cursed cards
- [x] For 3 players
    - [x] Check exactly 12 cards of each estate, duchy, province card types
    - [x] Check for 20 cursed cards
- [x] For 2 players
    - [x] Check exactly 8 cards of each estate, duchy, province card types
    - [x] Check for 10 cursed cards
- [x] For all other card types
   - [x] Check card type deck is required length
        - [x] Copper card type need 60 - [7 * (number of player)]
        - [x] Silver card type need 40
        - [x] Gold card type need 30
        - [x] Kingdom card type need 10


Draw Phase:

- [x] Players’ deck is shuffled - Did not explicitly test it because it is a one-liner method with Java's built-in shuffle

- [x] Player draws cards from deck
    - [x] Tests
        - [x] Check player draws 1 cards -> Expected when playing an action card that specifies +X cards
        - [x] Check player draws 5 cards -> Expected at the start the game
       
- [x] If the deck has less than the number of cards being drawn, draw the remaining cards, shuffle the player’s discard pile and draw cards until you have the desired amount.


Action Phase:

- [x] Check player can only play an action card if they have actions
- [x] Check player can only play an action card if they have an action card in their hand


- [x] When a player plays
    - [x] Any kingdom card
        - [x] Tests
            - [x] 0 actions available
            - [x] 1 action available
            - [x] 2 actions available

    - [x] Cellar (+1 action, discard as many cards as they’d like, receive a new card for every card discarded)
        - [x] Tests
            - [x] player gains +1 action
            - [x] Draw as many cards as they discard
         
    - [x] Market (+1 card, +1 action, +1 buy, +1 coin)
        - [x] Tests
            - [x] player hand gains +1 card
            - [x] player gains +1 action
            - [x] player gains +1 buy
            - [x] player gains +1 coin
    - [x] Militia (+2 coins, other players discard down to 3 cards)
        - [x] Tests
            - [x] Opponent already has 3 or less cards in hand
            - [x] Opponent has 5 cards
            - [x] player gains +2 coins
    - [ ] Mine (trash a treasure card, gain a treasure card up to 3 more coins than the card they discarded)
        - [ ] Tests
            - [x] Trash Copper
            - [x] Trash Silver
            - [x] Trash Gold
            - [ ] Trash with no other cards in hand
    - [ ] Moat (Blocks another players' Militia if this card is revealed before the attack, +2 cards)
        - [ ] Tests
           - [x] Play on your turn
           - [x] Play after Militia
           - [ ] Make it explicit tests
    - [ ] Remodel (Trash a card, +1 card costing up to 2 more coins then trashed card)
        - [ ] Tests
            - [x] Trash Gold and gain Province
            - [ ] Check Province is not available if trashed not Gold
    - [x] Smithy (+3 cards)
        - [x] Tests
            - [x] player hand gains +3 cards
    - [x] Village (+1 card, +2 action)
        - [x] Tests
           - [x] player hand gains 1 card
           - [x] player gains 2 actions 
    - [x] Woodcutter (+1 buy, +2 coins)
        - [x] Tests
             - [x] player gains 1 buy
             - [x] player gains 2 coins
    - [ ] Workshop (+1 card costing up to 4 coins)
        - [ ] Tests
            - [x] Pick up a card that's worth 4 coins
            - [ ] Pick up a card that's worth less than 4 coins
            - [ ] Test the helper method to show that it's impossible to pick up a card worth > 4 coins

Buy Phase:

- [x] Check that player has buys available
- [ ] Player can receive one card by paying the price on the card
    - [x]??? Test the helper method that returns cards available for purchase based on current player's coins
- [x] Purchased card is immediately placed into the player’s discard pile



Clean-up Phase:

- [ ] All cards in the players hand and any cards used that round are placed in the player’s discard pile
    - [ ] Tests
        - [ ] Test when player has 0 cards left in hand
        - [ ] Test when player has 1 card left in hand
        - [ ] Test when player has 2 cards left in hand
        - [ ] Test when player has 5 cards left in hand

End Game:

- [x] Check the supply pile of province cards is empty
    - [x] Tests (cross-checking)
        - [x] Supply pile of province cards has 0 left ->Game Over
        - [x] Supply pile of province cards has more than 0 left -> Continue Playing
- [ ] Check any three supply piles are empty (every supply card needs to be tested once)
    - [ ] Tests for each supply pile
        - [ ] Supply pile has 0 left -> Game Over
        - [ ] Supply pile has 1 left -> Continue Playing
        - [ ] Supply pile has 2 left -> Continue Playing
        - [ ] Supply pile has 10 left -> Continue Playing
- [ ] Determining Winner (Player with most points wins)
    - [ ] Tests
        - [x] Player X has the most points -> Player X wins
        - [ ] N players with the highest score are tied -> Player with fewer turns taken wins
        - [ ] N players with the highest score are tied and took same number of turns -> Tie Game
