package com;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gui {
    public static final int IMAGE_WIDTH = 150;
    public static final int IMAGE_HEIGHT = 240;
    JFrame frame;
    ResourceBundle bundle;
    
    public Gui() {
        setupLanguage();
        setupFrame();
    }

    private void setupLanguage() {
        // show dropdown with language thing
        String[] options = new String[] {"English", "Deutsch"};
        String selectionObject = (String) JOptionPane.showInputDialog(
                null,
                "Pick a language (Wählen Sie eine Sprache aus): ",
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (selectionObject.equals("Deutsch")) {
            bundle = ResourceBundle.getBundle(Utilities.GERMAN_BUNDLE);
            JOptionPane.setDefaultLocale(Locale.GERMAN);
        } else {
            bundle = ResourceBundle.getBundle(Utilities.ENGLISH_BUNDLE);
            JOptionPane.setDefaultLocale(Locale.ENGLISH);
        }
    }

    public void updateView(BoardDto boardDto) {
        frame.getContentPane().removeAll();
        
        addBoardDecks(boardDto);
        addHandAndInfo(boardDto);

        frame.revalidate();
        frame.repaint();
    }

    private void addHandAndInfo(BoardDto boardDto) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        JLabel infoLabel = new JLabel(
                MessageFormat.format(
                        bundle.getString("current.player.0.coins.1.actions.2.buys.3"), 
                        boardDto.currentPlayerNumber + 1,
                        boardDto.currentPlayerCoins,
                        boardDto.currentPlayerActions,
                        boardDto.currentPlayerBuys
                )
        );
        bottomPanel.add(infoLabel);

        JPanel handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout());
        handPanel.add(new JLabel(bundle.getString("hand")));
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
            JLabel deckLabel = new JLabel(MessageFormat.format(bundle.getString("cards.left.0"), deck.getValue().size()));
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
        frame = new JFrame(bundle.getString("dominion"));

        frame.setSize(1920, 1000);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        frame.setLayout(new BorderLayout());
    }

    public int getNumPlayers() {
        String[] options = {"2", "3", "4"};
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                bundle.getString("choose.number.of.players"),
                bundle.getString("number.of.players"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        return Integer.parseInt((String) selectionObject);
    }

    public int getActionSelection(int playerNumber) {
        String[] options = {bundle.getString("action"), bundle.getString("next.phase")};
        int chooseToAction = JOptionPane.showOptionDialog(
                null,
                MessageFormat.format(
                        bundle.getString("action.selection.message"), 
                        playerNumber + 1
                ),
                bundle.getString("action.selection.title"),
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
        String[] options = {bundle.getString("buy"), bundle.getString("end.turn")};
        int chooseToBuy = JOptionPane.showOptionDialog(
                null,
                MessageFormat.format(bundle.getString("buy.selection.message"), playerNumber + 1),
                bundle.getString("buy.selection.title"),
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
                bundle.getString("choose.card.to.buy"),
                bundle.getString("buy.phase.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        return (String) selectionObject;
    }

    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(
                null, 
                message, 
                bundle.getString("error"), 
                JOptionPane.ERROR_MESSAGE
        );
    }

    public boolean getIfPlayerWantsToBlock(int currentPlayer) {
        String[] options = {bundle.getString("yes"), bundle.getString("no")};
        int chooseToBuy = JOptionPane.showOptionDialog(
                null,
                MessageFormat.format(bundle.getString("player.block.message"), currentPlayer + 1),
                bundle.getString("player.block.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        return chooseToBuy == JOptionPane.YES_OPTION;
    }

    public String getCardToDiscard(ArrayList<Card> hand, int playerNumber) {
        String[] options = hand.stream().map((card) -> card.name).toArray(String[]::new);
        //...and passing `frame` instead of `null` as first parameter
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                MessageFormat.format(bundle.getString("get.discard.message"), playerNumber + 1),
                bundle.getString("get.discard.title"),
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
                bundle.getString("get.action.message"),
                bundle.getString("get.action.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                availableCardInHand,
                availableCardInHand[0]);
        return (String) selectionObject;
    }

    public String getCardFromAvailableSelection(String baseMessage, ArrayList<String> cardNames) {
        String[] options = cardNames.toArray(new String[cardNames.size()]);
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                baseMessage,
                bundle.getString("card.selection.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        return selectionObject.toString();
    }

    public int getDiscardOption() {
        String[] options = {bundle.getString("yes"), bundle.getString("no")};
        return JOptionPane.showOptionDialog(
                null,
                bundle.getString("discard.option.message"),
                bundle.getString("buy.phase.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    public void displayGameOverScreen(List<PlayerScoreEntry> scoredPlayers) {
        StringBuilder finalMessage = new StringBuilder(bundle.getString("game.over"));
        PlayerScoreEntry winner = scoredPlayers.getFirst();
        finalMessage.append(
                MessageFormat.format(
                        bundle.getString("winner.player.0.with.1.points"), 
                        winner.index, 
                        winner.score
                )
        );

        int rank = 1;
        for (PlayerScoreEntry entry : scoredPlayers) {
            finalMessage.append(
                    MessageFormat.format(
                            bundle.getString("player.score.entry"), 
                            rank,
                            entry.index,
                            entry.score
                    )
            );
            rank++;
        }

        JOptionPane.showMessageDialog(
                null, 
                finalMessage,
                bundle.getString("game.over.no.new.line"), 
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public ResourceBundle getBundle() {
        return bundle;
    }
}
