**Project Overview:**

Our team is tasked to make a multiplayer desktop version of the board game Dominion. The game will allow 2-4 players to play and will cycle through for each players' turns. We will be limiting our initial cards to the Dominion Donald X Vaccarino base game. We will limit our kingdom cards to the first game set specified in the recommended sets of 10 in the official rulebook.

**Testing**

For starting a game for number of players test the following inputs for number of players:

- [x] 2 player-> Allow game start [commit](https://github.com/rhit-csse376/S1G3/commit/d19db9a6a896de9c690bbf40ef56d721913fb051)
- [x] 4 player -> Allow game start [commit](https://github.com/rhit-csse376/S1G3/commit/ab82af26e8214615e79d9a5ea8cd57a67897e09d)

For the beginning of a new game, when handling each player's initial deck, each player is given ten cards in their deck with seven of them being copper cards and three being estate cards.


For starting deck of each player check the following:

- [x] Check deck size equals 10 cards -> Should be true [commit](https://github.com/rhit-csse376/S1G3/commit/45e3d2c1806e741b88eb6aafc80add904597d391)
    - [x] Check three are estate cards [commit](https://github.com/rhit-csse376/S1G3/commit/45e3d2c1806e741b88eb6aafc80add904597d391)
    - [x] Check seven copper cards [commit](https://github.com/rhit-csse376/S1G3/commit/45e3d2c1806e741b88eb6aafc80add904597d391)

For board setup:

- [x] For 4 players [commit](https://github.com/rhit-csse376/S1G3/commit/2aed27354d36789ffb01056f02353f70152cca9d)
    - [x] Check exactly 12 cards of each estate, duchy, province card types
    - [x] Check for 30 cursed cards
- [x] For 3 players [commit](https://github.com/rhit-csse376/S1G3/commit/2aed27354d36789ffb01056f02353f70152cca9d)
    - [x] Check exactly 12 cards of each estate, duchy, province card types
    - [x] Check for 20 cursed cards
- [x] For 2 players [commit](https://github.com/rhit-csse376/S1G3/commit/2aed27354d36789ffb01056f02353f70152cca9d)
    - [x] Check exactly 8 cards of each estate, duchy, province card types
    - [x] Check for 10 cursed cards
- [x] For all other card types [commit](https://github.com/rhit-csse376/S1G3/commit/2aed27354d36789ffb01056f02353f70152cca9d)
   - [x] Check card type deck is required length
        - [x] Copper card type need 60 - [7 * (number of player)]
        - [x] Silver card type need 40
        - [x] Gold card type need 30
        - [x] Kingdom card type need 10


Draw Phase:

- [x] Players’ deck is shuffled - Did not explicitly test it because it is a one-liner method with Java's built-in shuffle

- [x] Player draws cards from deck
    - [x] Tests
        - [x] Check player draws 1 cards -> Expected when playing an action card that specifies +X cards [commit](https://github.com/rhit-csse376/S1G3/commit/03f57668af32eb99203b40961dd65c1e4cad6097)
        - [x] Check player draws 5 cards -> Expected at the start the game [commit](https://github.com/rhit-csse376/S1G3/commit/6f3c2227b72685e062802cc8ec3d1bc3795c6a61)
       
- [x] If the deck has less than the number of cards being drawn, draw the remaining cards, shuffle the player’s discard pile and draw cards until you have the desired amount. [commit](https://github.com/rhit-csse376/S1G3/commit/efd981c9a9e764c01a9e7620dd464b8929009d14)


Action Phase:

- [x] Check player can't play an action card if they have no actions [commit](https://github.com/rhit-csse376/S1G3/commit/8c38775b2f56b20c82b38bd0b4e4784264ffb582)
- [x] Check player can only play an action card if they have an action card in their hand [commit](https://github.com/rhit-csse376/S1G3/commit/2aed27354d36789ffb01056f02353f70152cca9d)


- [x] When a player plays
    - [x] Cellar (+1 action, discard as many cards as they’d like, receive a new card for every card discarded)
        - [x] Tests [#33](https://github.com/rhit-csse376/S1G3/pull/33)
            - [x] player gains +1 action
            - [x] Draw as many cards as they discard
         
    - [x] Market (+1 card, +1 action, +1 buy, +1 coin)
        - [x] Tests [#26](https://github.com/rhit-csse376/S1G3/pull/26)
            - [x] player hand gains +1 card
            - [x] player gains +1 action
            - [x] player gains +1 buy
            - [x] player gains +1 coin
    - [x] Militia (+2 coins, other players discard down to 3 cards)
        - [x] Tests [#65](https://github.com/rhit-csse376/S1G3/pull/65)
            - [x] Opponent already has 3 or less cards in hand
            - [x] Opponent has 5 cards
            - [x] player gains +2 coins
            - [x] opponent blocks militia with moat
    - [x] Mine (trash a treasure card, gain a treasure card up to 3 more coins than the card they discarded)
        - [x] Tests [#33](https://github.com/rhit-csse376/S1G3/pull/33)
            - [x] Trash Copper
            - [x] Trash Silver
            - [x] Trash Gold
            - [x] Trash with no other cards in hand
    - [x] Moat (Blocks another players' Militia if this card is revealed before the attack, +2 cards)
        - [x] Tests [#31](https://github.com/rhit-csse376/S1G3/pull/31)
           - [x] Play on your turn
           - [x] Play to block Militia
    - [x] Remodel (Trash a card, +1 card costing up to 2 more coins than trashed card)
        - [x] Tests [#33](https://github.com/rhit-csse376/S1G3/pull/33)
            - [x] Trash Gold and gain Province
            - [x] Check Province is not available if trashed not Gold
    - [x] Smithy (+3 cards)
        - [x] Tests [#27](https://github.com/rhit-csse376/S1G3/pull/27)
            - [x] player hand gains +3 cards
    - [x] Village (+1 card, +2 action)
        - [x] Tests [#31](https://github.com/rhit-csse376/S1G3/pull/31)
           - [x] player hand gains 1 card
           - [x] player gains 2 actions 
    - [x] Woodcutter (+1 buy, +2 coins)
        - [x] Tests [#31](https://github.com/rhit-csse376/S1G3/pull/31)
             - [x] player gains 1 buy
             - [x] player gains 2 coins
    - [x] Workshop (+1 card costing up to 4 coins)
        - [x] Tests [#34](https://github.com/rhit-csse376/S1G3/pull/34)
            - [x] Pick up a card that's worth 4 coins
            - [x] Pick up a card that's worth less than 4 coins
            - [x] Test the helper method to show that it's impossible to pick up a card worth > 4 coins

Buy Phase:

- [x] Buy with exactly enough coins [commit](https://github.com/rhit-csse376/S1G3/commit/c8e2859f394fe69428d7c2b961b13fe64a0c695a)
- [x] Buy with more than enough coins [commit](https://github.com/rhit-csse376/S1G3/commit/5a42c237c033701a31e33d808cad9971eb5f97a5)
- [x] Buy with not enough coins -> Should not allow [commit](https://github.com/rhit-csse376/S1G3/commit/2e4940e25386f742e210e9146cd0303b65835c0b)
- [x] Buy with not enough buys available -> Should not allow [commit](https://github.com/rhit-csse376/S1G3/commit/1f13ab270ac19158f2de06c2de7ff723eab6c61c)
- [x] Player buys an available card with enough buys and coins [commit](https://github.com/rhit-csse376/S1G3/commit/c8e2859f394fe69428d7c2b961b13fe64a0c695a)



Clean-up Phase:

- [x] All cards in the players hand and any cards used that round are placed in the player’s discard pile
    - [x] Tests
        - [x] Test when player has 0 cards left in hand [commit](https://github.com/rhit-csse376/S1G3/commit/a0b134f49446386a362e0df61d69007b3e3452ed)
        - [x] Test when player has 1 card left in hand [commit](https://github.com/rhit-csse376/S1G3/commit/efd981c9a9e764c01a9e7620dd464b8929009d14)
        - [x] Test when player has 5 cards left in hand [commit](https://github.com/rhit-csse376/S1G3/commit/c2657830a50995fccfedddf70c32bbea6a304c36)

End Game:

- [x] Check the supply pile of province cards is empty
    - [x] Tests [#47](https://github.com/rhit-csse376/S1G3/pull/47)
        - [x] Supply pile of province cards has 0 left ->Game Over
        - [x] Supply pile of province cards has more than 0 left -> Continue Playing
- [x] Check any three supply piles are empty (every supply card needs to be tested once)
    - [x] Tests for each supply pile [#71](https://github.com/rhit-csse376/S1G3/pull/71)
        - [x] 3 piles empty -> Game Over
        - [x] 2 piles empty -> Not Over
        - [x] 4 piles empty -> Game Over
- [x] Determining Winner (Player with most points wins)
    - [x] Tests
        - [x] Player X has the most points -> Player X wins [#47](https://github.com/rhit-csse376/S1G3/pull/47)
        - [x] Display winner reason [#92](https://github.com/rhit-csse376/S1G3/pull/92)
        - [x] N players with the highest score are tied -> Player with fewer turns taken wins [#89](https://github.com/rhit-csse376/S1G3/pull/89)
