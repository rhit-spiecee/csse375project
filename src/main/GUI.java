import javax.swing.*;

public class GUI {
    public int getNumPlayers() {
        int numPlayers = -1;
        while (numPlayers == -1) {
            String input = JOptionPane.showInputDialog("Enter the number of players:");
            numPlayers = Integer.parseInt(input);
        }
        return numPlayers;
    }
}
