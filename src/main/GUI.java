import javax.swing.*;
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
        sb.append("Copper (Cost: 0, Value: 1): ").append(board.copperDeck.size()).append("\n");
        sb.append("Silver (Cost: 3, Value: 2): ").append(board.silverDeck.size()).append("\n");
        sb.append("Gold (Cost: 6, Value: 3): ").append(board.goldDeck.size()).append("\n\n");

        sb.append("Victory Decks:\n");
        sb.append("Estate (Cost: 2, Value: 1): ").append(board.estateDeck.size()).append("\n");
        sb.append("Duchy (Cost: 5, Value: 3): ").append(board.duchyDeck.size()).append("\n");
        sb.append("Province (Cost: 8, Value: 6): ").append(board.provinceDeck.size()).append("\n");
        sb.append("Curse (Cost: 0, Value: -1): ").append(board.cursedDeck.size()).append("\n\n");

        sb.append("Kingdom Decks:\n");
        sb.append("Cellar (Cost: 2): ").append(board.cellarDeck.size()).append("\n");
        sb.append("Market (Cost: 5): ").append(board.marketDeck.size()).append("\n");
        sb.append("Militia (Cost: 4): ").append(board.militiaDeck.size()).append("\n");
        sb.append("Mine (Cost: 5): ").append(board.mineDeck.size()).append("\n");
        sb.append("Moat (Cost: 2): ").append(board.moatDeck.size()).append("\n");
        sb.append("Remodel (Cost: 4): ").append(board.remodelDeck.size()).append("\n");
        sb.append("Smithy (Cost: 4): ").append(board.smithyDeck.size()).append("\n");
        sb.append("Village (Cost: 3): ").append(board.villageDeck.size()).append("\n");
        sb.append("Workshop (Cost: 3): ").append(board.workshopDeck.size()).append("\n");
        sb.append("Woodcutter (Cost: 3): ").append(board.woodcutterDeck.size()).append("\n\n");

        sb.append("Current player: ").append(board.getCurrentPlayerNumber() + 1).append("\n");
        
        sb.append("Hand: ")
                .append(board
                        .getCurrentPlayerHand()
                        .stream()
                        .map(Card::getName)
                        .collect(Collectors.toList()))
                .append("\n");

        return sb.toString();
    }
}
