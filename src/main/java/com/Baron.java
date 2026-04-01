package com;
public class Baron extends KingdomCard {
    private Board board;
    public Baron(Board board) {
        super(Gui.getString("baron"), "baron", 4, 0, 0, Gui.getString("tip.baron"));
        this.board = board;
    }
    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.buy++;
        boolean discarded = false;
        if (currentPlayer.hand.stream().anyMatch(c -> c.name.equals(board.bundle.getString("estate")))) {
            if (board.gui.getBaronChoice()) {
                currentPlayer.discardCard(board.bundle.getString("estate"));
                currentPlayer.coins += 4;
                discarded = true;
            }
        }
        if (!discarded) {
            board.transferCardFromDeckToPlayer(board.bundle.getString("estate"), currentPlayer);
        }
    }
}