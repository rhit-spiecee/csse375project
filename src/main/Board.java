
public class Board {
    BoardDeck cursedDeck;
    BoardDeck provinceDeck;
    BoardDeck duchyDeck;
    BoardDeck estateDeck;
    BoardDeck goldDeck;
    BoardDeck silverDeck;
    BoardDeck copperDeck;
    BoardDeck workshopDeck;
    BoardDeck woodcutterDeck;
    BoardDeck villageDeck;
    BoardDeck smithyDeck;
    BoardDeck remodelDeck;
    BoardDeck moatDeck;
    BoardDeck mineDeck;
    BoardDeck militiaDeck;
    BoardDeck marketDeck;
    BoardDeck cellarDeck;
    int kingdomDeckSize = 10;
    int numPlayers;
    
    GUI gui;

    public Board(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4) {
            throw new RuntimeException("Number of players must be between 2 and 4");
        }
        this.numPlayers = numPlayers;
        int copperDeckSize = 60 - (this.numPlayers * 7);
        int cursedDeckSize = (numPlayers - 1) * 10;
        int victoryCardDeckSize = numPlayers == 2 ? 8 : 12;

        //Kingdom Card Deck SETUP
        this.cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.marketDeck = new BoardDeck(new Card("market", 5, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.militiaDeck = new BoardDeck(new Card("militia", 4, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.mineDeck = new BoardDeck(new Card("mine", 5, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.moatDeck = new BoardDeck(new Card("moat", 2, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.remodelDeck = new BoardDeck(new Card("remodel", 4, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.smithyDeck = new BoardDeck(new Card("smithy", 4, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.villageDeck = new BoardDeck(new Card("village", 3, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.workshopDeck = new BoardDeck(new Card("workshop", 3, Card.CardType.KINGDOM, 0), kingdomDeckSize);
        this.woodcutterDeck = new BoardDeck(new Card("woodcutter", 3, Card.CardType.KINGDOM, 0), kingdomDeckSize);

        //Treasure deck setup
        this.copperDeck = new BoardDeck(new Card("copper", 0, Card.CardType.TREASURE, 1), copperDeckSize);
        this.silverDeck = new BoardDeck(new Card("silver", 3, Card.CardType.TREASURE, 2), 40);
        this.goldDeck = new BoardDeck(new Card("gold", 6, Card.CardType.TREASURE, 3), 30);

        //Victory deck setup
        this.estateDeck = new BoardDeck(new Card("estate", 2, Card.CardType.TREASURE, 1), victoryCardDeckSize);
        this.duchyDeck = new BoardDeck(new Card("duchy", 5, Card.CardType.TREASURE, 3), victoryCardDeckSize);
        this.provinceDeck = new BoardDeck(new Card("province", 8, Card.CardType.TREASURE, 6), victoryCardDeckSize);
        this.cursedDeck = new BoardDeck(new Card("cursed", 0, Card.CardType.TREASURE, -1), cursedDeckSize);
    }

    public static Board fromGUI(GUI gui) {
        int numPlayers = gui.getNumPlayers();
        Board board = new Board(numPlayers);
        board.gui = gui;

        return board;
    }
}
