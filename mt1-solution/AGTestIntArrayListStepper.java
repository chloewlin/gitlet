import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Random;

public class AGTestIntArrayListStepper {

    @Test(timeout = 5000)
    public void compilationBonus() {
        assertTrue(true);
    }

    @Test(timeout = 5000)
    public void getZeroTest() {
        IntArrayList lst = new IntArrayList();
        lst.setList(1,2,3,4);

        IntArrayListStepper stepper = new IntArrayListStepper(lst, 1);
        assertEquals(stepper.get(0), 1);

        stepper = new IntArrayListStepper(lst, 3);
        assertEquals(stepper.get(0), 1);
    }

    @Test(timeout = 5000)
    public void stepByZeroTest() {
        IntArrayList lst = new IntArrayList();
        lst.setList(1,2,3,4);

        IntArrayListStepper stepper = new IntArrayListStepper(lst, 0);
        assertEquals(stepper.get(0), 1);
        assertEquals(stepper.get(1), 1);
        assertEquals(stepper.get(2), 1);
        assertEquals(stepper.get(3), 1);
        assertEquals(stepper.get(50), 1);
    }

    @Test(timeout = 5000)
    public void stepByOneTest() {
        IntArrayList lst = new IntArrayList();
        lst.setList(1,2,3,4);

        IntArrayListStepper stepper = new IntArrayListStepper(lst, 1);
        assertEquals(stepper.get(0), 1);
        assertEquals(stepper.get(1), 2);
        assertEquals(stepper.get(2), 3);
        assertEquals(stepper.get(3), 4);
    }

    @Test(timeout = 5000)
    public void stepByOneNegativeTest() {
        IntArrayList lst = new IntArrayList();
        lst.setList(1,2,3,4);

        IntArrayListStepper stepper = new IntArrayListStepper(lst, 1);
        assertEquals(stepper.get(-4), 1);
        assertEquals(stepper.get(-3), 2);
        assertEquals(stepper.get(-2), 3);
        assertEquals(stepper.get(-1), 4);
    }

    @Test(timeout = 5000)
    public void stepByThreeTest() {
        IntArrayList lst = new IntArrayList();
        lst.setList(1,2,3,4,5,6,7,8,9);

        IntArrayListStepper stepper = new IntArrayListStepper(lst, 3);
        assertEquals(stepper.get(0), 1);
        assertEquals(stepper.get(1), 4);
        assertEquals(stepper.get(2), 7);
    }

    @Test(timeout = 5000)
    public void stepByThreeNegativeTest() {
        IntArrayList lst = new IntArrayList();
        lst.setList(1,2,3,4,5,6,7,8,9);

        IntArrayListStepper stepper = new IntArrayListStepper(lst, 3);
        assertEquals(stepper.get(-3), 3);
        assertEquals(stepper.get(-2), 6);
        assertEquals(stepper.get(-1), 9);
    }

    private static final int NUM_TRIALS = 10;
    private static final int LIST_LEN = 750000;
    private static final int NUM_GETS = 250000;

    @Test(timeout = 120000)
    public void testPerformanceGet() {
        Random r = new Random(0x61bL);
        LinkedList<Double> studToStaffRatios = new LinkedList<>();
        for (int trial = 0; trial < NUM_TRIALS; trial += 1) {
            int[] arr0 = r.ints(LIST_LEN, -10000, 10000).toArray();

            IntArrayList lst0 = new IntArrayList();
            IntArrayList lst1 = new IntArrayList();
            lst0.setList(arr0);
            lst1.setList(arr0);

            int step = r.nextInt(4) + 1;

            IntArrayListStepper studStepper = new IntArrayListStepper(lst0, step);
            StaffIntArrayListStepper staffStepper = new StaffIntArrayListStepper(lst1, step);

            double studentTime = 0.0;
            double staffTime = 0.0;
            double start;

            for (int getCount = 0; getCount < NUM_GETS; getCount += 1) {
                int index = r.nextInt((LIST_LEN - 1) / step) + -1 * LIST_LEN / step;

                start = System.nanoTime();
                int studResult = studStepper.get(index);
                studentTime += start - System.nanoTime();

                start = System.nanoTime();
                int staffResult = staffStepper.get(index);
                staffTime += start - System.nanoTime();

                assertEquals("Student get does not match staff get.", staffResult, studResult);
            }

            System.out.println("Successful .get test. Student to Staff time ratio: " + studentTime / staffTime);
            studToStaffRatios.addLast(studentTime / staffTime);
        }
        double minRatio = studToStaffRatios.stream().min(Double::compareTo).get();

        System.out.println("\nMinimum student to staff ratio: " + minRatio + "\n");

        assertTrue("Minimum student to staff ratio was greater than 5x", minRatio <= 5);

    }
}