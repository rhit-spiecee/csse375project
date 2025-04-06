import javax.swing.*;
import java.util.List;

public class GUI {
    public int getNumPlayers() {
        int numPlayers = -1;
        while (numPlayers == -1) {
            String input = JOptionPane.showInputDialog("Enter the number of players:");
            try {
                numPlayers = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,"Enter a valid number of players.");
            }
        }
        return numPlayers;
    }

    public int getActionSelection(String boardDisplayMessage) {
        String[] options = {"Action", "Next Phase"};
        return JOptionPane.showOptionDialog(
                null,
                boardDisplayMessage,
                "Action Phase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    public int getBuySelection(String boardDisplayMessage, List<String> availableDecks) {
        availableDecks.add("End Turn");
        return JOptionPane.showOptionDialog(
                null,
                boardDisplayMessage,
                "Buy Phase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                availableDecks.toArray(),
                availableDecks.getLast()
        );
    }


    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
