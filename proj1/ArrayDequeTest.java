import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        strArray.addFirst("zero");
        strArray.addFirst("one");
        strArray.addFirst("two");
        assertEquals("Array size should be correctly incremented", 3, strArray.size());
    }

    @Test
    public void addLastTest() {
        ArrayDeque<Integer> numsArray = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            numsArray.addLast(i);
        }
        assertEquals("Array size should be correctly incremented", 8, numsArray.size());
        numsArray.printDeque();
    }

    @Test
    public void addNextFrontAndLastTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                strArray.addLast("B");
            } else {
                strArray.addFirst("F");
            }
        }
        assertEquals("F", strArray.get(0));
        assertEquals("B", strArray.get(1));
        assertEquals("F", strArray.get(7));
        strArray.printDeque();
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
        strArray3.printDeque();
        assertEquals("should return first item", "F", strArray3.removeFirst());

        ArrayDeque<String> strArray4 = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            strArray4.addFirst("F");
        }
        assertEquals("should return first item","F", strArray4.removeFirst());
        assertEquals("should set item at old front to null",null, strArray4.get(1));
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
        assertEquals(null, strArray2.get(0));
        assertEquals("B", strArray2.removeLast());
        assertEquals(null, strArray2.get(7));
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

        for (int i = 0; i < 8; i++) {
            int curr = nums.get(i);
            assertEquals(i + 1, curr);
        }
        int newFront = nums.get(15);
        assertEquals(9, newFront);
    }

//
//    @Test
//    public void shrinkArrayTest() {
//        ArrayDeque strArray = new ArrayDeque();
//        for (int i = 0; i < 5; i++) {
//            strArray.addFirst(  "F");
//        }
//        for (int i = 0; i < 5; i++) {
//            strArray.addLast("B");
//        }
//        strArray.printDeque();
//        for (int i = 0; i < 7; i++) {
//            strArray.removeFirst();
//            strArray.printDeque();
//        }
//        strArray.printDeque();
//        assertEquals(3, strArray.size());
//    }
}
