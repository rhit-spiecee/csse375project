package com;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class Gui {
    public static final int DEFAULT_NUM_PLAYERS = -1;
    
    public int getNumPlayers() {
        String[] options = {"2", "3", "4"};
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                "Choose Number of Players:",
                "Number of Players",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        return Integer.parseInt((String) selectionObject);
    }

    public int getActionSelection(BoardDto boardDto) {
        String boardDisplayMessage = getBoardDisplay(boardDto);
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

    public int showBuyOption(BoardDto boardDto) {
        String boardDisplayMessage = getBoardDisplay(boardDto);
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

    public String getBuySelection(List<String> availableCards) {
        String[] options = new String[availableCards.size()];
        availableCards.toArray(options);
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                "Choose Card to Buy:",
                "Buy Phase",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        return (String) selectionObject;
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

            sb.append(
                    String.format("%s (Cost: %d, Value: %d): %d\n", 
                            Utilities.capitalize(entry.getKey()), 
                            card.cost, 
                            card.value, 
                            entry.getValue().size()
                    )
            );
        }
    }

    String getBoardDisplay(BoardDto boardDto) {
        StringBuilder sb = new StringBuilder();

        sb.append("Treasure Decks:\n");
        appendDeckDisplayWithValue(sb, boardDto.treasureDecks);

        sb.append("\nVictory Decks:\n");
        appendDeckDisplayWithValue(sb, boardDto.victoryDecks);

        sb.append("\nKingdom Decks:\n");
        appendDeckDisplayWithoutValue(sb, boardDto.kingdomDecks);

        sb.append("\nCurrent Player: ").append(boardDto.currentPlayerNumber + 1).append("\n");
        sb.append("Hand: ")
                .append(boardDto.currentPlayerHand
                        .stream()
                        .map((card) -> Utilities.capitalize(card.name))
                        .collect(Collectors.joining(", ")))
                .append("\n");
        sb.append("Coins: ").append(boardDto.currentPlayerCoins).append("\n");
        sb.append("Action Abilities: ").append(boardDto.currentPlayerActions).append("\n");
        sb.append("Buy Abilities: ").append(boardDto.currentPlayerBuys).append("\n");

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
        String[] options = hand.stream().map((card) -> card.name).toArray(String[]::new);
        //...and passing `frame` instead of `null` as first parameter
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                "Choose",
                "Menu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        return selectionObject.toString();
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
