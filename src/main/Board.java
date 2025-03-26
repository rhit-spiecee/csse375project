import java.util.*;

public class Board {
    List<Player> players;
    Map<String, BoardDeck> kingdomDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> treasureDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> victoryDecks = new LinkedHashMap<>();

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
        gui.getPlayerMove(this);
    }

    public int getCurrentPlayerNumber() {
        return currentPlayer;
    }

    public ArrayList<Card> getCurrentPlayerHand() {
        return players.get(currentPlayer).hand;
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
