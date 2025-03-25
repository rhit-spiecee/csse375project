
public class Board {
    BoardDeck cellarDeck;
    int numPlayers;
    public Board(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4) {
            throw new RuntimeException("Number of players must be between 2 and 4");
        }
        this.numPlayers = numPlayers;
        this.cellarDeck = new BoardDeck(new Card("cellar", 2, Card.CardType.KINGDOM, 0), 10);
    }

}
