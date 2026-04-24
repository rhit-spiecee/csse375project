package com;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            Gui gui = new Gui();
            
            // Run game logic on a separate background thread
            new Thread(() -> {
                Board board = Board.setupBoardFromGui(gui);
                board.startGame();
            }).start();
        });
    }
}
