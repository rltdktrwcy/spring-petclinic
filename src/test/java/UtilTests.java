import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilTests {

    private final Util util = new Util();

    @Test
    public void testPlusWithPositiveNumbers() {
        assertEquals(5, util.plus(2, 3));
    }

    @Test
    public void testPlusWithNegativeNumbers() {
        assertEquals(-5, util.plus(-2, -3));
    }

    @Test
    public void testPlusWithZero() {
        assertEquals(3, util.plus(3, 0));
        assertEquals(3, util.plus(0, 3));
    }

    @Test
    public void testPlusWithPositiveAndNegativeNumbers() {
        assertEquals(1, util.plus(3, -2));
        assertEquals(-1, util.plus(-3, 2));
    }

}
