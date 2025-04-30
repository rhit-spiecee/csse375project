package S1G3;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkshopTests {
    static class StubPlayer extends Player {
        public StubPlayer(Workshop workshop) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(workshop);
        }
    }

    @Test
    public void testCardBehavior() {
        GUI mockGui = EasyMock.mock(GUI.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("smithy");


        EasyMock.replay(mockGui);
        Board board = Board.fromGUI(mockGui);
        Workshop workshop = new Workshop(board);
        Player player = new WorkshopTests.StubPlayer(workshop);

        workshop.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }
}
