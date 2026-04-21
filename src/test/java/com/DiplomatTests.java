package com;

import org.easymock.EasyMock;
import org.junit.Test;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DiplomatTests {

    @Test
    public void testDiplomatActionIncrease() {
        Board mockBoard = EasyMock.mock(Board.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        curPlayer.action = 1;
        // 3 cards initially. drawOneCard() called twice -> 5 cards. 5 <= 5 is TRUE.
        for(int i=0; i<3; i++) curPlayer.hand.add(new Copper());

        EasyMock.replay(mockBoard);

        Diplomat diplomat = new Diplomat(mockBoard);
        diplomat.useCardPowers(curPlayer);

        assertEquals(5, curPlayer.hand.size()); 
        assertEquals(3, curPlayer.action); // 1 baseline + 2 extra
        EasyMock.verify(mockBoard);
    }

    @Test
    public void testDiplomatNoActionIfHandLarge() {
        Board mockBoard = EasyMock.mock(Board.class);
        ResourceBundle bundle = ResourceBundle.getBundle(Language.ENGLISH.bundleName);

        Player curPlayer = new Player(bundle);
        curPlayer.hand.clear();
        curPlayer.action = 1;
        // 4 cards initially. drawOneCard() called twice -> 6 cards. 6 <= 5 is FALSE.
        for(int i=0; i<4; i++) curPlayer.hand.add(new Copper());

        EasyMock.replay(mockBoard);

        Diplomat diplomat = new Diplomat(mockBoard);
        diplomat.useCardPowers(curPlayer);

        assertEquals(6, curPlayer.hand.size()); 
        assertEquals(1, curPlayer.action); // Still 1
        EasyMock.verify(mockBoard);
    }

    @Test
    public void testDiplomatGetTypes() {
        Board mockBoard = EasyMock.mock(Board.class);
        Diplomat diplomat = new Diplomat(mockBoard);
        List<String> types = diplomat.getTypes();
        assertTrue("Should contain Action", types.contains("Action"));
        assertTrue("Should contain Reaction", types.contains("Reaction"));
    }
}