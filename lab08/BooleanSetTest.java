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

    @Test
    public void testBooleanSet2() {
        BooleanSet aSet = new BooleanSet(10);
        assertEquals(0, aSet.size());
        for (int i = 0; i < 10; i += 2) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }
        aSet.add(0);
        aSet.add(4);
        assertEquals("add the same element repeatedly should not affect the size", 5, aSet.size());

        int[] expected = {0, 2, 4, 6, 8};
        int[] result = aSet.toIntArray();

        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
}
