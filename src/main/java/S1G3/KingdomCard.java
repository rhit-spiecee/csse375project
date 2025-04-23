package S1G3;

import java.util.ArrayList;

public abstract class KingdomCard extends Card{
    public KingdomCard(String name, int cost, int value) {
        super(name, cost, CardType.KINGDOM, value);
    }

    public abstract void useActionCard(Player currentPlayer);
}
