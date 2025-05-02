package com;

import java.util.*;

public class Board {
    List<Player> players;
    Map<String, BoardDeck> kingdomDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> treasureDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> victoryDecks = new LinkedHashMap<>();

    boolean gameOver = false;
    int numPlayers;
    int currentPlayer;
    Gui gui;

    public Board(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4) {
            throw new RuntimeException("Number of players must be between 2 and 4");
        }
        this.numPlayers = numPlayers;
        this.currentPlayer = 0;

        initializeDecks();
        initializePlayers();
    }

    private void initializePlayers() {
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player();
            player.drawHand();
            players.add(player);
        }
    }

    private void initializeDecks() {

        int kingdomDeckSize = 10;

        kingdomDecks.put("cellar", new BoardDeck(new Cellar(this), kingdomDeckSize));
        kingdomDecks.put("market", new BoardDeck(new Market(), kingdomDeckSize));
        kingdomDecks.put("militia", new BoardDeck(new Militia(this), kingdomDeckSize));
        kingdomDecks.put("mine", new BoardDeck(new Mine(this), kingdomDeckSize));
        kingdomDecks.put("moat", new BoardDeck(new Moat(), kingdomDeckSize));
        kingdomDecks.put("remodel", new BoardDeck(new Remodel(this), kingdomDeckSize));
        kingdomDecks.put("smithy", new BoardDeck(new Smithy(), kingdomDeckSize));
        kingdomDecks.put("village", new BoardDeck(new Village(), kingdomDeckSize));
        kingdomDecks.put("workshop", new BoardDeck(new Workshop(this), kingdomDeckSize));
        kingdomDecks.put("woodcutter", new BoardDeck(new Woodcutter(), kingdomDeckSize));

        int copperDeckSize = 60 - (numPlayers * 7);
        int silverDeckSize = 40;
        int goldDeckSize = 30;
        // Treasure Cards
        treasureDecks.put(
                "copper",
                new BoardDeck(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1),
                        copperDeckSize));
        treasureDecks.put(
                "silver",
                new BoardDeck(new TreasureCard("silver", 3, Card.CardType.TREASURE, 2),
                        silverDeckSize));
        treasureDecks.put(
                "gold",
                new BoardDeck(new TreasureCard("gold", 6, Card.CardType.TREASURE, 3),
                        goldDeckSize));

        int cursedDeckSize = (numPlayers - 1) * 10;
        int victoryCardDeckSize = (numPlayers == 2) ? 8 : 12;
        // Victory Cards
        victoryDecks.put(
                "estate",
                new BoardDeck(new VictoryCard("estate", 2, Card.CardType.VICTORY, 1),
                        victoryCardDeckSize));
        victoryDecks.put(
                "duchy",
                new BoardDeck(new VictoryCard("duchy", 5, Card.CardType.VICTORY, 3),
                        victoryCardDeckSize));
        victoryDecks.put(
                "province",
                new BoardDeck(new VictoryCard("province", 8, Card.CardType.VICTORY, 6),
                        victoryCardDeckSize));
        victoryDecks.put(
                "cursed",
                new BoardDeck(new VictoryCard("cursed", 0, Card.CardType.VICTORY, -1),
                        cursedDeckSize));
    }


    public static Board fromGui(Gui gui) {
        int numPlayers = gui.getNumPlayers();
        Board board = new Board(numPlayers);
        board.gui = gui;

        return board;
    }

    public void startGame() {
        while (!gameOver) {
            gui.updateView(getDto());
            processTurn();
            checkProvinceDeckLength();
        }
        StringBuilder finalMessage = new StringBuilder("Province Deck Empty! Game Over!\n");
        List<PlayerScoreEntry> scoredPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            int score = players.get(i).calculateScore(); // only calculate once
            scoredPlayers.add(new PlayerScoreEntry(i + 1, players.get(i), score));
        }

        scoredPlayers.sort((a, b) -> Integer.compare(b.score, a.score));

        PlayerScoreEntry winner = scoredPlayers.get(0);
        finalMessage.append("Winner: Player ").append(winner.index)
                .append(" with ").append(winner.score).append(" points!\n\n");

        int rank = 1;
        for (PlayerScoreEntry entry : scoredPlayers) {
            finalMessage.append(rank).append(". Player ")
                    .append(entry.index).append(" - ")
                    .append(entry.score).append(" points\n");
            rank++;
        }

        gui.showErrorPopup(finalMessage.toString());
    }

    private static class PlayerScoreEntry {
        int index;
        Player player;
        int score;

        PlayerScoreEntry(int index, Player player, int score) {
            this.index = index;
            this.player = player;
            this.score = score;
        }
    }

    public boolean checkProvinceDeckLength() {
        if (victoryDecks.get("province").size() <= 0) {
            gameOver = true;
            return true;
        }
        return false;
    }

    void processTurn() {
        actionPhase();
        buyPhase();
    }

    private void actionPhase() {
        int actionSelection = gui.getActionSelection(currentPlayer);
        while (actionSelection == 0) {
            if (checkProvinceDeckLength()) return;
            if (players.get(currentPlayer).getActions() <= 0) {
                gui.showErrorPopup("Player " + (currentPlayer + 1) + " has no actions available");
                break;
            }
            try {
                processActionMove();
            } catch (RuntimeException e) {
                gui.showErrorPopup(e.getMessage());
                break;
            }
            gui.updateView(getDto());
            actionSelection = gui.getActionSelection(currentPlayer);
        }
    }

    private BoardDto getDto() {
        BoardDto boardDto = new BoardDto();
        boardDto.populate(
                kingdomDecks,
                treasureDecks,
                victoryDecks,
                getCurrentPlayerNumber(),
                getCurrentPlayerHand(),
                getCurrentPlayerCoins(),
                getCurrentPlayerActions(),
                getCurrentPlayerBuys()
        );
        return boardDto;
    }

    private void processActionMove() {
        if (checkProvinceDeckLength()) return;
        if (!players.get(currentPlayer).hasActionCard()) {
            throw new RuntimeException("Player " + (currentPlayer + 1) + " has no action cards");
        } else {
            KingdomCard card = getActionCardToPlay();
            card.useActionCard(players.get(currentPlayer));
        }
    }

    public String[] getAvailableActionCardsInHand() {
        int index = 0;
        Player currentPlayer = players.get(this.currentPlayer);
        String[] availableActionCardsInHand = new String[getCurrentPlayerHand().size()];
        for (Card c : currentPlayer.hand) {
            if (c.type.equals(Card.CardType.KINGDOM)) { // TODO: change card to have isAction
                availableActionCardsInHand[index] = c.name;
                index++;
            }
        }
        return availableActionCardsInHand;
    }

    private KingdomCard getActionCardToPlay() {
        List<KingdomCard> actionCards = players.get(currentPlayer).getActionCards();
        String cardToPlay = gui.getActionCardToPlay(getAvailableActionCardsInHand()).toLowerCase();
        return getCardByName(actionCards, cardToPlay);
    }

    private KingdomCard getCardByName(List<KingdomCard> cards, String name) {
        for (KingdomCard card : cards) {
            if (card.name.equals(name)) {
                return card;
            }
        }
        return null;
    }

    private void buyPhase() {
        if (checkProvinceDeckLength()) return;
        int buySelection = gui.showBuyOption(currentPlayer);
        while (buySelection == 0) {
            if (checkProvinceDeckLength()) return;
            if (players.get(currentPlayer).getBuys() <= 0) {
                gui.showErrorPopup("Player " + (currentPlayer + 1) + " has no buys available");
                break;
            }
            String cardToBuy = gui.getBuySelection(
                    getAllCardsBelowCostOf(players.get(currentPlayer).getCoins()));
            if (cardToBuy != null) {
                processBuyPhaseSelection(cardToBuy.toLowerCase());
            }
            gui.updateView(getDto());
            buySelection = gui.showBuyOption(currentPlayer);
        }
        endTurn();
    }

    // TODO: we need to remove these unused methods,
    //  and rework the tests to test getAllCardsBelowCostOf
    public List<String> getAllAvailableDecks() {
        ArrayList<String> availableDecks = new ArrayList<>();
        availableDecks.addAll(getAvailableDecks(kingdomDecks));
        availableDecks.addAll(getAvailableDecks(treasureDecks));
        availableDecks.addAll(getAvailableDecks(victoryDecks));
        return availableDecks;
    }

    private List<String> getAvailableDecks(Map<String, BoardDeck> decks) {
        List<String> availableDecks = new ArrayList<>();
        for (Map.Entry<String, BoardDeck> entry : decks.entrySet()) {
            BoardDeck deck = entry.getValue();
            if (deck.isNotEmpty()) {
                availableDecks.add(entry.getKey());
            }
        }

        return availableDecks;
    }

    private void processBuyPhaseSelection(String buySelection) {
        if (checkProvinceDeckLength()) return;
        BoardDeck deckToBuyFrom = getBoardDeckFromName(buySelection);
        Card boughtCard = deckToBuyFrom.buyCard();
        players.get(currentPlayer).addBoughtCard(boughtCard);
        players.get(currentPlayer).buy--;
        Player currentPlayer = players.get(this.currentPlayer);

        int coinsInHand = currentPlayer.getCoinsInHand();
        if (coinsInHand >= boughtCard.cost) {
            currentPlayer.removeTreasureCardsOfCost(boughtCard.cost);
        } else {
            currentPlayer.coins -= (boughtCard.cost - coinsInHand);
            currentPlayer.removeTreasureCardsOfCost(coinsInHand);
        }
    }

    private BoardDeck getBoardDeckFromName(String nameOfDeck) {
        if (kingdomDecks.containsKey(nameOfDeck)) {
            return kingdomDecks.get(nameOfDeck);
        }
        if (treasureDecks.containsKey(nameOfDeck)) {
            return treasureDecks.get(nameOfDeck);
        }
        if (victoryDecks.containsKey(nameOfDeck)) {
            return victoryDecks.get(nameOfDeck);
        }
        throw new RuntimeException("Unknown kingdom deck: " + nameOfDeck);
    }

    private void endTurn() {
        players.get(currentPlayer).cleanup();
        players.get(currentPlayer).drawHand();
        currentPlayer = (currentPlayer + 1) % numPlayers;
        gui.updateView(getDto());
    }

    public int getCurrentPlayerNumber() {
        return currentPlayer;
    }

    public ArrayList<Card> getCurrentPlayerHand() {
        Player player = players.get(currentPlayer);
        return player.getHand();
    }

    public int getCurrentPlayerCoins() {
        return players.get(currentPlayer).getCoins();
    }

    public void forceMilitiaDiscard() {
        for (int i = 0; i < numPlayers; i++) {
            Player player = players.get(i);
            if (currentPlayer == i) {
                continue;
            }
            if (player.hasMoatCard()) {
                boolean blockMilitia = gui.getIfPlayerWantsToBlock(i);
                if (blockMilitia) {
                    continue;
                }
            }
            while (player.hand.size() > 3) {
                String cardToDiscard = gui.getCardToDiscard(player.hand, i);
                player.discardCard(cardToDiscard);
            }
        }
    }

    public BoardDeck getDeck(String name) {
        if (kingdomDecks.containsKey(name)) {
            return kingdomDecks.get(name);
        }
        if (treasureDecks.containsKey(name)) {
            return treasureDecks.get(name);
        }
        if (victoryDecks.containsKey(name)) {
            return victoryDecks.get(name);
        }
        return null;
    }

    public int getCurrentPlayerBuys() {
        return players.get(currentPlayer).getBuys();
    }

    public int getCurrentPlayerActions() {
        return players.get(currentPlayer).getActions();
    }

    public void discardAnyCard(Player player) {
        String popupMessage = "Enter name of the card you want to discard";
        ArrayList<String> cardNames = player.getCardsInHandNamesExcept("cellar");

        String cardToDiscard = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        while (cardNamesDoNotContainCardIgnoreCase(cardNames, cardToDiscard)) {
            cardToDiscard = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        }

        player.discardCard(cardToDiscard);
    }

    public Card trashAnyCard(Player player) {
        String popupMessage = "Enter name of a card you want to trash";
        ArrayList<String> cardNames = player.getCardsInHandNamesExcept("remodel");
        return trashCard(popupMessage, cardNames, player);
    }

    public Card trashTreasureCard(Player player) {
        String popupMessage = "Enter name of a treasure card you want to trash";
        ArrayList<String> cardNames = player.getTreasureCardsInHandNames();
        return trashCard(popupMessage, cardNames, player);
    }

    private Card trashCard(String popupMessage, ArrayList<String> cardNames, Player player) {
        String cardToTrash = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        while (cardNamesDoNotContainCardIgnoreCase(cardNames, cardToTrash)) {
            cardToTrash = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        }
        return player.trashCard(cardToTrash);
    }

    public void gainAnyCard(Player player, int maxCost) {
        ArrayList<String> cardNames = getAllCardsBelowCostOf(maxCost);

        String popupMessage = "Enter name of a card you want to gain";

        gainCard(popupMessage, cardNames, player);
    }

    private ArrayList<String> getAllCardsBelowCostOf(int maxCost) {
        ArrayList<String> cardNames = new ArrayList<>();

        cardNames.addAll(getCardsBelowCostOf(maxCost, kingdomDecks));
        cardNames.addAll(getCardsBelowCostOf(maxCost, treasureDecks));
        cardNames.addAll(getCardsBelowCostOf(maxCost, victoryDecks));
        return cardNames;
    }

    private List<String> getCardsBelowCostOf(int maxCost, Map<String, BoardDeck> decks) {
        ArrayList<String> cardNames = new ArrayList<>();

        for (String cardName : decks.keySet()) {
            BoardDeck deck = decks.get(cardName);
            if (deck.isNotEmpty() && deck.card.cost <= maxCost) {
                cardNames.add(cardName);
            }
        }

        return cardNames;
    }

    public void gainTreasureCard(Player player, Card trashedCard) {
        ArrayList<String> cardNames = new ArrayList<>();
        if (trashedCard.name.equalsIgnoreCase("copper")) {
            cardNames.add("copper");
            cardNames.add("silver");
        } else if (trashedCard.name.equalsIgnoreCase("silver")
                || trashedCard.name.equalsIgnoreCase("gold")) {
            cardNames.add("copper");
            cardNames.add("silver");
            cardNames.add("gold");
        } else {
            throw new RuntimeException("Unknown trashed card name: " + trashedCard.name);
        }

        String popupMessage = "Enter name of a treasure card you want to gain";

        gainCard(popupMessage, cardNames, player);
    }

    private void gainCard(String popupMessage, ArrayList<String> cardNames, Player player) {
        String cardToGain = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        while (cardNamesDoNotContainCardIgnoreCase(cardNames, cardToGain)) {
            cardToGain = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        }
        transferCardFromDeckToPlayer(cardToGain, player);
    }

    private void transferCardFromDeckToPlayer(String cardToGain, Player player) {
        Card card;
        if (kingdomDecks.containsKey(cardToGain)) {
            card = kingdomDecks.get(cardToGain).buyCard();
        } else if (treasureDecks.containsKey(cardToGain)) {
            card = treasureDecks.get(cardToGain).buyCard();
        } else if (victoryDecks.containsKey(cardToGain)) {
            card = victoryDecks.get(cardToGain).buyCard();
        } else {
            throw new RuntimeException("Unknown gained card name: " + cardToGain);
        }

        player.hand.add(card);
    }

    private boolean cardNamesDoNotContainCardIgnoreCase(
            ArrayList<String> cardNames, 
            String cardToCheck
    ) {
        for (String card : cardNames) {
            if (card.equalsIgnoreCase(cardToCheck)) {
                return false;
            }
        }

        return true;
    }

    public int discardAnyNumberOfCards(Player player) {
        int numDiscardedCards = 0;

        int discardSelection = gui.getDiscardOption();
        while (discardSelection == 0) {
            if (player.hand.size() <= 1) {
                gui.showErrorPopup("You have no more cards to discard");
                break;
            }
            try {
                discardAnyCard(player);
                numDiscardedCards++;
            } catch (RuntimeException e) {
                gui.showErrorPopup(e.getMessage());
                break;
            }
            discardSelection = gui.getDiscardOption();
        }

        return numDiscardedCards;
    }

    public void gainCards(int numCardsDiscarded, Player player) {
        for (int i = 0; i < numCardsDiscarded; i++) {
            gainAnyCard(player, Utilities.MAX_CARD_COST);
        }
    }
}
