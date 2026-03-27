package com;

public class Duke extends VictoryCard {
    public Duke() {
        super("duke", 5, 0, 0);
    }

    @Override
    public int getVictoryPoints(java.util.List<Card> allCards) {
        int count = 0;
        String duchyName = Gui.getString("duchy");
        for (Card card : allCards) {
            if (card.name.equals(duchyName)) {
                count++;
            }
        }
        return count;
    }
}
