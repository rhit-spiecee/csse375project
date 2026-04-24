package com;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ResourceBundle;

import static org.junit.Assert.assertTrue;

public class Quadrant4EnduranceBenchmark {

    @Test(timeout = 10000)
    public void testConcurrentMemoryLeakAndEndurance() {
        // Run 100 fast bots to detect OOM or significant degradation.
        long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Gui mockGui = EasyMock.niceMock(Gui.class);
            EasyMock.expect(mockGui.getNumPlayers()).andReturn(4).anyTimes();
            EasyMock.expect(mockGui.getBundle()).andReturn(ResourceBundle.getBundle(Language.ENGLISH.bundleName)).anyTimes();
            EasyMock.expect(mockGui.getActionSelection(EasyMock.anyInt())).andReturn(1).anyTimes();
            EasyMock.expect(mockGui.showBuyOption(EasyMock.anyInt())).andReturn(1).anyTimes();
            EasyMock.replay(mockGui);

            Board board = Board.setupBoardFromGui(mockGui);
            
            // Just initialize and process a few turns to let GC churn models
            for (int t = 0; t < 5; t++) {
                board.processTurn();
            }
        }
        
        System.gc(); // Suggest GC
        
        long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long endTime = System.currentTimeMillis();

        System.out.println("Endurance Benchmark complete. Elapsed ms: " + (endTime - startTime));
        System.out.println("Memory Diff bytes: " + (endMem - startMem));
        
        assertTrue("Execution should be fast enough (< 5s)", (endTime - startTime) < 5000);
        // Ensure no wild memory leaks beyond basic JVM expansion
        assertTrue("Memory leak bounded", (endMem - startMem) < 50 * 1024 * 1024);
    }
}
