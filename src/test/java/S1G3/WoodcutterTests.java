package S1G3;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class WoodcutterTests {

    static class StubPlayer extends Player {
        public StubPlayer(Woodcutter woodcutter) {
            coins = 0;
            action = 1;
            buy = 1;
            hand.add(woodcutter);
        }
    }

    @Test
    public void testCardBehavior() {
        Woodcutter woodcutter = new Woodcutter();
        Player player = new StubPlayer(woodcutter);

        woodcutter.useActionCard(player);

        assertEquals(2, player.coins);
        assertEquals(0, player.action);
        assertEquals(2, player.buy);
        assertEquals(0, player.hand.size());
        assertEquals(1, player.discardPile.size());
    }

}
