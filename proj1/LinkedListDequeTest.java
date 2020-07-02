import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    /** Adds a few things to the deque, checking isEmpty() and size() are correct,
     * finally printing the results. */
    @Test
    public void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        System.out.println("Make sure to uncomment the lines below (and delete this line).");

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

    /** Adds an item while the list is not empty*/
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

    /** Adds an item, then removes an item, and ensures that deque is empty afterwards. */
    @Test
    public void addRemoveTest() {
        System.out.println("Running add/remove test.");
        System.out.println("Make sure to uncomment the lines below (and delete this line).");

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
            assertEquals("index should not be greater than the size of deque", null, nums.get(5));
            assertEquals("index cannot be a negative number", null, nums.get(-3));

            nums.addFirst(1);
            nums.addLast(2);
            nums.addLast(3);
            nums.addFirst(0);
            nums.addLast(4);
            int removedLast1 = nums.removeLast();
            assertEquals("removedLast() should return the item removed",4, removedLast1);
            nums.addLast(4);

            assertEquals(0, (int) nums.get(0));
            assertEquals(1, (int) nums.get(1));
            assertEquals(2, (int) nums.get(2));
            assertEquals(3, (int) nums.get(3));
            assertEquals(4, (int) nums.get(4));

            int removedFirst1 = nums.removeFirst();
            assertEquals("removedLast() should return the item removed", 0, removedFirst1);
            assertEquals("removeFirst() should remove from the front",1, (int) nums.get(0));

            int removedLast2 = nums.removeLast();
            assertEquals("removeLast() should remove from the back", 3, (int) nums.get(2));
            assertEquals("removeLast() should reduce size by 1", 3, nums.size());

            int removedLast3 = nums.removeLast();
            assertEquals("removeLast() should return the item removed", 3, removedLast3);

            int removedFirst2 = nums.removeFirst();
            assertEquals("removedLast() should return the item removed", 1, removedFirst2);
            nums.removeLast();

            assertEquals(0, nums.size());
            assertEquals("removeFirst() return null when the deque is empty", null, nums.removeFirst());
        } finally {
            System.out.println("Printing out deque: ");
            nums.printDeque();
        }
    }
    /** get the item at the given index */
    @Test
    public void getRecursiveTest() {
        LinkedListDeque<Integer> nums = new LinkedListDeque<>();

        try {
            assertTrue(nums.isEmpty());
            assertEquals("index should not be greater than the size of deque", null, nums.getRecursive(5));
            assertEquals("index cannot be a negative number", null, nums.getRecursive(-3));

            nums.addFirst(1);
            nums.addLast(2);
            nums.addLast(3);
            nums.addFirst(0);
            nums.addLast(4);
            int removedLast1 = nums.removeLast();
            assertEquals("removedLast() should return the item removed", 4, removedLast1);
            nums.addLast(4);

            assertEquals(0, (int) nums.getRecursive(0));
//            assertEquals(1, (int) nums.getRecursive(1));
//            assertEquals(2, (int) nums.getRecursive(2));
//            assertEquals(3, (int) nums.getRecursive(3));
//            assertEquals(4, (int) nums.getRecursive(4));

//            int removedFirst1 = nums.removeFirst();
//            assertEquals("removedLast() should return the item removed", 0, removedFirst1);
//            assertEquals("removeFirst() should remove from the front", 1, (int) nums.getRecursive(0));
//
//            int removedLast2 = nums.removeLast();
//            assertEquals("removeLast() should remove from the back", 3, (int) nums.getRecursive(2));
//            assertEquals("removeLast() should reduce size by 1", 3, nums.size());
//
//            int removedLast3 = nums.removeLast();
//            assertEquals("removeLast() should return the item removed", 3, removedLast3);
//
//            int removedFirst2 = nums.removeFirst();
//            assertEquals("removedLast() should return the item removed", 1, removedFirst2);
//            nums.removeLast();
//
//            assertEquals(0, nums.size());
//            assertEquals("removeFirst() return null when the deque is empty", null, nums.removeFirst());
        } finally {
            System.out.println("Printing out deque: ");
            nums.printDeque();
        }
    }
}
