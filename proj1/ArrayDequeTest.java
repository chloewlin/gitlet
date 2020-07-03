import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void arrayLengthTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals(8, strArray.size());
    }

    @Test
    public void addFirstTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals(8, strArray.size());
        strArray.addFirst("zero");
        strArray.printDeque();
        strArray.addFirst("one");
        strArray.printDeque();
        strArray.addFirst("two");
        strArray.printDeque();
    }

    @Test
    public void addLastTest() {
        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals(8, strArray.size());
        strArray.addLast("0");
        strArray.addLast("1");
        strArray.addLast("2");
        strArray.addLast("3");
        strArray.addLast("4");
        strArray.addLast("5");
        strArray.addLast("6");
        strArray.addLast("7");
        strArray.printDeque();
    }

    @Test
    public void addFrontAndLastTest() {
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
}
