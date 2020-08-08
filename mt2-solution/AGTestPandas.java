import org.junit.Test;
import static org.junit.Assert.*;
import jh61b.grader.GradedTest;

import java.util.Random;
import java.util.Arrays;
import java.util.LinkedList;


public class AGTestPandas {

        @Test(timeout = 5000)
        @GradedTest(name = "compilationBonus", number = "a000", max_score = 6)
        public void compilationBonus() {
                assertTrue(true);
        }

        @Test(timeout = 5000)
        @GradedTest(name = "compareCheck", number = "a001", max_score = 1)
        public void compareCheck() {
                int[] arr = {42, 77};
                PandaBear bear = new PandaBear(arr, 40);
                int[] arr2 = {30};
                PandaBear bear2 = new PandaBear(arr2, 50);
                assertTrue(bear2.compareTo(bear) > 0);
                PandaBear bear3 = new PandaBear(arr2, 30);
                assertTrue(bear.compareTo(bear3) > 0);
                PandaBear bear4 = new PandaBear(arr2, 40);
                assertTrue(bear4.compareTo(bear) < 0);
        }

        @Test(timeout = 5000)
        @GradedTest(name = "givenTest", number = "a002", max_score = 1)
        public void test2() {
                int[] pandaArr = {20, 25, 30};
                PandaBear panda = new PandaBear(pandaArr,5);
                PandaTree tree = new PandaTree(panda);
                int[] bearArr = {20, 40, 55, 65};
                PandaBear bear = new PandaBear(bearArr,5);
                tree.add(bear);
                assertEquals(tree.mostAtRisk(), panda);
                assertEquals(tree.leastAtRisk(), bear);
                int[] newArr = {5};
                PandaBear newBear = new PandaBear(newArr,20);
                tree.add(newBear);
                assertEquals(tree.mostAtRisk(), panda);
                assertEquals(tree.leastAtRisk(), newBear);
        }

        @Test(timeout = 5000)
        @GradedTest(name = "nullTest", number = "a006", max_score = 1)
        public void testNull() {
                PandaTree nullTest = new PandaTree(null);
        }

        private static final int NUM_TRIALS = 10;
        private static final int NUM_BEARS = 4000;
        private static final int LIST_LEN = 200;

        private PandaBear[] randomBears(int numBears) {
                Random r = new Random(0x61bL);
                PandaBear[] result = new PandaBear[numBears];
                int weight;
                int[] arr;
                for(int bearCount = 0; bearCount < numBears; bearCount++) {
                        arr = r.ints(LIST_LEN, -10000, 10000).toArray();
                        weight = r.nextInt();
                        result[bearCount] = new PandaBear(arr, weight);
                }
                return result;
        }

        @Test(timeout = 5000)
        @GradedTest(name = "spectrumTest", number = "a008", max_score = 1)
        public void spectrumTest() {
                PandaBear[] bears = randomBears(NUM_BEARS);
                PandaTree tree = new PandaTree(bears[0]); // not testing null check
                for (int i = 1; i < bears.length; i++) {
                        tree.add(bears[i]);
                }
                Arrays.sort(bears);
                assertEquals("Student's leastAtRisk is not consistent with student's compareTo", bears[0], tree.mostAtRisk());
                assertEquals("Student's mostAtRisk is not consistent with student's compareTo", bears[NUM_BEARS - 1], tree.leastAtRisk());
        }

        @Test(timeout = 5000)
        @GradedTest(name = "fuzzTest", number = "a007", max_score = 1)
        public void fuzzTest1() {
                Random r = new Random(0x61bL);
                LinkedList<Double> studToStaffRatios = new LinkedList<>();
                for (int trial = 0; trial < NUM_TRIALS; trial += 1) {

                        int[] arr = r.ints(LIST_LEN, -10000, 10000).toArray();
                        int weight = r.nextInt();
                        PandaBear p0 = new PandaBear(arr, weight);
                        PandaBear sp0 = new PandaBear(arr, weight);
                        
                        PandaTree studentBears = new PandaTree(p0);
                        StaffTree staffBears = new StaffTree(sp0);

                        double studentTime = 0.0;
                        double staffTime = 0.0;
                        double start;


                        for(int bearCount = 0; bearCount < NUM_BEARS; bearCount++) {
                                arr = r.ints(LIST_LEN, -10000, 10000).toArray();
                                weight = r.nextInt();

                                start = System.nanoTime();
                                PandaBear p = new PandaBear(arr, weight);
                                studentBears.add(p);
                                studentTime += start - System.nanoTime();
                                
                                start = System.nanoTime();
                                PandaBear staff = new PandaBear(arr, weight);
                                staffBears.add(staff);
                                staffTime += start - System.nanoTime();

                                assertEquals("Student get does not match staff least", staffBears.leastAtRisk(), studentBears.leastAtRisk());
                                assertEquals("Student get does not match staff greatest", staffBears.mostAtRisk(), studentBears.mostAtRisk());
                        }
                        System.out.println("Successful .get test. Student to Staff time ratio: " + studentTime / staffTime);
                        studToStaffRatios.addLast(studentTime / staffTime);
                }
                double minRatio = studToStaffRatios.stream().min(Double::compareTo).get();

                System.out.println("\nMinimum student to staff ratio: " + minRatio + "\n");

                assertTrue("Minimum student to staff ratio was greater than 5x", minRatio <= 5);
        }
}