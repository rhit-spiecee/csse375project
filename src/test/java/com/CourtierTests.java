package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import static org.junit.Assert.assertEquals;

public class CourtierTests {

    @Test
    public void testCourtierRevealAction() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        Village actionCard = new Village();
        player.hand.add(actionCard);
        player.action = 1;
        player.coins = 0;

        mockBoard.gui = mockGui;

        // Reveal Village (1 type: Action)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("village");
        // Should get 1 choice. Let's say choice 2 (+$3)
        EasyMock.expect(mockGui.getCourtierOptions(1)).andReturn(Collections.singletonList(2));

        EasyMock.replay(mockBoard, mockGui);

        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(player);

        assertEquals(3, player.coins);
        assertEquals(1, player.action); // No change from Courtier choice

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierRevealDualType() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        Mill dualTypeCard = new Mill(mockBoard);
        player.hand.add(dualTypeCard);
        player.action = 1;
        player.buy = 1;

        mockBoard.gui = mockGui;

        // Reveal Mill (2 types: Action, Victory)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("mill");
        // Should get 2 choices. Let's say choice 0 (+1 Action) and 1 (+1 Buy)
        EasyMock.expect(mockGui.getCourtierOptions(2)).andReturn(Arrays.asList(0, 1));

        EasyMock.replay(mockBoard, mockGui);

        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(player);

        assertEquals(2, player.action);
        assertEquals(2, player.buy);

        EasyMock.verify(mockBoard, mockGui);
    }

    @Test
    public void testCourtierGainGold() {
        Board mockBoard = EasyMock.mock(Board.class);
        Gui mockGui = EasyMock.mock(Gui.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player player = new Player(bundle);
        player.hand.clear();
        player.hand.add(new Copper());

        mockBoard.gui = mockGui;

        // Reveal Copper (1 type: Treasure)
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.anyString(), EasyMock.anyObject())).andReturn("copper");
        // Choice 3: Gain a Gold
        EasyMock.expect(mockGui.getCourtierOptions(1)).andReturn(Collections.singletonList(3));
        
        // Expected gain card call
        mockBoard.transferCardFromDeckToPlayer("gold", player);
        EasyMock.expectLastCall();

        EasyMock.replay(mockBoard, mockGui);

        Courtier courtier = new Courtier(mockBoard);
        courtier.useCardPowers(player);

        EasyMock.verify(mockBoard, mockGui);
    }
}
