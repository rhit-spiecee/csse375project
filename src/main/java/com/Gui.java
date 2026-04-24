package com;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Gui {
    // Premium theme colors
    private static final Color BG_DARK = new Color(20, 20, 25);
    private static final Color BG_ACCENT = new Color(35, 35, 45);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT_WHITE = new Color(230, 230, 235);
    private static final Color ACCENT_BLUE = new Color(70, 130, 180);

    public static final int IMAGE_WIDTH = 130;
    public static final int IMAGE_HEIGHT = 200;
    public static final String CHOOSE_LANGUAGE_MESSAGE = "Pick a language / Sprache wählen: ";

    private JFrame frame;
    private ResourceBundle bundle;
    private static ResourceBundle staticBundle;
    private List<PlayerScoreEntry> currentScores = new ArrayList<>();
    private BoardDto lastBoardDto;

    // UI Components
    private JPanel supplyArea;
    private JPanel handArea;
    private JLabel statsLabel;
    private JTable scoreTable;
    private JButton actionBtn;
    private JButton buyBtn;
    private JButton nextBtn;
    private JTextArea logArea;
    
    // Input synchronization
    private final BlockingQueue<Integer> phaseQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> selectionQueue = new LinkedBlockingQueue<>();
    private String pendingCardSelection = null;

    public Gui() {
        setupLanguage();
        setupFrame();
    }

    private void setupLanguage() {
        String selected = (String) JOptionPane.showInputDialog(
                null, CHOOSE_LANGUAGE_MESSAGE, "", 
                JOptionPane.QUESTION_MESSAGE, null,
                Language.displayNames(), Language.displayNames()[0]);

        if (selected == null) System.exit(0);

        Language language = Language.fromDisplayName(selected);
        bundle = ResourceBundle.getBundle(language.bundleName, language.locale);
        staticBundle = bundle;
        JOptionPane.setDefaultLocale(language.locale);
    }

    public static String getString(String key) {
        if (staticBundle == null) {
            staticBundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName, Language.ENGLISH.locale);
        }
        return staticBundle.getString(key);
    }

    private void setupFrame() {
        frame = new JFrame(bundle.getString("dominion")); 
        frame.setSize(1600, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG_DARK);
        frame.setLayout(new BorderLayout(5, 5));

        // Sidebar (West) - Stats & Scores
        JPanel sidebar = createSidebar();
        frame.add(sidebar, BorderLayout.WEST);

        // Center Area - Supply & Hand
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setOpaque(false);
        
        supplyArea = new JPanel(new GridLayout(2, 9, 15, 15));
        supplyArea.setOpaque(false);
        supplyArea.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JScrollPane supplyScroll = new JScrollPane(supplyArea);
        supplyScroll.setOpaque(false);
        supplyScroll.getViewport().setOpaque(false);
        supplyScroll.setBorder(null);
        centerPanel.add(supplyScroll, BorderLayout.CENTER);

        // Player Area (South)
        JPanel playerArea = new JPanel(new BorderLayout());
        playerArea.setBackground(BG_ACCENT);
        playerArea.setPreferredSize(new Dimension(0, 280));
        playerArea.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, GOLD));
        
        JLabel handTitle = new JLabel(bundle.getString("hand").toUpperCase());
        handTitle.setForeground(GOLD);
        handTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        handTitle.setHorizontalAlignment(SwingConstants.CENTER);
        handTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
        playerArea.add(handTitle, BorderLayout.NORTH);

        handArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        handArea.setOpaque(false);
        playerArea.add(handArea, BorderLayout.CENTER);
        
        centerPanel.add(playerArea, BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Console/Action Panel (East)
        JPanel eastPanel = createEastPanel();
        frame.add(eastPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBackground(BG_ACCENT);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, GOLD));

        JLabel title = new JLabel("PLAYER STATUS");
        title.setForeground(GOLD);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(30, 0, 20, 0));
        panel.add(title);

        statsLabel = new JLabel("<html><div style='text-align: center;'>Initializing game...</div></html>");
        statsLabel.setForeground(TEXT_WHITE);
        statsLabel.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 18));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statsLabel);

        panel.add(Box.createVerticalGlue());

        JLabel scoreTitle = new JLabel("SCOREBOARD");
        scoreTitle.setForeground(GOLD);
        scoreTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(scoreTitle);

        scoreTable = new JTable();
        scoreTable.setRowHeight(30);
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scoreTable.setBackground(BG_DARK);
        scoreTable.setForeground(TEXT_WHITE);
        scoreTable.setSelectionBackground(GOLD);
        scoreTable.setSelectionForeground(BG_DARK);
        scoreTable.getTableHeader().setBackground(BG_DARK);
        scoreTable.getTableHeader().setForeground(GOLD);
        scoreTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setPreferredSize(new Dimension(240, 250));
        scrollPane.setMaximumSize(new Dimension(240, 250));
        scrollPane.setBorder(new LineBorder(GOLD, 1));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(30));

        return panel;
    }

    private JPanel createEastPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBackground(BG_ACCENT);
        panel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, GOLD));

        // Action Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(30, 20, 20, 20));

        actionBtn = createStyledButton(bundle.getString("action").toUpperCase(), GOLD);
        actionBtn.addActionListener(e -> phaseQueue.offer(0));
        btnPanel.add(actionBtn);
        btnPanel.add(Box.createVerticalStrut(15));

        buyBtn = createStyledButton(bundle.getString("buy").toUpperCase(), GOLD);
        buyBtn.addActionListener(e -> phaseQueue.offer(0));
        btnPanel.add(buyBtn);
        btnPanel.add(Box.createVerticalStrut(15));

        nextBtn = createStyledButton(bundle.getString("next.phase").toUpperCase(), ACCENT_BLUE);
        nextBtn.addActionListener(e -> phaseQueue.offer(1));
        btnPanel.add(nextBtn);
        btnPanel.add(Box.createVerticalStrut(15));

        JButton helpBtn = createStyledButton(bundle.getString("help").toUpperCase(), Color.LIGHT_GRAY);
        helpBtn.addActionListener(e -> showHelpDialog());
        btnPanel.add(helpBtn);

        panel.add(btnPanel, BorderLayout.NORTH);

        // Game Log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setOpaque(false);
        logPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel logTitle = new JLabel("GAME LOG");
        logTitle.setForeground(GOLD);
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logPanel.add(logTitle, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(BG_DARK);
        logArea.setForeground(TEXT_WHITE);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new LineBorder(BG_DARK, 5));
        logPanel.add(logScroll, BorderLayout.CENTER);

        panel.add(logPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(BG_DARK);
        btn.setForeground(color);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBorder(new LineBorder(color, 2));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if(btn.isEnabled()){ btn.setBackground(color); btn.setForeground(BG_DARK); } }
            public void mouseExited(MouseEvent e) { if(btn.isEnabled()){ btn.setBackground(BG_DARK); btn.setForeground(color); } }
        });
        
        return btn;
    }

    public void updateView(BoardDto boardDto) {
        this.lastBoardDto = boardDto;
        
        SwingUtilities.invokeLater(() -> {
            statsLabel.setText(String.format(
                "<html><div style='text-align: center; line-height: 200%%;'>" +
                "<span style='font-size: 24pt; color: #D4AF37;'>Player %d</span><br/><br/>" +
                "Actions: <b style='color: white;'>%d</b><br/>" +
                "Buys: <b style='color: white;'>%d</b><br/>" +
                "Coins: <b style='color: #FFD700;'>$%d</b>" +
                "</div></html>",
                boardDto.currentPlayerNumber + 1,
                boardDto.currentPlayerActions,
                boardDto.currentPlayerBuys,
                boardDto.currentPlayerCoins
            ));

            updateSupply(boardDto);
            updateHand(boardDto);
            updateScoreTable();

            frame.revalidate();
            frame.repaint();
        });
    }

    private void updateSupply(BoardDto boardDto) {
        supplyArea.removeAll();
        addDecksToSupply(boardDto.treasureDecks);
        addDecksToSupply(boardDto.victoryDecks);
        addDecksToSupply(boardDto.kingdomDecks);
    }

    private void addDecksToSupply(Map<String, BoardDeck> decks) {
        for (Map.Entry<String, BoardDeck> entry : decks.entrySet()) {
            BoardDeck deck = entry.getValue();
            Card card = deck.getCard();
            
            JPanel cardPile = new JPanel(new BorderLayout());
            cardPile.setOpaque(false);

            JLabel countLabel = new JLabel(String.valueOf(deck.size()));
            countLabel.setHorizontalAlignment(SwingConstants.CENTER);
            countLabel.setForeground(GOLD);
            countLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            
            JLabel imageLabel = new JLabel(getImageFromCard(card));
            imageLabel.setToolTipText("<html><body style='width: 200px;'>" + card.getGameTip() + "</body></html>");
            
            imageLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    processCardSelection(entry.getKey());
                }
                public void mouseEntered(MouseEvent e) { imageLabel.setBorder(new LineBorder(GOLD, 3)); }
                public void mouseExited(MouseEvent e) { imageLabel.setBorder(null); }
            });

            cardPile.add(imageLabel, BorderLayout.CENTER);
            cardPile.add(countLabel, BorderLayout.SOUTH);
            supplyArea.add(cardPile);
        }
    }

    private void updateHand(BoardDto boardDto) {
        handArea.removeAll();
        for (Card card : boardDto.currentPlayerHand) {
            JLabel cardLabel = new JLabel(getImageFromCard(card));
            cardLabel.setToolTipText("<html><body style='width: 200px;'>" + card.getGameTip() + "</body></html>");
            
            cardLabel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    cardLabel.setBorder(new LineBorder(ACCENT_BLUE, 4));
                    cardLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) { cardLabel.setBorder(null); }
                public void mouseClicked(MouseEvent e) {
                    processCardSelection(card.name);
                }
            });
            handArea.add(cardLabel);
        }
    }

    private void processCardSelection(String name) {
        if (pendingCardSelection == null) {
            pendingCardSelection = name;
            phaseQueue.offer(0); 
            selectionQueue.offer(name);
        } else {
            selectionQueue.offer(name);
        }
    }

    private void updateScoreTable() {
        if (!currentScores.isEmpty()) {
            DefaultTableModel model = new DefaultTableModel(new String[]{bundle.getString("player"), bundle.getString("score")}, 0);
            for (PlayerScoreEntry entry : currentScores) {
                model.addRow(new Object[]{"Player " + entry.index, entry.score});
            }
            scoreTable.setModel(model);
        }
    }

    private final Map<String, ImageIcon> imageCache = new HashMap<>();

    private ImageIcon getImageFromCard(Card card) {
        String key = card.imageId + bundle.getString("image.suffix");
        return imageCache.computeIfAbsent(key, k -> {
            String fileName = "src/main/resources/cards/" + k + ".jpg";
            ImageIcon raw = new ImageIcon(fileName);
            return new ImageIcon(raw.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH));
        });
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("> " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    // Input Methods
    public int getNumPlayers() {
        String[] options = { "2", "3", "4" };
        Object selection = JOptionPane.showInputDialog(null, bundle.getString("choose.number.of.players"), 
            "Players", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return (selection == null) ? 2 : Integer.parseInt((String) selection);
    }

    public int getActionSelection(int playerNumber) {
        SwingUtilities.invokeLater(() -> {
            actionBtn.setEnabled(true);
            buyBtn.setEnabled(false);
            nextBtn.setText(bundle.getString("next.phase").toUpperCase());
        });
        pendingCardSelection = null; 
        try { return phaseQueue.take(); } catch (InterruptedException e) { return 1; }
    }

    public int showBuyOption(int playerNumber) {
        SwingUtilities.invokeLater(() -> {
            actionBtn.setEnabled(false);
            buyBtn.setEnabled(true);
            nextBtn.setText(bundle.getString("end.turn").toUpperCase());
        });
        pendingCardSelection = null; 
        try { return phaseQueue.take(); } catch (InterruptedException e) { return 1; }
    }

    public String getBuySelection(List<String> availableCards) {
        if (pendingCardSelection != null) {
            String selection = pendingCardSelection;
            pendingCardSelection = null;
            return selection;
        }
        try { return selectionQueue.take(); } catch (InterruptedException e) { return ""; }
    }

    public String getActionCardToPlay(String[] availableCards) {
        if (pendingCardSelection != null) {
            String selection = pendingCardSelection;
            pendingCardSelection = null;
            return selection;
        }
        try { return selectionQueue.take(); } catch (InterruptedException e) { return ""; }
    }

    // Modal Dialogs for specific card effects
    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(frame, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean getIfPlayerWantsToBlock(int currentPlayer) {
        return JOptionPane.showConfirmDialog(frame, 
            MessageFormat.format(bundle.getString("player.block.message"), currentPlayer + 1),
            bundle.getString("player.block.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public String getCardToDiscard(ArrayList<Card> hand, int playerNumber) {
         return showCardSelectionDialog(bundle.getString("get.discard.title"), 
            MessageFormat.format(bundle.getString("get.discard.message"), playerNumber + 1), hand);
    }

    public String getCardToTrash(ArrayList<Card> hand, int playerNumber) {
        return showCardSelectionDialog(bundle.getString("get.trash.title"), 
            MessageFormat.format(bundle.getString("get.trash.message"), playerNumber + 1), hand);
    }

    private String showCardSelectionDialog(String title, String message, ArrayList<Card> hand) {
        return (String) JOptionPane.showInputDialog(frame, message, title, 
            JOptionPane.QUESTION_MESSAGE, null, hand.stream().map(c -> c.name).toArray(), null);
    }

    public String getCardFromAvailableSelection(String message, ArrayList<String> cardNames) {
        return (String) JOptionPane.showInputDialog(frame, message, bundle.getString("card.selection.title"), 
            JOptionPane.QUESTION_MESSAGE, null, cardNames.toArray(), cardNames.isEmpty() ? null : cardNames.get(0));
    }

    public int getDiscardOption() {
        return JOptionPane.showConfirmDialog(frame, bundle.getString("discard.option.message"), 
            "Discard Phase", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ? 0 : 1;
    }

    public void displayGameOverScreen(List<PlayerScoreEntry> scoredPlayers, boolean haveThreeEmptySupplyPiles) {
        StringBuilder msg = new StringBuilder("<html><body style='width: 350px; padding: 15px; background-color: #23232D; color: white;'>");
        msg.append("<h1 style='color: #D4AF37; text-align: center;'>VICTORY IS YOURS!</h1>");
        msg.append("<p style='text-align: center; border-bottom: 2px solid #D4AF37; padding-bottom: 10px;'>")
           .append(bundle.getString(haveThreeEmptySupplyPiles ? "you.have.3.empty.supply.piles.game.over" : "you.have.an.empty.province.deck.game.over"))
           .append("</p><br/>");
        
        msg.append("<table style='width: 100%; border-collapse: collapse;'>");
        msg.append("<tr style='background-color: #D4AF37; color: black;'><th style='padding: 5px;'>Rank</th><th>Player</th><th>Score</th></tr>");
        int rank = 1;
        for (PlayerScoreEntry entry : scoredPlayers) {
            String rowStyle = rank == 1 ? "background-color: rgba(212,175,55,0.2);" : "";
            msg.append(String.format("<tr style='%s'><td style='padding: 8px; text-align: center;'><b>#%d</b></td><td style='text-align: center;'>Player %d</td><td style='text-align: center;'>%d pts</td></tr>", 
                rowStyle, rank++, entry.index, entry.score));
        }
        msg.append("</table><br/><p style='text-align: center; font-style: italic;'>Thanks for playing Dominion Evolution!</p></body></html>");

        JOptionPane.showMessageDialog(frame, msg, "Final Results", JOptionPane.PLAIN_MESSAGE);
    }

    public ResourceBundle getBundle() { return bundle; }
    public void updateScore(List<PlayerScoreEntry> scoredPlayers) { this.currentScores = scoredPlayers; }

    private void showHelpDialog() {
        if (lastBoardDto == null) return;
        java.util.Set<Card> allCards = new java.util.HashSet<>();
        lastBoardDto.kingdomDecks.values().forEach(deck -> allCards.add(deck.getCard()));
        lastBoardDto.treasureDecks.values().forEach(deck -> allCards.add(deck.getCard()));
        lastBoardDto.victoryDecks.values().forEach(deck -> allCards.add(deck.getCard()));
        allCards.addAll(lastBoardDto.currentPlayerHand);

        String[] cardNames = allCards.stream().map(c -> c.name).distinct().sorted().toArray(String[]::new);
        String selection = (String) JOptionPane.showInputDialog(frame, bundle.getString("choose.card.for.tip"), 
            "Game Wisdom", JOptionPane.QUESTION_MESSAGE, null, cardNames, cardNames[0]);

        if (selection != null) {
            allCards.stream().filter(c -> c.name.equals(selection)).findFirst()
                .ifPresent(c -> JOptionPane.showMessageDialog(frame, "<html><body style='width: 250px;'>" + c.getGameTip() + "</body></html>", 
                    selection.toUpperCase(), JOptionPane.INFORMATION_MESSAGE));
        }
    }

    public int getLurkerOption() { return getCardFromAvailableSelection("Lurker: Choose Effect", new ArrayList<>(Arrays.asList("Trash Kingdom from Supply", "Gain Kingdom from Trash"))).contains("Trash") ? 0 : 1; }
    public int[] getPawnOptions() { return new int[]{0, 1}; } 
    public String getCardToPass(ArrayList<Card> hand, int playerNumber) { return getCardToDiscard(hand, playerNumber); }
    public boolean getIfPlayerWantsToTrash(int playerNumber) { return JOptionPane.showConfirmDialog(frame, "Trash a card?", "Masquerade", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
    public int getStewardOption() { return 0; }
    public String getWishingWellGuess(List<String> allCards) { return getCardFromAvailableSelection("Guess top card:", new ArrayList<>(allCards)); }
    public boolean getBaronChoice() { return true; }
    public boolean getMillDiscardChoice() { return true; }
    public boolean getMiningVillageTrashChoice() { return true; }
    public int getSecretPassagePosition(int deckSize) { return 0; }
    public java.util.List<Integer> getCourtierOptions(int numChoices) { return Arrays.asList(0, 1); }
    public boolean getNoblesChoice() { return true; }
    public boolean getMinionChoice() { return true; }
    public boolean getTorturerChoice(int playerNumber) { return true; }
}
