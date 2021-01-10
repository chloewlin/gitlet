import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class AGTestDNASequenceSet {

    @Test(timeout = 5000)
    public void compilationBonus() {
        assertTrue(true);
    }

    @Test(timeout = 5000)
    public void testSingleBase() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0});
        assertTrue(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{1}));
    }

    @Test(timeout = 5000)
    public void testSingleBases() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0});
        assertTrue(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{1}));

        set = new DNASequenceSet();
        set.add(new int[]{1});
        assertTrue(set.contains(new int[]{1}));
        assertFalse(set.contains(new int[]{2}));

        set = new DNASequenceSet();
        set.add(new int[]{2});
        assertTrue(set.contains(new int[]{2}));
        assertFalse(set.contains(new int[]{3}));

        set = new DNASequenceSet();
        set.add(new int[]{3});
        assertTrue(set.contains(new int[]{3}));
        assertFalse(set.contains(new int[]{0}));
    }

    @Test(timeout = 5000)
    public void testAllSameTwo() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,0});
        assertTrue(set.contains(new int[]{0,0}));
        assertFalse(set.contains(new int[]{0}));
    }

    @Test(timeout = 5000)
    public void testAllSameFour() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{1,1,1,1});
        assertTrue(set.contains(new int[]{1,1,1,1}));
        assertFalse(set.contains(new int[]{1}));
        assertFalse(set.contains(new int[]{1,1}));
        assertFalse(set.contains(new int[]{1,1,1}));
    }

    @Test(timeout = 5000)
    public void testDiffFourOrdered() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,1,2,3});
        assertTrue(set.contains(new int[]{0,1,2,3}));
        assertFalse(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{0,1,2}));
    }

    @Test(timeout = 5000)
    public void testMultipleSingle() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0});
        assertTrue(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{1}));
        assertFalse(set.contains(new int[]{2}));
        assertFalse(set.contains(new int[]{3}));


        set.add(new int[]{1});
        assertTrue(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{1}));
        assertFalse(set.contains(new int[]{2}));
        assertFalse(set.contains(new int[]{3}));

        set.add(new int[]{2});
        assertTrue(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{1}));
        assertTrue(set.contains(new int[]{2}));
        assertFalse(set.contains(new int[]{3}));

        set.add(new int[]{3});
        assertTrue(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{1}));
        assertTrue(set.contains(new int[]{2}));
        assertTrue(set.contains(new int[]{3}));
    }
    @Test(timeout = 5000)
    public void testMultipleOverlapping1() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,1});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));

        set.add(new int[]{3,2,1,0});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{3}));
        assertFalse(set.contains(new int[]{3,2}));
        assertFalse(set.contains(new int[]{3,2,1}));
        assertTrue(set.contains(new int[]{3,2,1,0}));
    }

    @Test(timeout = 5000)
    public void testMultipleOverlapping2() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{3,2,1,0});
        assertFalse(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{3}));
        assertFalse(set.contains(new int[]{3,2}));
        assertFalse(set.contains(new int[]{3,2,1}));
        assertTrue(set.contains(new int[]{3,2,1,0}));

        set.add(new int[]{0,1});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{3}));
        assertFalse(set.contains(new int[]{3,2}));
        assertFalse(set.contains(new int[]{3,2,1}));
        assertTrue(set.contains(new int[]{3,2,1,0}));
    }

    @Test(timeout = 5000)
    public void testMultipleNotOverlapping1() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,1});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{0,1,2}));
        assertFalse(set.contains(new int[]{0,1,2,3}));

        set.add(new int[]{0,1,2,3});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{0,1,2}));
        assertTrue(set.contains(new int[]{0,1,2,3}));
    }

    @Test(timeout = 5000)
    public void testMultipleNotOverlapping2() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,1,2,3});
        assertFalse(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{0,1,2}));
        assertTrue(set.contains(new int[]{0,1,2,3}));

        set.add(new int[]{0,1});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{0,1,2}));
        assertTrue(set.contains(new int[]{0,1,2,3}));
    }


    private static final int NUM_TRIALS = 10;
    private static final int NUM_SEQUENCES = 5000;
    private static final int MAX_SEQUENCE_LENGTH = 250;

    @Test(timeout = 120000)
    public void testPerformanceAdd() {
        Random r = new Random(0x61bL);
        LinkedList<Double> studToStaffRatios = new LinkedList<>();
        for (int trial = 0; trial < NUM_TRIALS; trial += 1) {
            DNASequenceSet studSet = new DNASequenceSet();
            StaffDNASequenceSet staffSet = new StaffDNASequenceSet();

            double studentTime = 0.0;
            double staffTime = 0.0;
            double start;

            HashSet<int[]> sequences = new HashSet<>();

            for (int sequenceCount = 0; sequenceCount < NUM_SEQUENCES; sequenceCount += 1) {
                int sequenceLength = r.nextInt(MAX_SEQUENCE_LENGTH);

                // create duplicate arrs to ensure separation
                int[] arr0 = r.ints(sequenceLength, 0, 4).toArray();
                int[] arr1 = arr0.clone();
                int[] arr2 = arr0.clone();

                sequences.add(arr0);

                start = System.nanoTime();
                studSet.add(arr1);
                studentTime += start - System.nanoTime();

                start = System.nanoTime();
                staffSet.add(arr2);
                staffTime += start - System.nanoTime();
            }

            // Verify Correctness
            for (int[] seq : sequences) {
                assertTrue(studSet.contains(seq));
            }

            System.out.println("Successful .add test. Student to Staff time ratio: " + studentTime / staffTime);
            studToStaffRatios.addLast(studentTime / staffTime);
        }
        double minRatio = studToStaffRatios.stream().min(Double::compareTo).get();
        assertTrue("Minimum student to staff ratio was greater than 5x", minRatio <= 5);

    }

    @Test(timeout = 120000)
    public void testPerformanceContains() {
        Random r = new Random(0x61bL);
        LinkedList<Double> studToStaffRatios = new LinkedList<>();
        for (int trial = 0; trial < NUM_TRIALS; trial += 1) {
            DNASequenceSet studSet = new DNASequenceSet();
            StaffDNASequenceSet staffSet = new StaffDNASequenceSet();

            double studentTime = 0.0;
            double staffTime = 0.0;
            double start;

            HashSet<int[]> sequences = new HashSet<>();

            for (int sequenceCount = 0; sequenceCount < NUM_SEQUENCES; sequenceCount += 1) {
                int sequenceLength = r.nextInt(MAX_SEQUENCE_LENGTH);

                // create duplicate arrs to ensure separation
                int[] arr0 = r.ints(sequenceLength, 0, 4).toArray();
                int[] arr1 = arr0.clone();
                int[] arr2 = arr0.clone();

                sequences.add(arr0);
                studSet.add(arr1);
                staffSet.add(arr2);
            }

            // Verify Correctness
            for (int[] seq : sequences) {
                start = System.nanoTime();
                studSet.contains(seq);
                studentTime += start - System.nanoTime();

                start = System.nanoTime();
                staffSet.contains(seq);
                staffTime += start - System.nanoTime();

                assertTrue(studSet.contains(seq));
            }

            // Check contains on sequences not contained
            for (int sequenceCount = 0; sequenceCount < NUM_SEQUENCES; sequenceCount += 1) {
                int sequenceLength = r.nextInt(MAX_SEQUENCE_LENGTH);

                // create duplicate arrs to ensure separation
                int[] arr0 = r.ints(sequenceLength, 0, 4).toArray();
                int[] arr1 = arr0.clone();
                int[] arr2 = arr0.clone();

                start = System.nanoTime();
                boolean studResult = studSet.contains(arr1);
                studentTime += start - System.nanoTime();

                start = System.nanoTime();
                boolean staffResult = staffSet.contains(arr2);
                staffTime += start - System.nanoTime();

                assertEquals("DNASequenceSet returns true for .contains but sequence should not be contained.", staffResult, studResult);
            }

            System.out.println("Successful .contains test. Student to Staff time ratio: " + studentTime / staffTime);
            studToStaffRatios.addLast(studentTime / staffTime);
        }
        double minRatio = studToStaffRatios.stream().min(Double::compareTo).get();

        System.out.println("\nMinimum student to staff ratio: " + minRatio + "\n");

        assertTrue("Minimum student to staff ratio was greater than 5x", minRatio <= 5);

    }
}