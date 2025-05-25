package com;

public class Main {
    public static void main(String[] args) {
        Gui gui = new Gui();
        Board board = Board.setupBoardFromGUI(gui);
        board.startGame();
    }
}
