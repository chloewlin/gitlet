import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        strArray.addFirst("zero");
        strArray.addFirst("one");
        strArray.addFirst("two");
        assertEquals("Array size should increment correctly", 3, strArray.size());
        assertTrue("addFirst should wrap around the array", strArray.get(0).equals("two"));
    }

    @Test
    public void addLastTest() {
        ArrayDeque<Integer> numsArray = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            numsArray.addLast(i);
        }
        assertEquals("Array size should increment correctly", 8, numsArray.size());
        assertTrue("nextFront should still be at index 0 before resizing", numsArray.get(0) == 7); // failing

        numsArray.addLast(9);
        int nextFront = numsArray.get(0);
        assertEquals("nextFront should still be at index 0 after resizing", 0, nextFront);
    }

    @Test
    public void addNextFrontAndLastTest() {
        /** create an ArrayDeque using addFirst and addLast: [F, B, B, B, B, F, F, F]
         *  get() should return items in the correct order
         * */
        ArrayDeque<String> strArray = new ArrayDeque<>();
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
            assertEquals("should get item 5 to 6","B", strArray.get(i));
        }
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals("should return null when array is emtpy", null, strArray.removeFirst());

        ArrayDeque<String> strArray1 = new ArrayDeque<>();
        strArray1.addFirst("F");
        assertEquals("should return first item","F", strArray1.removeFirst());

        ArrayDeque<String> strArray3 = new ArrayDeque<>();
        strArray3.addFirst("F");
        strArray3.addLast("B");
        strArray3.addFirst("F");
        assertEquals("should return first item", "F", strArray3.removeFirst());

        ArrayDeque<String> strArray4 = new ArrayDeque<>();
        for (int i = 0; i < 9; i++) {
            strArray4.addFirst(Integer.toString(i));
        }
        assertEquals("should return current front after resizing","8", strArray4.removeFirst());
    }

    @Test
    public void removeLastTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals(null, strArray.removeLast());

        ArrayDeque<String> strArray1 = new ArrayDeque<>();
        strArray1.addLast("B");
        assertEquals("B", strArray1.removeLast());

        ArrayDeque<String> strArray2 = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            strArray2.addLast("B");
        }

        assertEquals("B", strArray2.removeLast());
        assertEquals(7, strArray2.size());
        strArray2.printDeque();
        assertEquals("should return current front", null, strArray2.get(0)); // failing
//        assertEquals("B", strArray2.removeLast());
//        assertEquals(null, strArray2.get(7));
    }

    @Test
    public void resizeArrayTest1() {
        /** create an ArrayDeque: [4, 5, 6, 7, 8, 1, 2, 3]
         *  array should expend when we add 9, and resize to become
         *  [1, 2, 3, 4, 5, 6, 7, 8, 9, null, null, null, null, null, null, null]
         * */
        ArrayDeque<Integer> nums = new ArrayDeque<>();
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
        ArrayDeque<Integer> nums = new ArrayDeque<>();
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
        ArrayDeque<Integer> nums = new ArrayDeque<>();
        for (int i = 7; i >= 0; i--) {
            nums.addFirst(i + 1);
        }
        nums.addFirst(9);
        assertTrue("should get new front", nums.get(0) == 9);
        assertTrue("should get next item", nums.get(1) == 1);
    }

    @Test
    public void shrinkArrayTest1() {
        ArrayDeque strArray = new ArrayDeque();
        for (int i = 5; i > 0; i--) {
            strArray.addFirst(Integer.toString(i));
        }
        for (int i = 6; i < 10; i++) {
            strArray.addLast(Integer.toString(i));
        }
        for (int i = 0; i < 5; i++) {
            strArray.removeFirst();
        }
        assertEquals("should correctly resize", 4, strArray.size());
        assertTrue("should return correct front after shrinking", strArray.get(0).equals("6"));
    }

    @Test
    public void shrinkArrayTest2() {
        ArrayDeque strArray = new ArrayDeque();
        for (int i = 16; i > 0; i--) {
            strArray.addFirst(Integer.toString(i));
        }
        for (int i = 0; i < 12; i++) {
            strArray.removeLast();
        }
        assertEquals("should shrink array when use ration is <= 25%", 4, strArray.size());
        strArray.printWholeDeque();
        strArray.addLast("X");
        strArray.addLast("X2");
        strArray.addLast("X3");
        strArray.addLast("X4");
        strArray.addLast("X5"); // tests error
//        for (int i = 0; i < 5; i++) {
//            strArray.addLast(Integer.toString(i));
//        }
        strArray.printWholeDeque();

//        assertTrue("should return correct front after shrinking", strArray.get(0).equals("6"));
    }
}
