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

    public String getPlayerMove(Board board) {
        return JOptionPane.showInputDialog(getBoardDisplay(board));
    }
    
    private String getBoardDisplay(Board board) {
        StringBuilder sb = new StringBuilder();

        sb.append("Treasure Decks:\n");
        appendDeckDisplay(sb, board.treasureDecks, true);

        sb.append("\nVictory Decks:\n");
        appendDeckDisplay(sb, board.victoryDecks, true);

        sb.append("\nKingdom Decks:\n");
        appendDeckDisplay(sb, board.kingdomDecks, false);

        sb.append("\nCurrent player: ").append(board.getCurrentPlayerNumber() + 1).append("\n");
        sb.append("Hand: ")
                .append(board.getCurrentPlayerHand()
                        .stream()
                        .map(Card::getName)
                        .collect(Collectors.joining(", ")))
                .append("\n");
        sb.append("Coins: ").append(board.getCurrentPlayerCoins()).append("\n");
        sb.append("Action Abilities: ").append(board.getCurrentPlayerActions()).append("\n");
        sb.append("Buy Abilities: ").append(board.getCurrentPlayerBuys()).append("\n");

        return sb.toString();
    }

    private void appendDeckDisplay(StringBuilder sb, Map<String, BoardDeck> deckMap, boolean includeValue) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();
            if (includeValue) {
                sb.append(String.format("%s (Cost: %d, Value: %d): %d\n",
                        capitalize(entry.getKey()), card.getCost(), card.getValue(), entry.getValue().size()));
            } else {
                sb.append(String.format("%s (Cost: %d): %d\n",
                        capitalize(entry.getKey()), card.getCost(), entry.getValue().size()));
            }
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
