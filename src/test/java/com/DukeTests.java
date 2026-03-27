package com;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DukeTests {

    @Test
    public void testDukeZeroDuchies() {
        Duke duke = new Duke();
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(new Copper());
        allCards.add(new VictoryCard("estate", 2, 0, 1));
        
        Assert.assertEquals(0, duke.getVictoryPoints(allCards));
    }

    @Test
    public void testDukeMultipleDuchies() {
        Duke duke = new Duke();
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.add(new VictoryCard("duchy", 5, 0, 3));
        allCards.add(new VictoryCard("duchy", 5, 0, 3));
        allCards.add(new Copper());
        allCards.add(new Duke()); // Should ignore itself
        allCards.add(new VictoryCard("duchy", 5, 0, 3));
        
        Assert.assertEquals(3, duke.getVictoryPoints(allCards));
    }
}
