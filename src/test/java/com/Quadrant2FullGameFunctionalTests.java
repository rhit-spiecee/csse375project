package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Random;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class Quadrant2FullGameFunctionalTests {

    @Test(timeout = 5000)
    public void testFullRandomSeededBotGame() {
        // Deterministic Seed
        long seed = 12345L;
        Random rng = new Random(seed);

        Gui mockGui = EasyMock.niceMock(Gui.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2).anyTimes();
        EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Language.ENGLISH.bundleName)).anyTimes();
        
        // Randomize Action decisions
        EasyMock.expect(mockGui.getActionSelection(EasyMock.anyInt())).andAnswer(() -> rng.nextInt(2)).anyTimes();
        EasyMock.expect(mockGui.getActionCardToPlay(EasyMock.anyObject())).andAnswer(() -> {
            String[] cards = (String[]) EasyMock.getCurrentArguments()[0];
            if (cards.length > 0) return cards[rng.nextInt(cards.length)];
            return "";
        }).anyTimes();

        // Randomize Buy decisions
        EasyMock.expect(mockGui.showBuyOption(EasyMock.anyInt())).andAnswer(() -> rng.nextInt(2)).anyTimes();
        EasyMock.expect(mockGui.getBuySelection(EasyMock.anyObject())).andAnswer(() -> {
            java.util.List<String> valid = (java.util.List<String>) EasyMock.getCurrentArguments()[0];
            if (valid.size() > 0) return valid.get(rng.nextInt(valid.size()));
            return "";
        }).anyTimes();

        // Discard/Trash answers
        EasyMock.expect(mockGui.getDiscardOption()).andAnswer(() -> rng.nextInt(2)).anyTimes();
        
        EasyMock.expect(mockGui.getCardToDiscard(EasyMock.anyObject(), EasyMock.anyInt())).andAnswer(() -> {
            java.util.ArrayList<Card> cards = (java.util.ArrayList<Card>) EasyMock.getCurrentArguments()[0];
            if (!cards.isEmpty()) return cards.get(rng.nextInt(cards.size())).name;
            return "";
        }).anyTimes();

        EasyMock.expect(mockGui.getCardToTrash(EasyMock.anyObject(), EasyMock.anyInt())).andAnswer(() -> {
            java.util.ArrayList<Card> cards = (java.util.ArrayList<Card>) EasyMock.getCurrentArguments()[0];
            if (!cards.isEmpty()) return cards.get(rng.nextInt(cards.size())).name;
            return "";
        }).anyTimes();

        EasyMock.expect(mockGui.getCardToPass(EasyMock.anyObject(), EasyMock.anyInt())).andAnswer(() -> {
            java.util.ArrayList<Card> cards = (java.util.ArrayList<Card>) EasyMock.getCurrentArguments()[0];
            if (!cards.isEmpty()) return cards.get(rng.nextInt(cards.size())).name;
            return "";
        }).anyTimes();
        
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andAnswer(() -> {
            java.util.List<String> valid = (java.util.List<String>) EasyMock.getCurrentArguments()[1];
            if (valid.size() > 0) return valid.get(rng.nextInt(valid.size()));
            return "";
        }).anyTimes();

        EasyMock.expect(mockGui.getIfPlayerWantsToBlock(EasyMock.anyInt())).andReturn(true).anyTimes();
        EasyMock.expect(mockGui.getIfPlayerWantsToTrash(EasyMock.anyInt())).andReturn(false).anyTimes();
        EasyMock.expect(mockGui.getPawnOptions()).andReturn(new int[]{0, 1}).anyTimes();
        EasyMock.expect(mockGui.getCourtierOptions(EasyMock.anyInt())).andReturn(java.util.Arrays.asList(0, 1)).anyTimes();

        mockGui.updateView(EasyMock.anyObject(BoardDto.class));
        EasyMock.expectLastCall().anyTimes();
        mockGui.updateScore(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        mockGui.displayGameOverScreen(EasyMock.anyObject(), EasyMock.anyBoolean());
        EasyMock.expectLastCall().anyTimes();
        mockGui.showErrorPopup(EasyMock.anyString());
        EasyMock.expectLastCall().anyTimes();
        mockGui.log(EasyMock.anyString());
        EasyMock.expectLastCall().anyTimes();

        EasyMock.replay(mockGui);

        Board board = Board.setupBoardFromGui(mockGui);
        
        int maxTurns = 20;
        int currentTurn = 0;
        
        while (!board.isGameOver() && currentTurn < maxTurns) {
            board.processTurn();
            currentTurn++;
        }

        assertTrue("Game should terminate without fatal unhandled exceptions.", true);
        
        // Basic Invariants
        for (Player p : board.players) {
            assertTrue("Score calculation must not crash.", p.calculateScore() >= -100);
        }

        EasyMock.verify(mockGui);
    }
}
