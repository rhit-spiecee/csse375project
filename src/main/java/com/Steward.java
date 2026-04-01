package com;

public class Steward extends KingdomCard {
    private Board board;

    public Steward(Board board) {
        super(Gui.getString("steward"), "steward", 3, 0, 0, Gui.getString("tip.steward"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        int choice = board.gui.getStewardOption();

        if (choice == 0) {
            currentPlayer.drawOneCard();
            currentPlayer.drawOneCard();
        } else if (choice == 1) {
            currentPlayer.coins += 2;
        } else if (choice == 2) {
            for (int i = 0; i < 2; i++) {
                if (!currentPlayer.hand.isEmpty()) {
                    String cardToTrash = board.gui.getCardToTrash(new java.util.ArrayList<>(currentPlayer.hand), board.players.indexOf(currentPlayer));
                    if (cardToTrash != null && !cardToTrash.isEmpty()) {
                        board.trashPile.add(currentPlayer.trashCard(cardToTrash));
                    }
                }
            }
        }
    }
}