package S1G3;

public class Militia extends KingdomCard {
    private final Board board;

    public Militia(Board board) {
        super("militia", 4 , 0);
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.coins += 2;
        board.forceMilitiaDiscard();
    }
}
