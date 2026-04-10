package com;

import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.Test;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GuiTest extends AssertJSwingJUnitTestCase {
    private Gui gui;

    @Override
    protected void onSetUp() {
        // We start Gui in a separate thread because new Gui() blocks on JOptionPane in its constructor
        SwingUtilities.invokeLater(() -> {
            gui = new Gui();
        });
    }

    @Test
    public void testLanguageSelectionDialog() throws InterruptedException {
        // AssertJ Swing will look for the JOptionPane that pops up on startup
        JOptionPaneFixture dialog = JOptionPaneFinder.findOptionPane().using(robot());
        dialog.requireVisible();

        // The input dialog has a combobox for language selection
        dialog.comboBox().selectItem("English");
        dialog.okButton().click();

        // Give it a moment to render the main frame
        Thread.sleep(500);

        // Verify the JFrame appears and gets the correct title based on the English bundle
        FrameFixture frame = WindowFinder.findFrame(JFrame.class).using(robot());
        frame.requireVisible();
        frame.requireTitle("Dominion");
        
        // Clean up the frame so it doesn't linger
        frame.cleanUp();
    }
}
