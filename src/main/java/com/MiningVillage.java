package com;

public class MiningVillage extends KingdomCard {
    private Board board;

    public MiningVillage(Board board) {
        super(Gui.getString("miningvillage"), "miningvillage", 4, 0, 0, Gui.getString("tip.miningvillage"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.drawOneCard();
        currentPlayer.action += 2;

        if (board.gui.getMiningVillageTrashChoice()) {
            Card trashedCard = currentPlayer.trashCard(this.name);
            if (trashedCard != null) {
                board.trashPile.add(trashedCard);
                currentPlayer.coins += 2;
            }
        }
    }
}
