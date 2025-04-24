package S1G3;

import java.util.*;

public class Board {
    List<Player> players;
    Map<String, BoardDeck> kingdomDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> treasureDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> victoryDecks = new LinkedHashMap<>();

    boolean gameOver = false;
    int numPlayers;
    int currentPlayer;
    GUI gui;

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
        int copperDeckSize = 60 - (numPlayers * 7);
        int cursedDeckSize = (numPlayers - 1) * 10;
        int victoryCardDeckSize = (numPlayers == 2) ? 8 : 12;
        int kingdomDeckSize = 10;
        int silverDeckSize = 40;
        int goldDeckSize = 30;

        // Kingdom Cards
        String[] kingdomCards = { "cellar", "market", "militia", "mine", "moat", "remodel",
                "smithy", "village", "workshop", "woodcutter" };

        kingdomDecks.put("cellar", new BoardDeck(new Cellar(), kingdomDeckSize));
        kingdomDecks.put("market", new BoardDeck(new Market(), kingdomDeckSize));
        kingdomDecks.put("militia", new BoardDeck(new Militia(), kingdomDeckSize));
        kingdomDecks.put("mine", new BoardDeck(new Mine(this), kingdomDeckSize));
        kingdomDecks.put("moat", new BoardDeck(new Moat(), kingdomDeckSize));
        kingdomDecks.put("remodel", new BoardDeck(new Remodel(this), kingdomDeckSize));
        kingdomDecks.put("smithy", new BoardDeck(new Smithy(), kingdomDeckSize));
        kingdomDecks.put("village", new BoardDeck(new Village(), kingdomDeckSize));
        kingdomDecks.put("workshop", new BoardDeck(new Workshop(), kingdomDeckSize));
        kingdomDecks.put("woodcutter", new BoardDeck(new Woodcutter(), kingdomDeckSize));


        // Treasure Cards
        treasureDecks.put("copper", new BoardDeck(new TreasureCard("copper", 0, Card.CardType.TREASURE, 1), copperDeckSize));
        treasureDecks.put("silver", new BoardDeck(new TreasureCard("silver", 3, Card.CardType.TREASURE, 2), silverDeckSize));
        treasureDecks.put("gold", new BoardDeck(new TreasureCard("gold", 6, Card.CardType.TREASURE, 3), goldDeckSize));

        // Victory Cards
        victoryDecks.put("estate", new BoardDeck(new VictoryCard("estate", 2, Card.CardType.VICTORY, 1), victoryCardDeckSize));
        victoryDecks.put("duchy", new BoardDeck(new VictoryCard("duchy", 5, Card.CardType.VICTORY, 3), victoryCardDeckSize));
        victoryDecks.put("province", new BoardDeck(new VictoryCard("province", 8, Card.CardType.VICTORY, 6), victoryCardDeckSize));
        victoryDecks.put("cursed", new BoardDeck(new VictoryCard("cursed", 0, Card.CardType.VICTORY, -1), cursedDeckSize));
    }



    public static Board fromGUI(GUI gui) {
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
        if(victoryDecks.get("province").size() <= 0) {
            gameOver = true;
        }
    }

    void processTurn() {
        actionPhase();
        buyPhase();
    }

    private void actionPhase() {
        int actionSelection = gui.getActionSelection(getDTO());
        while (actionSelection == 0 ) {
            try {
                processActionMove();
            } catch (RuntimeException e) {
                gui.showErrorPopup(e.getMessage());
                break;
            }
            actionSelection = gui.getActionSelection(getDTO());
        }
    }

    private BoardDTO getDTO() {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.populate(kingdomDecks, treasureDecks, victoryDecks, getCurrentPlayerNumber(), getCurrentPlayerHand(), getCurrentPlayerCoins(), getCurrentPlayerActions(), getCurrentPlayerBuys());
        return boardDTO;
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

    private KingdomCard getActionCardToPlay() {
        List<KingdomCard> actionCards = players.get(currentPlayer).getActionCards();
        String cardToPlay = "";
        while (!actionCardsContainsName(actionCards, cardToPlay)) {
            cardToPlay = gui.getActionCardToPlay().toLowerCase();
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

        int buySelection = gui.showBuyOption(getDTO());
        while (buySelection == 0) {
            if (players.get(currentPlayer).getBuys() <= 0) {
                gui.showErrorPopup("Player " + (currentPlayer + 1) + " has no buys available");
                break;
            }
            
            String cardToBuy = gui.getBuySelection();
            if (cardToBuy == null) {
                continue;
            }

            try {
                processBuyPhaseSelection(cardToBuy.toLowerCase(), availableDecks);
            } catch (RuntimeException e) {
                gui.showErrorPopup(e.getMessage());
            }
            buySelection = gui.showBuyOption(getDTO());

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
            if (!deck.isEmpty()) {
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
                throw new RuntimeException("Player " + (currentPlayer + 1) + " does not have enough coins for " + buySelection + " card.");
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

    private int getKingdomCardCost(String name) {
        return switch (name) {
            case "cellar", "moat" -> 2;
            case "market", "mine" -> 5;
            case "militia", "remodel", "smithy" -> 4;
            case "village", "workshop", "woodcutter" -> 3;
            default -> 0;
        };
    }

    public BoardDeck getDeck(String name) {
        if (kingdomDecks.containsKey(name)) return kingdomDecks.get(name);
        if (treasureDecks.containsKey(name)) return treasureDecks.get(name);
        if (victoryDecks.containsKey(name)) return victoryDecks.get(name);
        return null;
    }

    public int getCurrentPlayerBuys() {
        return players.get(currentPlayer).getBuys();
    }

    public int getCurrentPlayerActions() {
        return players.get(currentPlayer).getActions();
    }

    public Card trashAnyCard(Player player) {
        String popupMessage = "Enter name of a card you want to trash";
        ArrayList<String> cardNames = player.getCardsInHandNamesExceptRemodel();
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

    public void gainAnyCard(Player player, Card trashedCard) {
        ArrayList<String> cardNames = new ArrayList<>();

        cardNames.addAll(extractCardsBelowCostOf(trashedCard.cost + 2, kingdomDecks));
        cardNames.addAll(extractCardsBelowCostOf(trashedCard.cost + 2, treasureDecks));
        cardNames.addAll(extractCardsBelowCostOf(trashedCard.cost + 2, victoryDecks));

        String popupMessage = "Enter name of a card you want to gain";

        gainCard(popupMessage, cardNames, player);
    }

    private Collection<String> extractCardsBelowCostOf(int maxCost, Map<String, BoardDeck> decks) {
        ArrayList<String> cardNames = new ArrayList<>();

        for (String cardName : decks.keySet()) {
            BoardDeck deck = decks.get(cardName);
            if (!deck.isEmpty() && deck.card.cost <= maxCost) {
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
        } else if (trashedCard.name.equalsIgnoreCase("silver") || trashedCard.name.equalsIgnoreCase("gold")) {
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

    private boolean cardNamesDoNotContainCardIgnoreCase(ArrayList<String> cardNames, String cardToCheck) {
        for (String card : cardNames) {
            if (card.equalsIgnoreCase(cardToCheck)) {
                return false;
            }
        }

        return true;
    }
}
