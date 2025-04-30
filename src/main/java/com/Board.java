package com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            processTurn();
            checkProvinceDeckLength();
        }
        gui.showErrorPopup("Game over. Player X won"); //TODO
    }

    public void checkProvinceDeckLength() {
        if (victoryDecks.get("province").size() <= 0) {
            gameOver = true;
        }
    }

    void processTurn() {
        actionPhase();
        buyPhase();
    }

    private void actionPhase() {
        int actionSelection = gui.getActionSelection(getDto());
        while (actionSelection == 0) {
            try {
                processActionMove();
            } catch (RuntimeException e) {
                gui.showErrorPopup(e.getMessage());
                break;
            }
            actionSelection = gui.getActionSelection(getDto());
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
        if (!players.get(currentPlayer).hasActionCard()) {
            throw new RuntimeException("Player " + (currentPlayer + 1) + " has no action cards");
        } else {
            KingdomCard card = getActionCardToPlay();
            System.out.println(card);
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
        String cardToPlay = "";
        while (!actionCardsContainsName(actionCards, cardToPlay)) {
            cardToPlay = gui.getActionCardToPlay(getAvailableActionCardsInHand()).toLowerCase();
            System.out.println(cardToPlay);
        }
        return getCardByName(actionCards, cardToPlay);
    }

    private boolean actionCardsContainsName(List<KingdomCard> actionCards, String cardToPlay) {
        for (KingdomCard actionCard : actionCards) {
            System.out.println(actionCard.name);
            if (cardToPlay.equals(actionCard.name)) {
                return true;
            }
        }
        return false;
    }

    private KingdomCard getCardByName(List<KingdomCard> cards, String name) {
        for (KingdomCard card : cards) {
            if (card.name.equals(name)) {
                System.out.println("found card");
                return card;
            }
        }
        return null;
    }

    private void buyPhase() {
        List<String> availableDecks = getAvailableDecks();

        int buySelection = gui.showBuyOption(getDto());
        while (buySelection == 0) {
            if (players.get(currentPlayer).getBuys() <= 0) {
                gui.showErrorPopup("Player " + (currentPlayer + 1) + " has no buys available");
                break;
            }

            String cardToBuy = gui.getBuySelection(getAvailableDecks());
            if (cardToBuy == null) {
                continue;
            }

            try {
                processBuyPhaseSelection(cardToBuy.toLowerCase(), availableDecks);
            } catch (RuntimeException e) {
                gui.showErrorPopup(e.getMessage());
            }
            buySelection = gui.showBuyOption(getDto());

        }
        endTurn();
    }

    public List<String> getAvailableDecks() {
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

    private void processBuyPhaseSelection(String buySelection, List<String> availableDecks) {
        if (availableDecks.contains(buySelection)) {
            BoardDeck deckToBuyFrom = getBoardDeckFromName(buySelection);
            if (deckToBuyFrom.getCost() <= players.get(currentPlayer).getCoins()) {
                Card boughtCard = deckToBuyFrom.buyCard();
                players.get(currentPlayer).addBoughtCard(boughtCard);
                players.get(currentPlayer).buy--;
                // TODO: we gotta remove all the coins that they player uses
            } else {
                throw new RuntimeException(
                        "Player "
                                + (currentPlayer + 1)
                                + " does not have enough coins for "
                                + buySelection
                                + " card."
                );
            }
        } else {
            throw new RuntimeException("Card: " + buySelection + " is not available.");
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
                boolean blockMilitia = gui.getIfPlayerWantsToBlock(currentPlayer);
                if (blockMilitia) {
                    continue;
                }
            }
            while (player.hand.size() > 3) {
                String cardToDiscard = gui.getCardToDiscard(player.hand);
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
        ArrayList<String> cardNames = new ArrayList<>();

        cardNames.addAll(getCardsBelowCostOf(maxCost, kingdomDecks));
        cardNames.addAll(getCardsBelowCostOf(maxCost, treasureDecks));
        cardNames.addAll(getCardsBelowCostOf(maxCost, victoryDecks));

        String popupMessage = "Enter name of a card you want to gain";

        gainCard(popupMessage, cardNames, player);
    }

    private Collection<String> getCardsBelowCostOf(int maxCost, Map<String, BoardDeck> decks) {
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
            cardNames.add("Copper");
            cardNames.add("Silver");
        } else if (trashedCard.name.equalsIgnoreCase("silver")
                || trashedCard.name.equalsIgnoreCase("gold")) {
            cardNames.add("Copper");
            cardNames.add("Silver");
            cardNames.add("Gold");
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
            throw new RuntimeException("Unknown trashed card name: " + cardToGain);
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
            if (player.hand.isEmpty()) {
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
