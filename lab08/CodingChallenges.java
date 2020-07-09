import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N containing all the
     * values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        int missingNum = -1;
        Set<Integer> seenSoFar = new HashSet<>();

        for (int i: values) {
            seenSoFar.add(i);
        }

        for (int x = 0; x <= values.length; x++) {
            if (!seenSoFar.contains(x)) {
                missingNum = x;
            }
        }

        return missingNum;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     * Assume all values in the array are unique.
     */
    public static boolean sumTo(int[] values, int n) {
        boolean found = false;
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < values.length; i++) {
            map.put(values[i], n - values[i]);
        }

        for (int i = 0; i < values.length; i++) {
            if (map.get(values[i]) != values[i] && map.containsValue(values[i])) {
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
        boolean isPermute = true;

        if (s1.length() != s2.length()) {
            return false;
        }

        Map<Character, Integer> map = new HashMap<>();

        for (int i = 0; i < s2.length(); i++) {
            map.put(s2.charAt(i), map.getOrDefault(map.get(s2.charAt(i)), 0) + 1);
        }

        for (int i = 0; i < s1.length(); i++) {
            if (map.containsKey(s1.charAt(i))) {
                map.put(s1.charAt(i), map.get(s1.charAt(i)) - 1);
            }
        }

        for (int v : map.values()) {
            if (v != 0) {
                isPermute = false;
            }
        }

        return isPermute;
    }
}
