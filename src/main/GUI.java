import javax.swing.*;

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
        return "Buy";
    }
}
