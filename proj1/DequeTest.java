import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")

public class DequeTest {

    private int generateRandom(int low, int high) {
        Random g = new Random();
        return g.nextInt(high - low) + low;
    };

    @Test
    public void dequeTest1() {
        Deque<Object> arr = new ArrayDeque<>();
        Deque<Object> lld = new LinkedListDeque<>();

        int r1 = generateRandom(600, 1000);
        for (int i = 0; i < r1; i++) {
            arr.addFirst(i);
            lld.addFirst(i);
        }
        assertEquals("arr should have the correct size", r1, arr.size());
        assertEquals("arr should return the first item", r1 - 1, arr.get(0));
        assertEquals("arr should return the last item", 0, arr.get(r1 - 1));
        assertEquals("list should have the correct size", r1, lld.size());
        assertEquals("list should return the first item", r1 - 1, lld.get(0));
        assertEquals("list should return the last item", 0, lld.get(r1 - 1));

        for (int i = 0; i < r1; i++) {
            assertEquals("should keep the order", r1 - i - 1, arr.removeFirst());
            assertEquals("should keep the order", r1 - i - 1, lld.removeFirst());
        }
        assertTrue("arr should shrink when it has 0 items", arr.isEmpty());
        assertNull("arr should return null when it is empty", arr.get(0));
        assertTrue("list should be empty", lld.isEmpty());
        assertNull("list should return null when it is empty", lld.get(0));

        int r2 = generateRandom(1000, 2000);
        for (int i = 0; i < r2; i++) {
            arr.addLast(i);
            lld.addLast(i);
        }
        assertEquals("arr should resize with a large num of insertions", r2, arr.size());
        assertEquals("arr should return the first item", 0, arr.get(0));
        assertEquals("list should hold a large number of insertions", r2, lld.size());
        assertEquals("list should return the first item", 0, lld.get(0));
    }
}
