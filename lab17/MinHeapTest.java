import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapTest {

    @Test
    public void minHeapBasicTest() {
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
        assertTrue(minHeap.contains(5));
        assertTrue(minHeap.removeMin() == 5);
        assertTrue(minHeap.contains(7));
        assertTrue(minHeap.removeMin() == 7);
        assertTrue(minHeap.contains(8));
        assertTrue(minHeap.removeMin() == 8);
        assertTrue(minHeap.contains(9));
        assertTrue(minHeap.removeMin() == 9);
        System.out.println(minHeap.toString());
    }

    @Test
    public void minHeapUpdateTest() {
        MinHeap<Integer> minHeap = new MinHeap<>();

        minHeap.insert(7);
        minHeap.insert(3);
        minHeap.insert(9);
        minHeap.insert(8);
        minHeap.insert(5);
        minHeap.insert(4);
        minHeap.insert(1);
        System.out.println(minHeap.toString());

        System.out.println("=========== after update ==============");
        minHeap.update(9);
        System.out.println(minHeap.toString());

    }
}
