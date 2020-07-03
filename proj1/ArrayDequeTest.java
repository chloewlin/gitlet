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
    public void resizeArrayTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            strArray.addFirst("F");
        }
        assertEquals(10, strArray.size());
        assertEquals("F", strArray.get(7));
    }
}
