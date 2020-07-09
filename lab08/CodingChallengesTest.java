import org.junit.Test;

import static org.junit.Assert.*;

public class CodingChallengesTest {

    @Test
    public void testMissingNumber() {
        int[] arr = {0, 1, 2, 3, 4, 5, 7};
        assertEquals(6, CodingChallenges.missingNumber(arr));

        int[] arr2 = {9, 5, 3, 0, 1, 6, 7, 2, 8};
        assertEquals(4, CodingChallenges.missingNumber(arr2));
    }

    @Test
    public void testSumTo() {
        int[] arr = {1, 2, 5, 8};
        assertTrue(CodingChallenges.sumTo(arr, 3));
        assertTrue(CodingChallenges.sumTo(arr, 9));
        assertFalse(CodingChallenges.sumTo(arr, 11));
        assertFalse(CodingChallenges.sumTo(arr, 4));

        int[] arr2 = {7, 2, 5, 0};
        assertTrue(CodingChallenges.sumTo(arr2, 7));
        assertTrue(CodingChallenges.sumTo(arr2, 9));
        assertFalse(CodingChallenges.sumTo(arr2, -7));
        assertFalse(CodingChallenges.sumTo(arr2, -5));
    }

    @Test
    public void testSumTo2() {
        int[] arr = {-48, 36, -5, 24, 97, 56};
        assertTrue(CodingChallenges.sumTo(arr, 31));
        assertTrue(CodingChallenges.sumTo(arr, 8));
        assertFalse(CodingChallenges.sumTo(arr, 100));
        assertFalse(CodingChallenges.sumTo(arr, 72));

        int[] arr2 = {-2, 5, 3, -1, 9};
        assertTrue(CodingChallenges.sumTo(arr2, 14));
        assertTrue(CodingChallenges.sumTo(arr2, -3));
        assertFalse(CodingChallenges.sumTo(arr2, -8));
        assertFalse(CodingChallenges.sumTo(arr2, -5));
    }

    @Test
    public void testIsPermute1() {
        String s1 = "cat";
        String s2 = "tac";
        String s3 = "act";
        String s4 = "der";
        assertTrue(CodingChallenges.isPermutation(s1, s2));
        assertTrue(CodingChallenges.isPermutation(s1, s3));
        assertFalse(CodingChallenges.isPermutation(s1, s4));
    }

    @Test
    public void testIsPermute2() {
        String s1 = "permute";
        String s2 = "mteurpe";
        String s3 = "magpie";
        assertTrue(CodingChallenges.isPermutation(s1, s2));
        assertFalse(CodingChallenges.isPermutation(s1, s3));
    }
}
