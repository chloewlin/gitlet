import org.junit.Test;

import java.util.Arrays;

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

        BooleanSet aSet1 = new BooleanSet(100);
        assertEquals(0, aSet1.size());
        for (int i = 0; i < 10; i += 2) {
            aSet1.add(i);
            assertTrue(aSet1.contains(i));
        }
        assertEquals(5, aSet1.size());
        int[] result = new int[] {0, 2, 4, 6, 8};
        assertArrayEquals(result, aSet1.toIntArray());

        aSet1.remove(0);
        int[] result1 = new int[] {2, 4, 6, 8};
        assertArrayEquals(result1, aSet1.toIntArray());
    }
}