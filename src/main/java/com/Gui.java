package com;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gui {
    public static final int IMAGE_WIDTH = 150;
    public static final int IMAGE_HEIGHT = 240;
    JFrame frame;
    
    public Gui() {
        setupFrame();
    }
    
    public void updateView(BoardDto boardDto) {
        addBoardDecks(boardDto);
        addHandAndInfo(boardDto);

        frame.revalidate();
        frame.repaint();
    }

    private void addHandAndInfo(BoardDto boardDto) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        JLabel infoLabel = new JLabel(
                "Current Player: " + (boardDto.currentPlayerNumber + 1)
                        + ", Coins: " + boardDto.currentPlayerCoins
                        + ", Actions: " + boardDto.currentPlayerActions
                        + ", Buys: " + boardDto.currentPlayerBuys
                        
        );
        bottomPanel.add(infoLabel);

        JPanel handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout());
        handPanel.add(new JLabel("Hand: "));
        for (Card card : boardDto.currentPlayerHand) {
            ImageIcon imageIcon = getImageFromCardName(card.name);
            JLabel cardImageLabel = new JLabel(imageIcon);
            handPanel.add(cardImageLabel);
        }
        bottomPanel.add(handPanel);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addBoardDecks(BoardDto boardDto) {
        JPanel supplyPanel = new JPanel();
        supplyPanel.setLayout(new GridLayout(2, 9));
        addDecksToFrame(boardDto.treasureDecks, supplyPanel);
        addDecksToFrame(boardDto.victoryDecks, supplyPanel);
        addDecksToFrame(boardDto.kingdomDecks, supplyPanel);

        frame.add(supplyPanel, BorderLayout.NORTH);
    }

    private void addDecksToFrame(Map<String, BoardDeck> decks, JPanel supplyPanel) {
        for (Map.Entry<String, BoardDeck> deck : decks.entrySet()) {
            JPanel deckPanel = new JPanel();
            JLabel deckLabel = new JLabel("Cards left: " + deck.getValue().size());
            ImageIcon imageIcon = getImageFromCardName(deck.getKey());
            JLabel imageLabel = new JLabel(imageIcon);
            deckPanel.add(deckLabel);
            deckPanel.add(imageLabel);
            supplyPanel.add(deckPanel);
        }
    }

    private ImageIcon getImageFromCardName(String cardName) {
        String fileName = "src/main/resources/cards/" + cardName + ".jpg";
        ImageIcon imageIcon = new ImageIcon(fileName);
        return new ImageIcon(
                imageIcon
                        .getImage()
                        .getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_DEFAULT)
        );
    }

    private void setupFrame() {
        frame = new JFrame("Dominion");

        frame.setSize(1920, 1000);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        frame.setLayout(new BorderLayout());
    }

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

    public int getActionSelection(int playerNumber) {
        String[] options = {"Action", "Next Phase"};
        int chooseToAction = JOptionPane.showOptionDialog(
                null,
                "Player " + (playerNumber + 1) + ": What would you like to do in the action phase?",
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

    public int showBuyOption(int playerNumber) {
        String[] options = {"Buy", "End Turn"};
        int chooseToBuy = JOptionPane.showOptionDialog(
                null,
                "Player " + (playerNumber + 1) + ": What would you like to do in the buy phase?",
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

    public String getBuySelection(List<String> availableCardsUnderPlayerCoins) {
        String[] options = new String[availableCardsUnderPlayerCoins.size()];
        availableCardsUnderPlayerCoins.toArray(options);
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

    public String getActionCardToPlay(String[] availableCardInHand) {
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                "Choose Action Card to Play:",
                "Action Phase",
                JOptionPane.PLAIN_MESSAGE,
                null,
                availableCardInHand,
                availableCardInHand[0]);
        return (String) selectionObject;
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
