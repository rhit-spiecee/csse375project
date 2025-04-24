package S1G3;

public class Mine extends KingdomCard {
    Board board;

    public Mine(Board board) {
        super("mine", 5, 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        String trashedCard = board.trashCard(currentPlayer);
        board.gainCard(currentPlayer, trashedCard);
    }
}
