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
    public static final String CHOOSE_LANGUAGE_MESSAGE = "Pick a language (Wählen Sie eine Sprache aus): ";
    JFrame frame;
    ResourceBundle bundle;
    private static ResourceBundle staticBundle;
    private List<PlayerScoreEntry> currentScores = new ArrayList<>();
    private BoardDto lastBoardDto;

    public Gui() {
        setupLanguage();
        setupFrame();
    }

    private void setupLanguage() {
        String selected = (String) JOptionPane.showInputDialog(
                null,
                CHOOSE_LANGUAGE_MESSAGE,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                Language.displayNames(),
                Language.displayNames()[0]);

        Language language = Language.fromDisplayName(selected);
        bundle = ResourceBundle.getBundle(language.bundleName);
        staticBundle = bundle;
        JOptionPane.setDefaultLocale(language.locale);
    }

    public static String getString(String key) {
        if (staticBundle == null) {
            staticBundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);
        }
        return staticBundle.getString(key);
    }

    public void updateView(BoardDto boardDto) {
        this.lastBoardDto = boardDto;
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
                        boardDto.currentPlayerBuys));
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
            handPanel.add(new JLabel(getImageFromCard(card)));
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
            ImageIcon imageIcon = getImageFromCard(deck.getValue().getCard());
            JLabel imageLabel = new JLabel(imageIcon);
            deckPanel.add(deckLabel);
            deckPanel.add(imageLabel);
            supplyPanel.add(deckPanel);
        }
    }

    private ImageIcon getImageFromCard(Card card) {
        String fileName = "src/main/resources/cards/"
                + card.imageId
                + bundle.getString("image.suffix")
                + ".jpg";
        ImageIcon imageIcon = new ImageIcon(fileName);
        return new ImageIcon(
                imageIcon
                        .getImage()
                        .getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_DEFAULT));
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

        String[] options = { two, three, four };
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
        String[] options = {
                bundle.getString("action"),
                bundle.getString("next.phase"),
                bundle.getString("help")
        };
        while (true) {
            int chooseToAction = JOptionPane.showOptionDialog(
                    null,
                    MessageFormat.format(
                            bundle.getString("action.selection.message"),
                            playerNumber + 1),
                    bundle.getString("action.selection.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (chooseToAction == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
            if (chooseToAction == 2) { // Help
                showHelpDialog();
                continue;
            }
            return chooseToAction;
        }
    }

    public int showBuyOption(int playerNumber) {
        String[] options = {
                bundle.getString("buy"),
                bundle.getString("end.turn"),
                bundle.getString("help")
        };
        while (true) {
            int chooseToBuy = JOptionPane.showOptionDialog(
                    null,
                    MessageFormat.format(bundle.getString("buy.selection.message"), playerNumber + 1),
                    bundle.getString("buy.selection.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (chooseToBuy == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
            if (chooseToBuy == 2) { // Help
                showHelpDialog();
                continue;
            }
            return chooseToBuy;
        }
    }

    public String getBuySelection(List<String> availableCardsUnderPlayerCoins) {
        return showSelectionDialogWithHelp(
                bundle.getString("buy.phase.title"),
                bundle.getString("choose.card.to.buy"),
                availableCardsUnderPlayerCoins.toArray(new String[0]));
    }

    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                bundle.getString("error"),
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean getIfPlayerWantsToBlock(int currentPlayer) {
        String[] options = {
                bundle.getString("yes"),
                bundle.getString("no"),
                bundle.getString("help")
        };
        while (true) {
            int chooseToBlock = JOptionPane.showOptionDialog(
                    null,
                    MessageFormat.format(bundle.getString("player.block.message"), currentPlayer + 1),
                    bundle.getString("player.block.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (chooseToBlock == 2) { // Help
                showHelpDialog();
                continue;
            }
            return chooseToBlock == JOptionPane.YES_OPTION;
        }
    }

    public String getCardToDiscard(ArrayList<Card> hand, int playerNumber) {
        String[] options = hand.stream().map((card) -> card.name).toArray(String[]::new);
        return showSelectionDialogWithHelp(
                bundle.getString("get.discard.title"),
                MessageFormat.format(bundle.getString("get.discard.message"), playerNumber + 1),
                options);
    }

    public String getCardToTrash(ArrayList<Card> hand, int playerNumber) {
        String[] options = hand.stream().map((card) -> card.name).toArray(String[]::new);
        return showSelectionDialogWithHelp(
                bundle.getString("get.trash.title"),
                MessageFormat.format(bundle.getString("get.trash.message"), playerNumber + 1),
                options);
    }

    public String getActionCardToPlay(String[] availableCardInHand) {
        return showSelectionDialogWithHelp(
                bundle.getString("get.action.title"),
                bundle.getString("get.action.message"),
                availableCardInHand);
    }

    public String getCardFromAvailableSelection(String baseMessage, ArrayList<String> cardNames) {
        return showSelectionDialogWithHelp(
                bundle.getString("card.selection.title"),
                baseMessage,
                cardNames.toArray(new String[0]));
    }

    public int getDiscardOption() {
        String[] options = {
                bundle.getString("yes"),
                bundle.getString("no"),
                bundle.getString("help")
        };
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    bundle.getString("discard.option.message"),
                    bundle.getString("buy.phase.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (result == 2) { // Help
                showHelpDialog();
                continue;
            }
            return result;
        }
    }

    public void displayGameOverScreen(
            List<PlayerScoreEntry> scoredPlayers,
            boolean haveThreeEmptySupplyPiles) {
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
                        winner.score));

        int rank = 1;
        for (PlayerScoreEntry entry : scoredPlayers) {
            finalMessage.append(
                    MessageFormat.format(
                            bundle.getString("player.score.entry"),
                            rank,
                            entry.index,
                            entry.score));
            rank++;
        }

        JOptionPane.showMessageDialog(
                null,
                finalMessage,
                bundle.getString("game.over.no.new.line"),
                JOptionPane.PLAIN_MESSAGE);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void updateScore(List<PlayerScoreEntry> scoredPlayers) {
        this.currentScores = scoredPlayers;
    }

    private void showHelpDialog() {
        if (lastBoardDto == null) {
            return;
        }

        java.util.Set<Card> allCards = new java.util.HashSet<>();
        lastBoardDto.kingdomDecks.values().forEach(deck -> allCards.add(deck.getCard()));
        lastBoardDto.treasureDecks.values().forEach(deck -> allCards.add(deck.getCard()));
        lastBoardDto.victoryDecks.values().forEach(deck -> allCards.add(deck.getCard()));
        allCards.addAll(lastBoardDto.currentPlayerHand);

        String[] cardNames = allCards.stream()
                .map(card -> card.name)
                .distinct()
                .sorted()
                .toArray(String[]::new);

        Object selection = JOptionPane.showInputDialog(
                null,
                bundle.getString("choose.card.for.tip"),
                bundle.getString("card.tip.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                cardNames,
                cardNames[0]);

        if (selection != null) {
            String selectedCardName = (String) selection;
            Card selectedCard = allCards.stream()
                    .filter(card -> card.name.equals(selectedCardName))
                    .findFirst()
                    .orElse(null);

            if (selectedCard != null) {
                JOptionPane.showMessageDialog(
                        null,
                        selectedCard.getGameTip(),
                        selectedCardName,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private String showSelectionDialogWithHelp(String title, String message, String[] options) {
        if (options.length == 0) {
            return "";
        }

        String[] buttonOptions = {
                bundle.getString("yes"), // reusing "Yes" for "OK" or just localized OK if available
                bundle.getString("no"), // reusing "No" for "Cancel"
                bundle.getString("help")
        };

        while (true) {
            JComboBox<String> comboBox = new JComboBox<>(options);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel(message), BorderLayout.NORTH);
            panel.add(comboBox, BorderLayout.CENTER);

            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    title,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    buttonOptions,
                    buttonOptions[0]);

            if (result == 2) { // Help
                showHelpDialog();
                continue;
            }
            if (result == JOptionPane.YES_OPTION) {
                return (String) comboBox.getSelectedItem();
            }
            return "";
        }
    }

    public int getLurkerOption() {
        String[] options = {
                bundle.getString("lurker.trash.supply"),
                bundle.getString("lurker.gain.trash")
        };
        int choice = JOptionPane.showOptionDialog(
                null,
                bundle.getString("lurker.choice.message"),
                bundle.getString("lurker.title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return choice;
    }

    public int[] getPawnOptions() {
        String[] options = {
                bundle.getString("pawn.card"),
                bundle.getString("pawn.action"),
                bundle.getString("pawn.buy"),
                bundle.getString("pawn.coin")
        };

        int[] selections = new int[2];
        for (int i = 0; i < 2; i++) {
            String message = (i == 0) ? bundle.getString("pawn.first.choice") : bundle.getString("pawn.second.choice");

            String[] currentOptions;
            if (i == 1) {
                final int firstIdx = selections[0];
                currentOptions = java.util.Arrays.stream(options)
                        .filter(opt -> !opt.equals(options[firstIdx]))
                        .toArray(String[]::new);
            } else {
                currentOptions = options;
            }

            String choice = (String) JOptionPane.showInputDialog(
                    null,
                    message,
                    bundle.getString("pawn.title"),
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    currentOptions,
                    currentOptions[0]);

            for (int j = 0; j < options.length; j++) {
                if (options[j].equals(choice)) {
                    selections[i] = j;
                    break;
                }
            }
        }
        return selections;
    }

    public String getCardToPass(ArrayList<Card> hand, int playerNumber) {
        String[] options = hand.stream().map((card) -> card.name).toArray(String[]::new);
        return showSelectionDialogWithHelp(
                bundle.getString("masquerade.pass.title"),
                MessageFormat.format(bundle.getString("masquerade.pass.message"), playerNumber + 1),
                options);
    }

    public boolean getIfPlayerWantsToTrash(int playerNumber) {
        String[] options = {
                bundle.getString("yes"),
                bundle.getString("no"),
                bundle.getString("help")
        };
        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    MessageFormat.format(bundle.getString("masquerade.trash.query"), playerNumber + 1),
                    bundle.getString("masquerade.trash.title"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (choice == 2) {
                showHelpDialog();
                continue;
            }
            return choice == JOptionPane.YES_OPTION;
        }
    }
}
