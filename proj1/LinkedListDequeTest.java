import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")

public class LinkedListDequeTest {

    private static class Obj<T> {
        private int item;
        private static String string = new String("object");

        Obj(int item) {
            this.item = item;
        }
    }

    /** ensure all the methods work for doubles */
    @Test
    public void typeDoubleTest() {
        LinkedListDeque<Double> doubles = new LinkedListDeque<>();

        assertTrue(doubles.isEmpty());
        doubles.addFirst(0.0012);
        assertEquals(1, doubles.size());
        double value = doubles.get(0);
        assertEquals(0.0012, value, 0.0002);
    }

    /** ensure all the methods work for booleans */
    @Test
    public void typeBooleanTest() {
        LinkedListDeque<Boolean> booleans = new LinkedListDeque<>();

        assertTrue(booleans.isEmpty());
        booleans.addFirst(true);
        assertEquals(1, booleans.size());
        assertTrue(booleans.get(0));
        booleans.addLast(true);
        booleans.addLast(false);
        assertFalse(booleans.getRecursive(2));
        booleans.removeFirst();
        booleans.removeLast();
        assertEquals(1, booleans.size());
        assertNull(booleans.get(-5));
        assertNull(booleans.get(100));
    }

    /** ensure all the methods work for generic objects */
    @Test
    public void typeObjTest() {
        LinkedListDeque<Obj> objects = new LinkedListDeque<>();

        assertTrue(objects.isEmpty());
        objects.addFirst(new Obj(99));
        assertEquals(1, objects.size());
        Obj value = objects.get(0);
        assertEquals(99, value.item);
        assertEquals("object", value.string);
        objects.addLast(new Obj(199));
        objects.addLast(new Obj(1999));
        objects.addLast(new Obj(19999));
        Obj removedLast1 = objects.removeLast();
        assertEquals(19999, removedLast1.item);
        Obj removedFirst1 = objects.removeFirst();
        assertEquals(99, removedFirst1.item);
        assertNull(objects.get(-5));
        assertNull(objects.get(100));
    }

    /** Adds a few things to the deque, checking isEmpty() and size() are correct,
     * finally printing the results. */
    @Test
    public void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");

        LinkedListDeque<String> lld1 = new LinkedListDeque<>();

        // Java will try to run the below code.
        // If there is a failure, it will jump to the finally block before erroring.
        // If all is successful, the finally block will also run afterwards.
        try {

            assertTrue(lld1.isEmpty());

            lld1.addFirst("front");
            assertEquals(1, lld1.size());
            assertFalse(lld1.isEmpty());

            lld1.addLast("middle");
            assertEquals(2, lld1.size());

            lld1.addLast("back");
            assertEquals(3, lld1.size());

        } finally {
            // The deque will be printed at the end of this test
            // or after the first point of failure.
            System.out.println("Printing out deque: ");
            lld1.printDeque();
        }
    }

    /** add items with addFirst() and addLast(), ensure size() is correct */
    @Test
    public void addIsNotEmptySizeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        assertTrue(lld1.isEmpty());
        lld1.addFirst(1);
        assertEquals(1, lld1.size());
        assertFalse(lld1.isEmpty());
        lld1.addFirst(2);
        assertEquals(2, lld1.size());
        lld1.addLast(3);
        assertEquals(3, lld1.size());
        lld1.addLast(4);
        assertEquals(4, lld1.size());
    }

    /** add item with addFirst() and addLast(), remove item with removeFirst()
     * and removeLast(), ensure size() is correct
     * */
    @Test
    public void addAndRemoveIntegerTest() {
        System.out.println("Running add/remove test.");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        try {
            assertTrue(lld1.isEmpty());

            lld1.addFirst(10);
            assertFalse(lld1.isEmpty());

            lld1.removeFirst();
            assertTrue(lld1.isEmpty());

            lld1.addLast(10);
            assertFalse(lld1.isEmpty());

            lld1.removeLast();
            assertTrue(lld1.isEmpty());
        } finally {
            System.out.println("Printing out deque: ");
            lld1.printDeque();
        }
    }

    /** get the item at the given index */
    @Test
    public void getTest() {
        LinkedListDeque<Integer> nums = new LinkedListDeque<>();

        try {
            assertTrue(nums.isEmpty());
            assertEquals("index should be < deque size", null, nums.get(5));
            assertEquals("index cannot be a negative number", null, nums.get(-3));

            nums.addFirst(1);
            nums.addLast(2);
            nums.addLast(3);
            nums.addFirst(0);
            nums.addLast(4);
            int removedLast1 = nums.removeLast();
            assertEquals("should return the item removed", 4, removedLast1);
            nums.addLast(4);

            assertEquals(0, (int) nums.get(0));
            assertEquals(1, (int) nums.get(1));
            assertEquals(2, (int) nums.get(2));
            assertEquals(3, (int) nums.get(3));
            assertEquals(4, (int) nums.get(4));

            int removedFirst1 = nums.removeFirst();
            assertEquals("should return the item removed", 0, removedFirst1);
            assertEquals("should remove from the front", 1, (int) nums.get(0));

            int removedLast2 = nums.removeLast();
            assertEquals("should remove from the back", 3, (int) nums.get(2));
            assertEquals("should reduce size by 1", 3, nums.size());

            int removedLast3 = nums.removeLast();
            assertEquals("should return the item removed", 3, removedLast3);

            int removedFirst2 = nums.removeFirst();
            assertEquals("should return the item removed", 1, removedFirst2);
            nums.removeLast();

            assertEquals(0, nums.size());
            assertEquals("return null when empty", null, nums.removeFirst());
        } finally {
            System.out.println("Printing out deque: ");
            nums.printDeque();
        }
    }

    /** get the item at the given index recursively */
    @Test
    public void getRecursiveTest() {
        LinkedListDeque<Integer> nums = new LinkedListDeque<>();

        try {
            assertTrue(nums.isEmpty());
            assertEquals(null, nums.getRecursive(5));
            assertEquals(null, nums.getRecursive(-3));

            nums.addFirst(1);
            nums.addLast(2);
            nums.addLast(3);
            nums.addFirst(0);
            nums.addLast(4);
            int removedLast1 = nums.removeLast();
            assertEquals("should return the item removed", 4, removedLast1);
            nums.addLast(4);

            assertEquals(0, (int) nums.getRecursive(0));
            assertEquals(1, (int) nums.getRecursive(1));
            assertEquals(2, (int) nums.getRecursive(2));
            assertEquals(3, (int) nums.getRecursive(3));
            assertEquals(4, (int) nums.getRecursive(4));

            int removedFirst1 = nums.removeFirst();
            assertEquals("should return the item removed", 0, removedFirst1);
            assertEquals(1, (int) nums.getRecursive(0));

            int removedLast2 = nums.removeLast();
            assertEquals(3, (int) nums.getRecursive(2));
            assertEquals("should reduce size by 1", 3, nums.size());

            int removedLast3 = nums.removeLast();
            assertEquals("should return the item removed", 3, removedLast3);

            int removedFirst2 = nums.removeFirst();
            assertEquals("should return the item removed", 1, removedFirst2);
            nums.removeLast();

            assertEquals(0, nums.size());
            assertEquals("return null when empty", null, nums.removeFirst());
        } finally {
            System.out.println("Printing out deque: ");
            nums.printDeque();
        }
    }
}
