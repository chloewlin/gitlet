import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        strArray.addFirst("zero");
        strArray.printDeque();
        strArray.addFirst("one");
        strArray.printDeque();
        strArray.addFirst("two");
        strArray.printDeque();
    }

    @Test
    public void addLastTest() {
        ArrayDeque<Integer> nums = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            nums.addLast(i);
        }
        nums.printDeque();
    }

    @Test
    public void addnextFrontAndLastTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals(8, strArray.size());
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
        assertEquals(null, strArray.removeFirst());

        ArrayDeque<String> strArray1 = new ArrayDeque<>();
        strArray1.addFirst("F");
        assertEquals("F", strArray1.removeFirst());

        ArrayDeque<String> strArray3 = new ArrayDeque<>();
        strArray3.addFirst("F");
        strArray3.addLast("B");
        strArray3.addFirst("F");
        assertEquals("F", strArray3.removeFirst());

        ArrayDeque<String> strArray4 = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            strArray4.addFirst("F");
        }
        assertEquals("F", strArray4.removeFirst());
        assertEquals(null, strArray4.get(1));
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
        assertEquals(null, strArray2.get(0));
        assertEquals("B", strArray2.removeLast());
        assertEquals(null, strArray2.get(7));
    }
}
