import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapTest {

    @Test
    public void minHeapTest1() {
        MinHeap<Integer> minHeap = new MinHeap<>();

        minHeap.insert(7);
        assertTrue(minHeap.findMin() == 7);
        minHeap.insert(3);
        assertTrue(minHeap.findMin() == 3);
        minHeap.insert(9);
        assertTrue(minHeap.findMin() == 3);
        minHeap.insert(8);
        assertTrue(minHeap.findMin() == 3);
        minHeap.insert(5);
        assertTrue(minHeap.findMin() == 3);
        minHeap.insert(4);
        assertTrue(minHeap.findMin() == 3);
        minHeap.insert(1);
        assertTrue(minHeap.findMin() == 1);
//        System.out.println(minHeap.toString());


        assertTrue(minHeap.removeMin() == 1);
        assertTrue(minHeap.removeMin() == 3);
        assertTrue(minHeap.removeMin() == 4);
        assertTrue(minHeap.removeMin() == 5);
        assertTrue(minHeap.removeMin() == 7);
        assertTrue(minHeap.removeMin() == 8);
        assertTrue(minHeap.removeMin() == 9);
        System.out.println(minHeap.toString());
    }
}
