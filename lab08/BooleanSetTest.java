import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanSetTest {

    @Test
    public void testBasics() {
        BooleanSet aSet = new BooleanSet(100);
        assertEquals(0, aSet.size());
        for (int i = 0; i < 100; i += 2) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }
        assertEquals(50, aSet.size());

        for (int i = 0; i < 100; i += 2) {
            aSet.remove(i);
            assertFalse(aSet.contains(i));
        }
        assertTrue(aSet.isEmpty());
        assertEquals(0, aSet.size());
    }

    @Test
    public void testBooleanSet() {
        BooleanSet aSet = new BooleanSet(10);
        assertEquals(0, aSet.size());
        for (int i = 0; i < 10; i++) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }
        assertEquals(10, aSet.size());

        for (int i = 0; i < 10; i += 2) {
            aSet.remove(i);
            assertFalse(aSet.contains(i));
        }

        int[] expected = {1, 3, 5, 7, 9};
        int[] result = aSet.toIntArray();

        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
}
