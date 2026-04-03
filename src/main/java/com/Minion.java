package com;

import java.util.List;

public class Minion extends KingdomCard {
    private Board board;

    public Minion(Board board) {
        super(Gui.getString("minion"), "minion", 5, 0, 0, Gui.getString("tip.minion"));
        this.board = board;
    }

    @Override
    public void useCardPowers(Player currentPlayer) {
        currentPlayer.action += 1;
        
        boolean choiceCoin = board.gui.getMinionChoice();
        
        if (choiceCoin) {
            currentPlayer.coins += 2;
        } else {
            // Discard hand and draw 4
            discardAndDrawFour(currentPlayer);
            
            // Attack other players
            for (Player p : board.players) {
                if (p != currentPlayer) {
                    if (p.hasMoatCard()) {
                        int index = board.players.indexOf(p);
                        boolean block = board.gui.getIfPlayerWantsToBlock(index);
                        if (block) {
                            continue;
                        }
                    }
                    if (p.hand.size() >= 5) {
                        discardAndDrawFour(p);
                    }
                }
            }
        }
    }
    
    private void discardAndDrawFour(Player p) {
        p.discardPile.addAll(p.hand);
        p.hand.clear();
        for (int i = 0; i < 4; i++) {
            p.drawOneCard();
        }
    }

    @Override
    public List<String> getTypes() {
        List<String> types = super.getTypes();
        types.add("Attack");
        return types;
    }
}
