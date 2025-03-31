import javax.swing.*;
import java.util.Map;
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

    public int getPlayerMove(Board board) {
        String[] options = {"Buy", "Action", "End Turn"};
        return JOptionPane.showOptionDialog(null, getBoardDisplay(board), "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }
    
    private String getBoardDisplay(Board board) {
        StringBuilder sb = new StringBuilder();

        sb.append("Treasure Decks:\n");
        appendDeckDisplayWithValue(sb, board.treasureDecks);

        sb.append("\nVictory Decks:\n");
        appendDeckDisplayWithValue(sb, board.victoryDecks);

        sb.append("\nKingdom Decks:\n");
        appendDeckDisplayWithoutValue(sb, board.kingdomDecks);

        sb.append("\nCurrent Player: ").append(board.getCurrentPlayerNumber() + 1).append("\n");
        sb.append("Hand: ")
                .append(board.getCurrentPlayerHand()
                        .stream()
                        .map((card)->capitalize(card.name))
                        .collect(Collectors.joining(", ")))
                .append("\n");
        sb.append("Coins: ").append(board.getCurrentPlayerCoins()).append("\n");
        sb.append("Action Abilities: ").append(board.getCurrentPlayerActions()).append("\n");
        sb.append("Buy Abilities: ").append(board.getCurrentPlayerBuys()).append("\n");

        return sb.toString();
    }

    private void appendDeckDisplayWithValue(StringBuilder sb, Map<String, BoardDeck> deckMap) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();

            sb.append(String.format("%s (Cost: %d, Value: %d): %d\n",
                        capitalize(entry.getKey()), card.cost, card.value, entry.getValue().size()));
        }
    }

    private void appendDeckDisplayWithoutValue(StringBuilder sb, Map<String, BoardDeck> deckMap) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();

                sb.append(String.format("%s (Cost: %d): %d\n",
                        capitalize(entry.getKey()), card.cost, entry.getValue().size()));

        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
