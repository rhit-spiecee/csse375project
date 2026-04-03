package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class TradingPostTests {

    @Test
    public void testTradingPostTrashTwoGainSilver() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        curPlayer.hand.add(new Copper());
        curPlayer.hand.add(new VictoryCard("estate", 2, 0, 1));

        mockBoard.gui = mockGui;
        mockBoard.trashPile = new ArrayList<>();

        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("estate");

        mockBoard.transferCardFromDeckToPlayer("silver", curPlayer);
        EasyMock.expectLastCall();
        
        EasyMock.replay(mockBoard, mockGui);

        TradingPost post = new TradingPost(mockBoard);
        post.useCardPowers(curPlayer);

        assertEquals(0, curPlayer.hand.size()); // Trashed both. Silver transferred by board, but we only verify the call
        assertEquals(2, mockBoard.trashPile.size()); // 2 cards trashed
        
        EasyMock.verify(mockBoard, mockGui);
    }
}
