package com;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Board {
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
        this.bundle = ResourceBundle.getBundle(Utilities.ENGLISH_BUNDLE);

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
                        new TreasureCard(bundle.getString("copper"), 0, Card.CardType.TREASURE, 1),
                        copperDeckSize
                )
        );
        treasureDecks.put(
                bundle.getString("silver"),
                new BoardDeck(
                        new TreasureCard(bundle.getString("silver"), 3, Card.CardType.TREASURE, 2),
                        silverDeckSize
                )
        );
        treasureDecks.put(
                bundle.getString("gold"),
                new BoardDeck(
                        new TreasureCard(bundle.getString("gold"), 6, Card.CardType.TREASURE, 3),
                        goldDeckSize
                )
        );


        int cursedDeckSize = (numPlayers - 1) * 10;
        int victoryCardDeckSize = (numPlayers == 2) ? 8 : 12;
        victoryDecks.put(
                bundle.getString("estate"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("estate"), 2, Card.CardType.VICTORY, 1),
                        victoryCardDeckSize
                )
        );
        victoryDecks.put(
                bundle.getString("duchy"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("duchy"), 5, Card.CardType.VICTORY, 3),
                        victoryCardDeckSize
                )
        );
        victoryDecks.put(
                bundle.getString("province"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("province"), 8, Card.CardType.VICTORY, 6),
                        victoryCardDeckSize
                )
        );
        victoryDecks.put(
                bundle.getString("cursed"),
                new BoardDeck(
                        new VictoryCard(bundle.getString("cursed"), 0, Card.CardType.VICTORY, -1),
                        cursedDeckSize
                )
        );
    }

    public void startGame() { //TODO: Game class
        while (!isGameOver()) {
            gui.updateView(getDto());
            processTurn();
        }

        handleGameOver();
    }

    private void handleGameOver() {
        List<PlayerScoreEntry> scoredPlayers = getSortedPlayerEntries();

        gui.displayGameOverScreen(scoredPlayers);
    }

    List<PlayerScoreEntry> getSortedPlayerEntries() {
        List<PlayerScoreEntry> scoredPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            int score = players.get(i).calculateScore(); // only calculate once
            scoredPlayers.add(new PlayerScoreEntry(i + 1, players.get(i), score));
        }

        scoredPlayers.sort((a, b) -> Integer.compare(b.score, a.score));
        return scoredPlayers;
    }

    public boolean checkProvinceDeckLength() { // TODO: change to game class
        return !victoryDecks.get(bundle.getString("province")).isNotEmpty();
    }

    void processTurn() { // TODO: change to game class
        actionPhase();

        buyPhase();
    }

    void actionPhase() { // TODO: change to game class
        int actionSelection = gui.getActionSelection(currentPlayerIndex);
        while (actionSelection == 0 && !isGameOver()) {
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

            KingdomCard actionCardToPlay = getActionCardToPlay();
            if (actionCardToPlay != null) {
                actionCardToPlay.useActionCard(getCurrentPlayer());
            }
            gui.updateView(getDto());
            if (isGameOver()) {
                return;
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

    KingdomCard getActionCardToPlay() {
        List<KingdomCard> actionCards = getCurrentPlayerActionCardsInHand();
        String cardToPlay = gui.getActionCardToPlay(getAvailableActionCardsInHand()).toLowerCase();
        return getCardByName(actionCards, cardToPlay);
    }

    KingdomCard getCardByName(List<KingdomCard> cards, String name) {
        for (KingdomCard card : cards) {
            if (card.name.equals(name)) {
                return card;
            }
        }
        throw new RuntimeException("Card list is empty or name of card is invalid");
    }

    void buyPhase() { //TODO: add to game class
        int buySelection = gui.showBuyOption(currentPlayerIndex);
        while (buySelection == 0 && !isGameOver()) {
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
            if (cardToBuy != null) {
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

    void processBuyPhaseSelection(String buySelection) { // TODO: game class
        BoardDeck deckToBuyFrom = getBoardDeckByName(buySelection);
        Card boughtCard = deckToBuyFrom.buyCard();
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.addBoughtCard(boughtCard);
        currentPlayer.buy--;

        int coinsInHand = currentPlayer.getCoinsInHand();
        currentPlayer.coins -= (boughtCard.cost - coinsInHand);
        currentPlayer.removeTreasureCardsOfCost(boughtCard.cost);
    }

    private void endTurn() { // TODO: game class
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

    public void forceMilitiaDiscard() { //TODO: this should be moved to militia implementation
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

    public void discardAnyCard(Player player) {
        String popupMessage = bundle.getString("enter.discard.name");
        ArrayList<String> cardNames = player.getCardsInHandNamesExcept("cellar");

        String cardToDiscard = gui.getCardFromAvailableSelection(popupMessage, cardNames);

        player.discardCard(cardToDiscard);
    }

    public Card trashAnyCard(Player player) {
        String popupMessage = bundle.getString("trash.any.card");
        ArrayList<String> cardNames = player.getCardsInHandNamesExcept(bundle.getString("remodel"));
        return trashCard(popupMessage, cardNames, player);
    }

    public Card trashTreasureCard(Player player) {
        String popupMessage = bundle.getString("trash.treasure.card");
        ArrayList<String> cardNames = player.getTreasureCardsInHandNames();
        return trashCard(popupMessage, cardNames, player);
    }

    private Card trashCard(String popupMessage, ArrayList<String> cardNames, Player player) {
        String cardToTrash = gui.getCardFromAvailableSelection(popupMessage, cardNames);
        
        return player.trashCard(cardToTrash);
    }

    public void gainAnyCard(Player player, int maxCost) {
        ArrayList<String> cardNames = getAllCardsBelowCostOf(maxCost);

        String popupMessage = bundle.getString("card.to.discard");

        gainCard(popupMessage, cardNames, player);
    }

    public ArrayList<String> gainTreasureCard(Player player, Card trashedCard) {
        ArrayList<String> cardNames = new ArrayList<>();
        if (trashedCard.name.equalsIgnoreCase("copper")) {
            cardNames.add("copper");
            cardNames.add("silver");
        } else {
            cardNames.add("copper");
            cardNames.add("silver");
            cardNames.add("gold");
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

    public int discardAnyNumberOfCards(Player player) { // TODO: Move to cellar implementation
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


    // Only used in tests
    public List<String> getAllAvailableDecks() {
        ArrayList<String> availableDecks = new ArrayList<>();
        availableDecks.addAll(getAvailableDecks(kingdomDecks));
        availableDecks.addAll(getAvailableDecks(treasureDecks));
        availableDecks.addAll(getAvailableDecks(victoryDecks));
        return availableDecks;
    }

    private List<String> getAvailableDecks(Map<String, BoardDeck> decks) {
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

        return numEmptyPiles >= 3;
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
