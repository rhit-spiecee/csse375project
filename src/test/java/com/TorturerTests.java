package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class TorturerTests {

    @Test
    public void testTorturerAttackCurse() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();

        Player targetPlayer = new Player(bundle);
        targetPlayer.hand.clear();
        targetPlayer.hand.add(new Copper());

        mockBoard.gui = mockGui;
        mockBoard.players = new ArrayList<>();
        mockBoard.players.add(curPlayer);
        mockBoard.players.add(targetPlayer);

        // False = gain a Curse
        EasyMock.expect(mockGui.getTorturerChoice(1)).andReturn(false);
        mockBoard.transferCardFromDeckToPlayer("cursed", targetPlayer);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        Torturer torturer = new Torturer(mockBoard);
        torturer.useCardPowers(curPlayer);

        assertEquals(3, curPlayer.hand.size()); // +3 Cards
        
        EasyMock.verify(mockBoard, mockGui);
    }
}
