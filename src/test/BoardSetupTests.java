import org.junit.Test;
import static org.junit.Assert.*;

public class BoardSetupTests {

    @Test
    public void testOnePlayer(){
        Board board = new Board();
        assertFalse(board.startGame(1));
    }
}
