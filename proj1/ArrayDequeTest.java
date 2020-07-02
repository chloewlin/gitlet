import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void arrayLengthTest() {

        ArrayDeque<String> strArray = new ArrayDeque<>();
        assertEquals(8, strArray.size());
    }
}
