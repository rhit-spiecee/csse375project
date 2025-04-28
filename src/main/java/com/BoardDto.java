package com;

import java.util.ArrayList;
import java.util.Map;

public class BoardDto {
    public Map<String, BoardDeck> kingdomDecks;
    public Map<String, BoardDeck> treasureDecks;
    public Map<String, BoardDeck> victoryDecks;
    public int currentPlayerNumber;
    public ArrayList<Card> currentPlayerHand;
    public int currentPlayerCoins;
    public int currentPlayerActions;
    public int currentPlayerBuys;

    public void populate(Map<String, BoardDeck> kingdomDecks,
                    Map<String, BoardDeck> treasureDecks,
                    Map<String, BoardDeck> victoryDecks,
                    int currentPlayerNumber,
                    ArrayList<Card> currentPlayerHand,
                    int currentPlayerCoins,
                    int currentPlayerActions,
                    int currentPlayerBuys) {
        this.kingdomDecks = kingdomDecks;
        this.treasureDecks = treasureDecks;
        this.victoryDecks = victoryDecks;
        this.currentPlayerNumber = currentPlayerNumber;
        this.currentPlayerHand = currentPlayerHand;
        this.currentPlayerCoins = currentPlayerCoins;
        this.currentPlayerActions = currentPlayerActions;
        this.currentPlayerBuys = currentPlayerBuys;
    }
}
