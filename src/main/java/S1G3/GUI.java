package S1G3;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
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

    public int getActionSelection(BoardDTO boardDTO) {
        String boardDisplayMessage = getBoardDisplay(boardDTO);
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

    public int showBuyOption(BoardDTO boardDTO) {
        String boardDisplayMessage = getBoardDisplay(boardDTO);
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

    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    void appendDeckDisplayWithoutValue(StringBuilder sb, Map<String, BoardDeck> deckMap) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();

            sb.append(String.format("%s (Cost: %d): %d\n",
                    Utilities.capitalize(entry.getKey()), card.cost, entry.getValue().size()));

        }
    }

    void appendDeckDisplayWithValue(StringBuilder sb, Map<String, BoardDeck> deckMap) {
        for (Map.Entry<String, BoardDeck> entry : deckMap.entrySet()) {
            Card card = entry.getValue().getCard();

            sb.append(String.format("%s (Cost: %d, Value: %d): %d\n",
                    Utilities.capitalize(entry.getKey()), card.cost, card.value, entry.getValue().size()));
        }
    }

    String getBoardDisplay(BoardDTO boardDTO) {
        StringBuilder sb = new StringBuilder();

        sb.append("Treasure Decks:\n");
        appendDeckDisplayWithValue(sb, boardDTO.treasureDecks);

        sb.append("\nVictory Decks:\n");
        appendDeckDisplayWithValue(sb, boardDTO.victoryDecks);

        sb.append("\nKingdom Decks:\n");
        appendDeckDisplayWithoutValue(sb, boardDTO.kingdomDecks);

        sb.append("\nCurrent Player: ").append(boardDTO.currentPlayerNumber + 1).append("\n");
        sb.append("Hand: ")
                .append(boardDTO.currentPlayerHand
                        .stream()
                        .map((card)-> Utilities.capitalize(card.name))
                        .collect(Collectors.joining(", ")))
                .append("\n");
        sb.append("Coins: ").append(boardDTO.currentPlayerCoins).append("\n");
        sb.append("Action Abilities: ").append(boardDTO.currentPlayerActions).append("\n");
        sb.append("Buy Abilities: ").append(boardDTO.currentPlayerBuys).append("\n");

        return sb.toString();
    }

    public boolean getIfPlayerWantsToBlock(int currentPlayer) {
        String[] options = {"Yes", "No"};
        int chooseToBuy = JOptionPane.showOptionDialog(
                null,
                "Player " + (currentPlayer + 1) + ": Do you want to be immune to this action?",
                "Block with Moat",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        return chooseToBuy == JOptionPane.YES_OPTION;
    }

    public String getCardToDiscard(ArrayList<Card> hand) {
        String[] options = hand.stream().map((card)->card.name).toArray(String[]::new);
        //...and passing `frame` instead of `null` as first parameter
        Object selectionObject = JOptionPane.showInputDialog(null, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String selectionString = selectionObject.toString();
        return selectionString;
    }

    public String getActionCardToPlay() {
        return JOptionPane.showInputDialog("Enter name of card to play:");
    }

    public String getCardFromAvailableSelection(String baseMessage, ArrayList<String> cardNames) {
        StringBuilder popupMessage = new StringBuilder(baseMessage);
        popupMessage.append("(Available: ");
        popupMessage.append(String.join(", ", cardNames));
        popupMessage.append(")");

        return JOptionPane.showInputDialog(popupMessage);
    }

    public int getDiscardOption() {
        String[] options = {"Yes", "No"};
        return JOptionPane.showOptionDialog(
                null,
                "Do you want to discard a card?",
                "Buy Phase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }
}
