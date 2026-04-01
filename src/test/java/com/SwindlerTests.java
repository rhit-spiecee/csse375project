package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class SwindlerTests {

    @Test
    public void testSwindlerFullExecution() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player p1 = new Player(bundle);
        Player p2 = new Player(bundle);
        p2.deck.getCards().clear();
        p2.deck.addTop(new Copper());

        mockBoard.numPlayers = 2;
        mockBoard.currentPlayerIndex = 0;
        mockBoard.players = new ArrayList<>(Arrays.asList(p1, p2));
        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();
        mockBoard.kingdomDecks = new java.util.HashMap<>();

        EasyMock.expect(mockGui.getBundle()).andReturn(bundle).anyTimes();
        EasyMock.expect(mockBoard.getCardsInDeckBelowCostOf(EasyMock.anyInt(), EasyMock.anyObject()))
                .andReturn(Arrays.asList(bundle.getString("copper")));
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject()))
                .andReturn(bundle.getString("copper"));
        mockBoard.transferCardFromDeckToPlayer(EasyMock.eq(bundle.getString("copper")), EasyMock.eq(p2));

        EasyMock.replay(mockBoard, mockGui);

        Swindler swindler = new Swindler(mockBoard);
        swindler.useCardPowers(p1);

        assertEquals(1, mockBoard.trashPile.size());
        EasyMock.verify(mockBoard, mockGui);
    }
}
