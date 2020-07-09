import java.util.*;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N containing all the
     * values from 0 to N except for one missing number.
     */
    // [1, 0, 2, 4]
    // linear time
    public static int missingNumber(int[] values) {
        Set<Integer> seenSoFar = new HashSet<>();
        for (int i: values) {
            seenSoFar.add(i);
        }
        for(int x = 0; x <= values.length; x++) {
            if (!seenSoFar.contains(x)) {
                return x;
            }
        }
        return -1;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     * Assume all values in the array are unique.
     */
    public static boolean sumTo(int[] values, int n) {
        boolean found = false;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            map.put(i, n - values[i]);
        }

        for (int i = 0; i < values.length; i++) {
            if (map.containsValue(values[i])) {
                found = true;
            }
        }

        return found;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        // TODO
        return false;
    }
}