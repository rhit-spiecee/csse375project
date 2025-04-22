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
        kingdomDecks.put("mine", new BoardDeck(new Mine(), kingdomDeckSize));
        kingdomDecks.put("moat", new BoardDeck(new Moat(), kingdomDeckSize));
        kingdomDecks.put("remodel", new BoardDeck(new Remodel(), kingdomDeckSize));
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
        }
    }

    void processTurn() {
        actionPhase();
        buyPhase();
    }

    private void actionPhase() {
        int actionSelection = gui.getActionSelection(getDTO());
        try {
            processActionPhaseSelection(actionSelection);
        } catch (RuntimeException e) {
            gui.showErrorPopup(e.getMessage());
            actionPhase();
        }
    }

    private BoardDTO getDTO() {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.populate(kingdomDecks, treasureDecks, victoryDecks, getCurrentPlayerNumber(), getCurrentPlayerHand(), getCurrentPlayerCoins(), getCurrentPlayerActions(), getCurrentPlayerBuys());
        return boardDTO;
    }

    private void processActionPhaseSelection(int actionSelection) {
        if (actionSelection == 0) {
            processActionMove();
        }
    }

    private void processActionMove() {
        if (!players.get(currentPlayer).hasActionCard()) {
            throw new RuntimeException("Player " + (currentPlayer + 1) + " has no action cards");
        }
    }

    private void buyPhase() {
        List<String> availableDecks = getAvailableDecks();
        boolean noBuyDecisionMade = true;

        while (noBuyDecisionMade) {
            int buySelection = gui.showBuyOption(getDTO());

            if (buySelection == 0) {
                String cardToBuy = gui.getBuySelection();
                if (cardToBuy == null) {
                    continue;
                }

                try {
                    processBuyPhaseSelection(cardToBuy.toLowerCase(), availableDecks);
                    noBuyDecisionMade = false;
                } catch (RuntimeException e) {
                    gui.showErrorPopup(e.getMessage());
                }

            } else {
                noBuyDecisionMade = false;
            }
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

}
