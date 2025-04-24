package S1G3;

public class Remodel extends KingdomCard {
    Board board;

    public Remodel(Board board) {
        super("remodel", 4, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        Card trashedCard = board.trashAnyCard(currentPlayer);
        board.gainAnyCard(currentPlayer, trashedCard.cost + 2);
    }
}
