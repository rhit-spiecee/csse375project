package S1G3;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemodelTests {
    static class StubPlayer extends Player {
        public StubPlayer(Remodel remodel) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(remodel);
            hand.add(new TreasureCard("gold", 6, Card.CardType.TREASURE, 3));
        }
    }

    @Test
    public void testCardBehavior() {
        GUI mockGui = EasyMock.mock(GUI.class);
        EasyMock.expect(mockGui.getNumPlayers()).andReturn(2);
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("gold");
        EasyMock.expect(mockGui.getCardFromAvailableSelection(EasyMock.notNull(), EasyMock.notNull())).andReturn("province");

        EasyMock.replay(mockGui);
        Board board = Board.fromGUI(mockGui);
        Remodel remodel = new Remodel(board);
        Player player = new StubPlayer(remodel);

        remodel.useActionCard(player);

        assertEquals(0, player.coins);
        assertEquals(0, player.action);
        assertEquals(1, player.buy);
        assertEquals(1, player.hand.size());
        assertEquals(1, player.discardPile.size());

        EasyMock.verify(mockGui);
    }
}
