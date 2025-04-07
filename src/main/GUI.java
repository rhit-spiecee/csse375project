import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

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

    public int showBuyOption(String boardDisplayMessage) {
        String[] options = {"Buy", "End Turn"};
        return JOptionPane.showOptionDialog(
                null,
                boardDisplayMessage,
                "Buy Phase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    public String getBuySelection() {
        return JOptionPane.showInputDialog("Enter name of card to buy:").toLowerCase();
    }

    private List<String> formatAvailableDecks(List<String> availableDecks) {
        List<String> formattedDecks = availableDecks.stream().map(deck -> "Buy " + Utilities.capitalize(deck) + "\n").collect(Collectors.toList());
        formattedDecks.add("End Turn");
        return formattedDecks;
    }


    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
