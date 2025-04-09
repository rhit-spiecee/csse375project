import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

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
        for (String name : kingdomCards) {
            kingdomDecks.put(name, new BoardDeck(new Card(name, getKingdomCardCost(name), Card.CardType.KINGDOM, 0), kingdomDeckSize));
        }

        // Treasure Cards
        treasureDecks.put("copper", new BoardDeck(new Card("copper", 0, Card.CardType.TREASURE, 1), copperDeckSize));
        treasureDecks.put("silver", new BoardDeck(new Card("silver", 3, Card.CardType.TREASURE, 2), silverDeckSize));
        treasureDecks.put("gold", new BoardDeck(new Card("gold", 6, Card.CardType.TREASURE, 3), goldDeckSize));

        // Victory Cards
        victoryDecks.put("estate", new BoardDeck(new Card("estate", 2, Card.CardType.VICTORY, 1), victoryCardDeckSize));
        victoryDecks.put("duchy", new BoardDeck(new Card("duchy", 5, Card.CardType.VICTORY, 3), victoryCardDeckSize));
        victoryDecks.put("province", new BoardDeck(new Card("province", 8, Card.CardType.VICTORY, 6), victoryCardDeckSize));
        victoryDecks.put("cursed", new BoardDeck(new Card("cursed", 0, Card.CardType.VICTORY, -1), cursedDeckSize));
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
        int actionSelection = gui.getActionSelection(getBoardDisplay());
        try {
            processActionPhaseSelection(actionSelection);
        } catch (RuntimeException e) {
            gui.showErrorPopup(e.getMessage());
            actionPhase();
        }
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
            int buySelection = gui.showBuyOption(getBoardDisplay());

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

    String getBoardDisplay() {
        StringBuilder sb = new StringBuilder();

        sb.append("Treasure Decks:\n");
        appendDeckDisplayWithValue(sb, treasureDecks);

        sb.append("\nVictory Decks:\n");
        appendDeckDisplayWithValue(sb, victoryDecks);

        sb.append("\nKingdom Decks:\n");
        appendDeckDisplayWithoutValue(sb, kingdomDecks);

        sb.append("\nCurrent Player: ").append(getCurrentPlayerNumber() + 1).append("\n");
        sb.append("Hand: ")
                .append(getCurrentPlayerHand()
                        .stream()
                        .map((card)-> Utilities.capitalize(card.name))
                        .collect(Collectors.joining(", ")))
                .append("\n");
        sb.append("Coins: ").append(getCurrentPlayerCoins()).append("\n");
        sb.append("Action Abilities: ").append(getCurrentPlayerActions()).append("\n");
        sb.append("Buy Abilities: ").append(getCurrentPlayerBuys()).append("\n");

        return sb.toString();
    }

    private void appendDeckDisplayWithValue(StringBuilder sb, Map<String, BoardDeck> deckMap) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();

            sb.append(String.format("%s (Cost: %d, Value: %d): %d\n",
                    Utilities.capitalize(entry.getKey()), card.cost, card.value, entry.getValue().size()));
        }
    }

    private void appendDeckDisplayWithoutValue(StringBuilder sb, Map<String, BoardDeck> deckMap) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();

            sb.append(String.format("%s (Cost: %d): %d\n",
                    Utilities.capitalize(entry.getKey()), card.cost, entry.getValue().size()));

        }
    }
}
