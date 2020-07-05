import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")

public class ArrayDequeTest {

    @Test
    public void addFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<String>();
        strArray.addFirst("zero");
        strArray.addFirst("one");
        strArray.addFirst("two");
        assertEquals("should increment array size", 3, strArray.size());
        assertTrue("should wrap around the array", strArray.get(0).equals("two"));
    }

    @Test
    public void addFirstTest2() {
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 0; i < 8; i++) {
            nums.addFirst(i);
        }
        assertEquals("should hold up to 8 items", 8, nums.size());
        assertTrue("should return the first element", nums.get(0).equals(7));

        nums.addFirst(8);
        assertEquals("should resize the array", 9, nums.size());
        assertTrue("should return the new first element", nums.get(0).equals(8));
    }

    @Test
    public void addLastTest() {
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 0; i < 8; i++) {
            nums.addLast(i);
        }
        assertEquals("should increment array size", 8, nums.size());

        for (int i = 0; i < 8; i++) {
            assertTrue("should return the elements in order", nums.get(i) == i);
        }
    }

    @Test
    public void addNextFrontAndLastTest() {
        /** create an ArrayDeque using addFirst and addLast: [F, B, B, B, B, F, F, F]
         *  get() should return items in the correct order
         * */
        ArrayDeque<String> strArray = new ArrayDeque<String>();
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                strArray.addLast("B");
            } else {
                strArray.addFirst("F");
            }
        }
        for (int i = 0; i < 4; i++) {
            assertEquals("should get item 1 to 4", "F", strArray.get(i));
        }
        for (int i = 4; i < 8; i++) {
            assertEquals("should get item 5 to 6", "B", strArray.get(i));
        }
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<String>();
        assertEquals("should return null", null, strArray.removeFirst());

        ArrayDeque<String> strArray1 = new ArrayDeque<String>();
        strArray1.addFirst("F");
        assertEquals("should return first item", "F", strArray1.removeFirst());

        ArrayDeque<String> strArray3 = new ArrayDeque<String>();
        strArray3.addFirst("F");
        strArray3.addLast("B");
        strArray3.addFirst("F");
        assertEquals("should return first item", "F", strArray3.removeFirst());

        ArrayDeque<String> strArray4 = new ArrayDeque<String>();
        for (int i = 0; i < 9; i++) {
            strArray4.addFirst(Integer.toString(i));
        }
        String front = strArray4.removeFirst();
        assertEquals("should return front after resizing", "8", front);
    }

    @Test
    public void removeLastTest() {
        ArrayDeque<String> strArray = new ArrayDeque<String>();
        assertEquals("should return null", null, strArray.removeLast());

        ArrayDeque<String> strArray1 = new ArrayDeque<String>();
        strArray1.addLast("B");
        assertEquals("should return the last item", "B", strArray1.removeLast());

        ArrayDeque strArray2 = new ArrayDeque();
        for (int i = 0; i < 8; i++) {
            strArray2.addLast(i);
        }
        assertEquals("should return the last element", 7, strArray2.removeLast());
        assertEquals("should return the first element", 0, strArray2.get(0));

        for (int i = 6; i > 0; i--) {
            assertEquals("should return the last element", i, strArray2.removeLast());
        }
        assertEquals("should return the first element", 0, strArray2.get(0));
    }

    @Test
    public void resizeArrayTest1() {
        /** create an ArrayDeque: [4, 5, 6, 7, 8, 1, 2, 3]
         *  array should expend when we add 9, and resize to become
         *  [1, 2, 3, 4, 5, 6, 7, 8, 9, null, null, null, null, null, null, null]
         * */
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 4; i > 0; i--) {
            nums.addFirst(i);
        }
        for (int i = 0; i < 5; i++) {
            nums.addLast(i + 5);
        }
        assertEquals(9, nums.size());

        for (int i = 0; i < 9; i++) {
            int curr = nums.get(i);
            assertEquals(i + 1, curr);
        }
        assertTrue("should get new front", nums.get(0) == 1);
        assertTrue("should get next item", nums.get(1) == 2);
    }

    @Test
    public void resizeArrayTest2() {
        /** ArrayDeque should not correctly update the nextBack index
         *  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, null, null, null, null]
         * */
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 0; i < 12; i++) {
            nums.addLast(i + 1);
        }

        for (int i = 0; i < 12; i++) {
            int curr = nums.get(i);
            assertEquals(i + 1, curr);
        }

        assertTrue("should get new front", nums.get(0) == 1);
        assertTrue("should get next item", nums.get(1) == 2);
    }

    @Test
    public void resizeArrayTest3() {
        /** create an ArrayDeque using addFirst: [8, 1, 2, 3, 4, 5, 6, 7]
         *  array should expend when we add 9, and resize to become
         *  [1, 2, 3, 4, 5, 6, 7, 8, null, null, null, null, null, null, null, 9]
         * */
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 7; i >= 0; i--) {
            nums.addFirst(i + 1);
        }
        nums.addFirst(9);
        assertTrue("should get new front", nums.get(0) == 9);
        assertTrue("should get next item", nums.get(1) == 1);
    }

    @Test
    public void shrinkArrayTest1() {
        ArrayDeque<String> strings = new ArrayDeque<String>();
        for (int i = 5; i > 0; i--) {
            strings.addFirst(Integer.toString(i));
        }
        for (int i = 6; i < 10; i++) {
            strings.addLast(Integer.toString(i));
        }
        for (int i = 0; i < 5; i++) {
            strings.removeFirst();
        }
        assertEquals("should correctly resize", 4, strings.size());
        assertTrue("should return front after shrinking", strings.get(0).equals("6"));
    }

    @Test
    public void shrinkArrayTest2() {
        ArrayDeque<String> strings = new ArrayDeque<String>();
        /** create array [9 10 11 12 13 14 15 16 1 2 3 4 5 6 7 8] */
        for (int i = 16; i > 0; i--) {
            strings.addFirst(Integer.toString(i));
        }

        /** remove items to test if array shrinks to [1 2 3 4 null null null null] */
        for (int i = 0; i < 12; i++) {
            strings.removeLast();
        }
        assertEquals("should not shrink when use ratio is 25%", 4, strings.size());

        /** add items to array from the back to test if array doubles when it is full */
        for (int i = 0; i < 4; i++) {
            strings.addLast(Integer.toString(i + 5));
        }
        assertEquals("should hold up to 8 items", 8, strings.size());

        strings.addLast("9");
        assertEquals("should expand with more than 8 items", 9, strings.size());
        assertTrue("should return the first element", strings.get(0).equals("1"));
    }

    @Test
    public void shrinkArrayTest3() {
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 5; i > 0; i--) {
            nums.addFirst(i);
        }
        for (int i = 6; i < 10; i++) {
            nums.addLast(i);
        }
        for (int i = 0; i < 5; i++) {
            nums.removeFirst();
        }
        assertEquals("should not shrink when use ratio is 25%", 4, nums.size());
        assertTrue("should return the first element of array", nums.get(0).equals(6));

        nums.removeFirst();
        assertEquals("should shrink when use ratio is < 25%", 3, nums.size());
        assertTrue("should return first element", nums.get(0).equals(7));
    }

    @Test
    public void shrinkArrayTest4() {
        ArrayDeque<Integer> nums = new ArrayDeque<Integer>();
        for (int i = 1; i < 16; i++) {
            nums.addLast(i);
        }

        for (int i = 0; i < 11; i++) {
            nums.removeLast();
        }

        assertEquals("should not shrink when use ratio is 25%", 4, nums.size());
        assertTrue("should return the first element", nums.get(0).equals(1));

        nums.removeFirst();
        assertEquals("should shrink when use ratio is < 25%", 3, nums.size());
        assertTrue("should return the first element", nums.get(0).equals(2));
    }
}
