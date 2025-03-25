import javax.swing.*;
import java.awt.*;

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
        sb.append("Copper: ").append(board.copperDeck.size()).append("\n");
        sb.append("Silver: ").append(board.silverDeck.size()).append("\n");
        sb.append("Gold: ").append(board.goldDeck.size()).append("\n\n");

        sb.append("Victory Decks:\n");
        sb.append("Estate: ").append(board.estateDeck.size()).append("\n");
        sb.append("Duchy: ").append(board.duchyDeck.size()).append("\n");
        sb.append("Province: ").append(board.provinceDeck.size()).append("\n");
        sb.append("Curse: ").append(board.cursedDeck.size()).append("\n\n");

        sb.append("Kingdom Decks:\n");
        sb.append("Cellar: ").append(board.cellarDeck.size()).append("\n");
        sb.append("Market: ").append(board.marketDeck.size()).append("\n");
        sb.append("Militia: ").append(board.militiaDeck.size()).append("\n");
        sb.append("Mine: ").append(board.mineDeck.size()).append("\n");
        sb.append("Moat: ").append(board.moatDeck.size()).append("\n");
        sb.append("Remodel: ").append(board.remodelDeck.size()).append("\n");
        sb.append("Smithy: ").append(board.smithyDeck.size()).append("\n");
        sb.append("Village: ").append(board.villageDeck.size()).append("\n");
        sb.append("Workshop: ").append(board.workshopDeck.size()).append("\n");
        sb.append("Woodcutter: ").append(board.woodcutterDeck.size()).append("\n\n");
        
        sb.append("Current player: ").append(board.getCurrentPlayer()).append("\n");

        return sb.toString();
    }
}
