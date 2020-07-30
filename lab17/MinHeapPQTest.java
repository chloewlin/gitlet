import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void PQTest() {
        MinHeapPQ<String> PQ = new MinHeapPQ<>();

        PQ.insert("cat", 9);
        PQ.insert("munchkin", 2); // X
        PQ.insert("shiba", 7); // X
        PQ.insert("pomeranian", 6);
        PQ.insert("scottishShortHair", 1); // X
        PQ.insert("britishShortHair", 3);
        PQ.insert("akita", 5);


        System.out.println(PQ.toString());

        assertTrue(PQ.peek().equals("scottishShortHair"));
        assertTrue(PQ.poll().equals("scottishShortHair"));

        assertTrue(PQ.peek().equals("munchkin"));
        assertTrue(PQ.poll().equals("munchkin"));


//        System.out.println("========== tree after removing ==========");
        System.out.println(PQ.toString());
//
        PQ.changePriority("shiba", 1);
        assertTrue(PQ.peek().equals("shiba"));
        assertTrue(PQ.poll().equals("shiba"));

        System.out.println(PQ.toString());
    }
}
