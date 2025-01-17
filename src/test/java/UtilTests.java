import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UtilTests {

    @Test
    public void testPlus() {
        Util util = new Util();
        assertEquals(5, util.plus(2, 3));
        assertEquals(0, util.plus(0, 0));
        assertEquals(-5, util.plus(-2, -3));
        assertEquals(1, util.plus(-2, 3));
    }

}
