
public class Board {
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
    int numPlayers;
    public Board(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4) {
            throw new RuntimeException("Number of players must be between 2 and 4");
        }
        this.numPlayers = numPlayers;
        this.cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 10);
        this.marketDeck = new BoardDeck(new Card("market", 5, Card.CardType.KINGDOM, 0), 10);
        this.militiaDeck = new BoardDeck(new Card("militia", 4, Card.CardType.KINGDOM, 0), 10);
        this.mineDeck = new BoardDeck(new Card("mine", 5, Card.CardType.KINGDOM, 0), 10);
        this.moatDeck = new BoardDeck(new Card("moat", 2, Card.CardType.KINGDOM, 0), 10);
        this.remodelDeck = new BoardDeck(new Card("remodel", 4, Card.CardType.KINGDOM, 0), 10);
        this.smithyDeck = new BoardDeck(new Card("smithy", 4, Card.CardType.KINGDOM, 0), 10);
        this.villageDeck = new BoardDeck(new Card("village", 3, Card.CardType.KINGDOM, 0), 10);
        this.workshopDeck = new BoardDeck(new Card("workshop", 3, Card.CardType.KINGDOM, 0), 10);
        this.woodcutterDeck = new BoardDeck(new Card("woodcutter", 3, Card.CardType.KINGDOM, 0), 10);


    }

}
