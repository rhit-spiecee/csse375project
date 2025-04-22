import org.junit.Test;

import static org.junit.Assert.*;

public class UtilitiesTest {
    @Test
    public void capitalizeTest() {
        assertEquals("Test", Utilities.capitalize("test"));
    }
}
