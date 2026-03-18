package com;

import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.*;

public class Gui {
    public static final int IMAGE_WIDTH = 150;
    public static final int IMAGE_HEIGHT = 240;
    public static final String[] AVAILABLE_LANGUAGES = new String[] {"English", "Deutsch"};
    public static final String CHOOSE_LANGUAGE_MESSAGE = 
            "Pick a language (Wählen Sie eine Sprache aus): ";
    public static final String ENGLISH_BUNDLE = "language";
    public static final String GERMAN_BUNDLE = "language_de";
    public static final Locale ENGLISH_LOCALE = Locale.ENGLISH;
    public static final Locale GERMAN_LOCALE = Locale.GERMAN;

    JFrame frame;
    ResourceBundle bundle;
    private static ResourceBundle staticBundle;
    private List<PlayerScoreEntry> currentScores = new ArrayList<>();

    public Gui() {
        setupLanguage();
        setupFrame();
    }

    private void setupLanguage() {
        String[] options = AVAILABLE_LANGUAGES;
        String selectionObject = (String) JOptionPane.showInputDialog(
                null,
                CHOOSE_LANGUAGE_MESSAGE,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        bundle = ResourceBundle.getBundle(getBundleName(selectionObject));
        staticBundle = bundle;
        JOptionPane.setDefaultLocale(getLocale(selectionObject));
    }

    public static String getString(String key) {
        if (staticBundle == null) {
            staticBundle = ResourceBundle.getBundle(ENGLISH_BUNDLE);
        }
        return staticBundle.getString(key);
    }

    public static String getBundleName(String languageSelection) {
        return switch (languageSelection) {
            case "English" -> ENGLISH_BUNDLE;
            case "Deutsch" -> GERMAN_BUNDLE;
            default -> ENGLISH_BUNDLE;
        };
    }

    public static Locale getLocale(String languageSelection) {
        return switch (languageSelection) {
            case "English" -> ENGLISH_LOCALE;
            case "Deutsch" -> GERMAN_LOCALE;
            default -> ENGLISH_LOCALE;
        };
    }

    public void updateView(BoardDto boardDto) {
        frame.getContentPane().removeAll();

        addBoardDecks(boardDto);
        addHandAndInfo(boardDto);

        frame.revalidate();
        frame.repaint();
    }

    private void addHandAndInfo(BoardDto boardDto) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel infoLabel = new JLabel(
                MessageFormat.format(
                        bundle.getString("current.player.0.coins.1.actions.2.buys.3"),
                        boardDto.currentPlayerNumber + 1,
                        boardDto.currentPlayerCoins,
                        boardDto.currentPlayerActions,
                        boardDto.currentPlayerBuys
                )
        );
        bottomPanel.add(infoLabel, BorderLayout.NORTH);

        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (!currentScores.isEmpty()) {
            String[] columnNames = { bundle.getString("player"), bundle.getString("score") };
            Object[][] data = new Object[currentScores.size()][2];
            for (int i = 0; i < currentScores.size(); i++) {
                data[i][0] = currentScores.get(i).index;
                data[i][1] = currentScores.get(i).score;
            }
            JTable scoreTable = new JTable(data, columnNames);
            scoreTable.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(scoreTable);
            scrollPane.setPreferredSize(new Dimension(200, scoreTable.getRowHeight() * currentScores.size() + 24));
            scorePanel.add(scrollPane);
        }
        bottomPanel.add(scorePanel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.CENTER);

        JPanel handPanel = new JPanel(new FlowLayout());
        handPanel.add(new JLabel(bundle.getString("hand")));
        for (Card card : boardDto.currentPlayerHand) {
            handPanel.add(new JLabel(getImageFromCardName(card.name)));
        }
        frame.add(handPanel, BorderLayout.SOUTH);
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
            JLabel deckLabel = new JLabel(
                    MessageFormat.format(bundle.getString("cards.left.0"), deck.getValue().size()));
            ImageIcon imageIcon = getImageFromCardName(deck.getKey());
            JLabel imageLabel = new JLabel(imageIcon);
            deckPanel.add(deckLabel);
            deckPanel.add(imageLabel);
            supplyPanel.add(deckPanel);
        }
    }

    private ImageIcon getImageFromCardName(String cardName) {
        String fileName = "src/main/resources/cards/" 
                + cardName 
                + bundle.getString("image.suffix")
                + ".jpg";
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
        String two = MessageFormat.format("{0,number}", 2);
        String three = MessageFormat.format("{0,number}", 3);
        String four = MessageFormat.format("{0,number}", 4);

        String[] options = {two, three, four};
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

        if (selectionObject != null) {
            return selectionObject.toString();
        }
        return "";

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
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                MessageFormat.format(bundle.getString("get.discard.message"), playerNumber + 1),
                bundle.getString("get.discard.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectionObject != null) {
            return selectionObject.toString();
        }

        return "";
    }

    public String getCardToTrash(ArrayList<Card> hand, int playerNumber) {
        String[] options = hand.stream().map((card) -> card.name).toArray(String[]::new);
        Object selectionObject = JOptionPane.showInputDialog(
                null,
                MessageFormat.format(bundle.getString("get.trash.message"), playerNumber + 1),
                bundle.getString("get.trash.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectionObject != null) {
            return selectionObject.toString();
        }

        return "";
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

        if (selectionObject == null) {
            return "";
        }
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

    public void displayGameOverScreen(
            List<PlayerScoreEntry> scoredPlayers, 
            boolean haveThreeEmptySupplyPiles
    ) {
        String key;
        if (haveThreeEmptySupplyPiles) {
            key = "you.have.3.empty.supply.piles.game.over";
        } else {
            key = "you.have.an.empty.province.deck.game.over";
        }
        StringBuilder finalMessage = new StringBuilder(bundle.getString(key));
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

    public void updateScore(List<PlayerScoreEntry> scoredPlayers) {
        this.currentScores = scoredPlayers;
    }
}
