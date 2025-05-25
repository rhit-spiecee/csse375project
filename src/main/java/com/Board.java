package com;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Board {
    private static final int NUM_EMPTY_PILES_FOR_GAME_OVER = 3;

    List<Player> players;
    Map<String, BoardDeck> kingdomDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> treasureDecks = new LinkedHashMap<>();
    Map<String, BoardDeck> victoryDecks = new LinkedHashMap<>();

    int numPlayers;
    int currentPlayerIndex;
    Gui gui;
    ResourceBundle bundle;

    public Board(int numPlayers, ResourceBundle bundle) {
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
        this.bundle = bundle;

        initializeDecks();
        initializePlayers();
    }

    public Board(int numPlayers) {
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
        this.bundle = ResourceBundle.getBundle(Gui.ENGLISH_BUNDLE);

        initializeDecks();
        initializePlayers();
    }

    public static Board fromGui(Gui gui) {
        int numPlayers = gui.getNumPlayers();
        ResourceBundle bundle = gui.getBundle();
        Board board = new Board(numPlayers, bundle);
        board.gui = gui;

        return board;
    }

    private void initializePlayers() {
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(bundle);
            player.drawHand();
            players.add(player);
        }
    }

    private void initializeDecks() {

        int kingdomDeckSize = 10;
        kingdomDecks.put(
                bundle.getString("cellar"), 
                new BoardDeck(new Cellar(this, bundle.getString("cellar")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("market"), 
                new BoardDeck(new Market(bundle.getString("market")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("militia"), 
                new BoardDeck(new Militia(this, bundle.getString("militia")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("mine"), 
                new BoardDeck(new Mine(this, bundle.getString("mine")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("moat"), 
                new BoardDeck(new Moat(bundle.getString("moat")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("remodel"),
                new BoardDeck(new Remodel(this, bundle.getString("remodel")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("smithy"), 
                new BoardDeck(new Smithy(bundle.getString("smithy")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("village"), 
                new BoardDeck(new Village(bundle.getString("village")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("workshop"), 
                new BoardDeck(new Workshop(this, bundle.getString("workshop")), kingdomDeckSize)
        );
        kingdomDecks.put(
                bundle.getString("woodcutter"), 
                new BoardDeck(new Woodcutter(bundle.getString("woodcutter")), kingdomDeckSize)
        );


        int copperDeckSize = 60 - (numPlayers * 7);
        int silverDeckSize = 40;
        int goldDeckSize = 30;
        treasureDecks.put(
                bundle.getString("copper"),
                new BoardDeck(
                        new TreasureCard(bundle.getString("copper"), 0, 1),
                        copperDeckSize
                )
        );
        treasureDecks.put(
                bundle.getString("silver"),
                new BoardDeck(
                        new TreasureCard(bundle.getString("silver"), 3, 2),
                        silverDeckSize
                )
        );
        treasureDecks.put(
                bundle.getString("gold"),
                new BoardDeck(
                        new TreasureCard(bundle.getString("gold"), 6, 3),
                        goldDeckSize
                )
        );


        int cursedDeckSize = (numPlayers - 1) * 10;
        int victoryCardDeckSize = (numPlayers == 2) ? 8 : 12;
        victoryDecks.put(
                bundle.getString("estate"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("estate"), 2, 1),
                        victoryCardDeckSize
                )
        );
        victoryDecks.put(
                bundle.getString("duchy"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("duchy"), 5, 3),
                        victoryCardDeckSize
                )
        );
        victoryDecks.put(
                bundle.getString("province"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("province"), 8, 6),
                        victoryCardDeckSize
                )
        );
        victoryDecks.put(
                bundle.getString("cursed"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("cursed"), 0, -1),
                        cursedDeckSize
                )
        );
    }

    public void startGame() {
        while (!isGameOver()) {
            gui.updateView(getDto());
            processTurn();
        }

        handleGameOver();
    }

    private void handleGameOver() {
        List<PlayerScoreEntry> scoredPlayers = getSortedPlayerEntries();
        String gameOverKey;
        if (haveThreeEmptySupplyPiles()) {
            gameOverKey = "you.have.3.empty.supply.piles.game.over";
        } else {
            gameOverKey = "you.have.an.empty.province.deck.game.over";
        }
        gui.displayGameOverScreen(scoredPlayers, gameOverKey);
    }

    List<PlayerScoreEntry> getSortedPlayerEntries() {
        List<PlayerScoreEntry> scoredPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            int score = players.get(i).calculateScore();
            scoredPlayers.add(new PlayerScoreEntry(i + 1, players.get(i), score));
        }

        scoredPlayers.sort(Board::getPlayerScoreComparison);
        return scoredPlayers;
    }


    private static int getPlayerScoreComparison(PlayerScoreEntry a, PlayerScoreEntry b) {
        int scoreComparison = Integer.compare(b.score, a.score);
        if (scoreComparison == 0) {

            return Integer.compare(b.index, a.index);

        }
        return scoreComparison;
    }

    public boolean checkProvinceDeckLength() {
        return !victoryDecks.get(bundle.getString("province")).isNotEmpty();
    }

    void processTurn() {
        actionPhase();

        buyPhase();
    }

    void actionPhase() {
        int actionSelection = gui.getActionSelection(currentPlayerIndex);
        while (actionSelection == 0) {
            if (getCurrentPlayerActions() <= 0) {
                gui.showErrorPopup(
                        MessageFormat.format(
                                bundle.getString("player.0.no.actions.available"), 
                                getCurrentPlayerNumber()
                        )
                );
                break;
            }
            if (!getCurrentPlayer().hasActionCardInHand()) {
                gui.showErrorPopup(
                        MessageFormat.format(
                                bundle.getString("player.0.no.action.cards"), 
                                getCurrentPlayerNumber()
                        )
                );
                break;
            }

            String actionCardToPlay = getActionCardToPlay();
            if (!actionCardToPlay.isEmpty()) {
                KingdomCard actionCard = getCardByName(
                        getCurrentPlayerActionCardsInHand(),
                        actionCardToPlay);
                actionCard.useActionCard(getCurrentPlayer());
                gui.updateView(getDto());
            }
            actionSelection = gui.getActionSelection(currentPlayerIndex);
        }
    }

    public BoardDto getDto() {
        BoardDto boardDto = new BoardDto();
        boardDto.populate(
                kingdomDecks,
                treasureDecks,
                victoryDecks,
                getCurrentPlayerIndex(),
                getCurrentPlayerHand(),
                getCurrentPlayerCoins(),
                getCurrentPlayerActions(),
                getCurrentPlayerBuys()
        );
        return boardDto;
    }

    public String[] getAvailableActionCardsInHand() {
        List<KingdomCard> actionCards = getCurrentPlayer().getActionCardsInHand();
        String[] availableActionCards = new String[actionCards.size()];
        for (int i = 0; i < actionCards.size(); i++) {
            availableActionCards[i] = actionCards.get(i).name;
        }
        return availableActionCards;
    }

    String getActionCardToPlay() {
        return gui.getActionCardToPlay(getAvailableActionCardsInHand());
    }

    KingdomCard getCardByName(List<KingdomCard> cards, String name) {
        for (KingdomCard card : cards) {
            if (card.name.equals(name)) {
                return card;
            }
        }
        throw new RuntimeException("Card list is empty or name of card is invalid");
    }

    void buyPhase() {
        int buySelection = gui.showBuyOption(currentPlayerIndex);
        while (buySelection == 0) {
            if (getCurrentPlayerBuys() <= 0) {
                gui.showErrorPopup(
                        MessageFormat.format(
                                bundle.getString("player.0.no.buys.available"), 
                                getCurrentPlayerNumber()
                        )
                );
                break;
            }
            String cardToBuy = gui.getBuySelection(
                    getAllCardsBelowCostOf(getCurrentPlayerCoins()));
            if (!cardToBuy.isEmpty()) {
                processBuyPhaseSelection(cardToBuy.toLowerCase());
            }
            gui.updateView(getDto());
            if (isGameOver()) {
                return;
            }
            buySelection = gui.showBuyOption(currentPlayerIndex);
        }
        endTurn();
    }

    void processBuyPhaseSelection(String buySelection) {
        BoardDeck deckToBuyFrom = getBoardDeckByName(buySelection);
        Card boughtCard = deckToBuyFrom.buyCard();
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.addBoughtCard(boughtCard);
        currentPlayer.buy--;

        int coinsInHand = currentPlayer.getCoinsInHand();
        
        if (coinsInHand >= boughtCard.cost) {
            currentPlayer.removeTreasureCardsOfCost(boughtCard.cost);
        } else {
            currentPlayer.coins -= (boughtCard.cost - coinsInHand);
            currentPlayer.removeTreasureCardsOfCost(coinsInHand);
        }
    }

    private void endTurn() {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.cleanup();
        currentPlayer.drawHand();
        currentPlayerIndex = getCurrentPlayerNumber() % numPlayers;
        gui.updateView(getDto());
    }

    private int getCurrentPlayerNumber() {
        return currentPlayerIndex + 1;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public ArrayList<Card> getCurrentPlayerHand() {
        return getCurrentPlayer().getHand();
    }

    public int getCurrentPlayerCoins() {
        return getCurrentPlayer().getCoins();
    }

    public int getCurrentPlayerBuys() {
        return getCurrentPlayer().getBuys();
    }

    public int getCurrentPlayerActions() {
        return getCurrentPlayer().getActions();
    }

    public List<KingdomCard> getCurrentPlayerActionCardsInHand() {
        return getCurrentPlayer().getActionCardsInHand();
    }

    public void forceMilitiaDiscard() {
        for (int i = 0; i < numPlayers; i++) {
            Player player = players.get(i);
            if (currentPlayerIndex == i) {
                continue;
            }
            if (player.hasMoatCard()) {
                boolean blockMilitia = gui.getIfPlayerWantsToBlock(i);
                if (blockMilitia) {
                    continue;
                }
            }
            while (player.hand.size() > 3) {
                String cardToDiscard = gui.getCardToDiscard(player.hand, i);
                if (cardToDiscard.isEmpty()) {
                    continue;
                }
                player.discardCard(cardToDiscard);
            }
        }
    }

    public BoardDeck getBoardDeckByName(String nameOfDeck) {
        if (kingdomDecks.containsKey(nameOfDeck)) {
            return kingdomDecks.get(nameOfDeck);
        }
        if (treasureDecks.containsKey(nameOfDeck)) {
            return treasureDecks.get(nameOfDeck);
        }
        if (victoryDecks.containsKey(nameOfDeck)) {
            return victoryDecks.get(nameOfDeck);
        }
        throw new RuntimeException("Unknown kingdom deck: " + nameOfDeck);
    }

    private void discardAnyCard(Player player) {
        String cardToDiscard = gui.getCardToDiscard(
                player.getCardsInHandExceptOne(bundle.getString("cellar")),
                players.indexOf(player));
        while (cardToDiscard.isEmpty()) {
            cardToDiscard = gui.getCardToDiscard(
                    player.getCardsInHandExceptOne(bundle.getString("cellar")),
                    players.indexOf(player));
        }

        player.discardCard(cardToDiscard);
    }

    public Card trashAnyCard(Player player) {
        ArrayList<Card> cards = player.getCardsInHandExceptOne(bundle.getString("remodel"));
        return trashCard(cards, player);
    }

    public Card trashTreasureCard(Player player) {
        ArrayList<Card> cards = player.getTreasureCardsInHand();
        return trashCard(cards, player);
    }

    private Card trashCard(ArrayList<Card> cards, Player player) {
        String cardToTrash = gui.getCardToTrash(cards, players.indexOf(player));
        while (cardToTrash.isEmpty()) {
            cardToTrash = gui.getCardToTrash(cards, players.indexOf(player));
        }
        return player.trashCard(cardToTrash);
    }

    public void gainAnyCard(Player player, int maxCost) {
        ArrayList<String> cardNames = getAllCardsBelowCostOf(maxCost);

        String popupMessage = bundle.getString("card.to.discard");

        gainCard(popupMessage, cardNames, player);
    }

    public ArrayList<String> gainTreasureCard(Player player, Card trashedCard) {
        ArrayList<String> cardNames = new ArrayList<>();
        if (trashedCard.name.equalsIgnoreCase(bundle.getString("copper"))) {
            cardNames.add(bundle.getString("copper"));
            cardNames.add(bundle.getString("silver"));
        } else {
            cardNames.add(bundle.getString("copper"));
            cardNames.add(bundle.getString("silver"));
            cardNames.add(bundle.getString("gold"));
        }

        String popupMessage = bundle.getString("gain.treasure.card");
        gainCard(popupMessage, cardNames, player);
        return cardNames;
    }

    private void gainCard(String popupMessage, ArrayList<String> cardNames, Player player) {
        String cardToGain = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        
        transferCardFromDeckToPlayer(cardToGain, player);
    }

    void transferCardFromDeckToPlayer(String cardToGain, Player player) {
        Card card;
        if (kingdomDecks.containsKey(cardToGain)) {
            card = kingdomDecks.get(cardToGain).buyCard();
        } else if (treasureDecks.containsKey(cardToGain)) {
            card = treasureDecks.get(cardToGain).buyCard();
        } else if (victoryDecks.containsKey(cardToGain)) {
            card = victoryDecks.get(cardToGain).buyCard();
        } else {
            throw new RuntimeException("Unknown gained card name: " + cardToGain);
        }

        player.hand.add(card);
    }

    private ArrayList<String> getAllCardsBelowCostOf(int maxCost) {
        ArrayList<String> cardNames = new ArrayList<>();
        cardNames.addAll(getCardsBelowCostOf(maxCost, kingdomDecks));
        cardNames.addAll(getCardsBelowCostOf(maxCost, treasureDecks));
        cardNames.addAll(getCardsBelowCostOf(maxCost, victoryDecks));
        return cardNames;
    }

    public List<String> getCardsBelowCostOf(int maxCost, Map<String, BoardDeck> decks) {
        ArrayList<String> cardNames = new ArrayList<>();

        for (String cardName : decks.keySet()) {
            BoardDeck deck = decks.get(cardName);
            if (deck.isNotEmpty() && deck.card.cost <= maxCost) {
                cardNames.add(cardName);
            }
        }

        return cardNames;
    }

    public int discardAnyNumberOfCards(Player player) {
        int numDiscardedCards = 0;

        int discardSelection = gui.getDiscardOption();
        while (discardSelection == 0) {
            if (player.hand.size() <= 1) {
                gui.showErrorPopup(bundle.getString("no.more.cards"));
                break;
            }

            discardAnyCard(player);
            numDiscardedCards++;

            discardSelection = gui.getDiscardOption();

        }

        return numDiscardedCards;
    }

    List<String> getAvailableDecks(Map<String, BoardDeck> decks) {
        List<String> availableDecks = new ArrayList<>();
        for (Map.Entry<String, BoardDeck> entry : decks.entrySet()) {
            BoardDeck deck = entry.getValue();
            if (deck.isNotEmpty()) {
                availableDecks.add(entry.getKey());
            }
        }
        return availableDecks;
    }

    public boolean haveThreeEmptySupplyPiles() {
        int numEmptyPiles = 0;

        numEmptyPiles += getNumEmptyKingdomDecks();
        numEmptyPiles += getNumEmptyTreasureDecks();
        numEmptyPiles += getNumEmptyVictoryDecks();

        return numEmptyPiles >= NUM_EMPTY_PILES_FOR_GAME_OVER;
    }

    public boolean isGameOver() {
        return haveThreeEmptySupplyPiles() || checkProvinceDeckLength();
    }

    private int getNumEmptyKingdomDecks() {
        int numEmptyDecks = 0;

        for (BoardDeck deck : kingdomDecks.values()) {
            if (!deck.isNotEmpty()) {
                numEmptyDecks++;
            }
        }

        return numEmptyDecks;
    }

    private int getNumEmptyTreasureDecks() {
        int numEmptyDecks = 0;

        for (BoardDeck deck : treasureDecks.values()) {
            if (!deck.isNotEmpty()) {
                numEmptyDecks++;
            }
        }

        return numEmptyDecks;
    }

    private int getNumEmptyVictoryDecks() {
        int numEmptyDecks = 0;

        for (BoardDeck deck : victoryDecks.values()) {
            if (!deck.isNotEmpty()) {
                numEmptyDecks++;
            }
        }

        return numEmptyDecks;
    }
}
