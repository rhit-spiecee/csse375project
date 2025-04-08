import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class GUI {
    public static final int DEFAULT_NUM_PLAYERS = -1;
    public int getNumPlayers() {
        int numPlayers = DEFAULT_NUM_PLAYERS;
        while (numPlayers == DEFAULT_NUM_PLAYERS) {
            String input = JOptionPane.showInputDialog("Enter the number of players:");
            if (input == null) {
                System.exit(0);
            }

            try {
                numPlayers = Integer.parseInt(input);
                if (numPlayers < 2 || numPlayers > 4) {
                    this.showErrorPopup("Enter a valid number from 2 to 4");
                    numPlayers = DEFAULT_NUM_PLAYERS;
                }
            } catch (NumberFormatException e) {
                this.showErrorPopup("Enter a valid number from 2 to 4");
            }
        }
        return numPlayers;
    }

    public int getActionSelection(String boardDisplayMessage) {
        String[] options = {"Action", "Next Phase"};
        int chooseToAction = JOptionPane.showOptionDialog(
                null,
                boardDisplayMessage,
                "Action Phase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (chooseToAction == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
        return chooseToAction;
    }

    public int showBuyOption(String boardDisplayMessage) {
        String[] options = {"Buy", "End Turn"};
        int chooseToBuy = JOptionPane.showOptionDialog(
                null,
                boardDisplayMessage,
                "Buy Phase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (chooseToBuy == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
        return chooseToBuy;
    }

    public String getBuySelection() {
        return JOptionPane.showInputDialog("Enter name of card to buy:");
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
