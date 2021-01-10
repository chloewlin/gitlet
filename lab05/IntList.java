/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * @author Maurice Lee and Wan Fung Chui
 */

public class IntList {

    /** The integer stored by this node. */
    public int item;
    /** The next node in this IntList. */
    public IntList next;

    /** Constructs an IntList storing ITEM and next node NEXT. */
    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an IntList storing ITEM and no next node. */
    public IntList(int item) {
        this(item, null);
    }

    /** Returns an IntList consisting of the elements in ITEMS.
     * IntList L = IntList.list(1, 2, 3);
     * System.out.println(L.toString()) // Prints 1 2 3 */
    public static IntList of(int... items) {
        /** Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }
        /** Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;
        /** Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }
        return head;
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */

    public int get(int position) {
        if (position < 0) {
            throw new IllegalArgumentException();
        }
        int count = 0;
        IntList p = this;

         while (p != null) {
            if (count == position) {
                return p.item;
            } else {
                count++;
                p = p.next;
            }
        }

        if (count <= position) {
            throw new IllegalArgumentException();
        }

        return -1;
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        String s = "";
        IntList p = this;
        while (p != null) {
            s = s + p.item + " ";
            p = p.next;
        }
        s = s.trim();
        return s;
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */
    public boolean equals(Object obj) {
        if (!this.getClass().isInstance(obj)) return false;
        if (!(obj instanceof IntList)) return false;
        IntList p = this;
        IntList q = (IntList) obj;
        int lengthP = getListLength(this);
        int lengthQ = getListLength(q);
        if (lengthP != lengthQ) {
            return false;
        }

        while (p != null && q != null) {
            int currValue = p.item;
            int objValue = q.item;
            if (currValue != objValue) {
                return false;
            } else {
                p = p.next;
                q = q.next;
            }
        }

        return true;
    }

    /**
     * Returns length of list.
     *
     * @param L the list
     * @return length of the list
     */
    private static int getListLength(IntList L) {
        int length = 0;

        while (L != null) {
            length++;
            L = L.next;
        }

        return length;
    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(int value) {
        IntList p = this;
        while (p.next != null) {
            p = p.next;
        }
        p.next = new IntList(value);
    }

    /**
     * Returns the smallest element in the list.
     *
     * @return smallest element in the list
     */
    public int smallest() {
        IntList p = this;
        int min = this.get(0);
        while (p != null) {
            if (p.item < min) {
                min = p.item;
            }
            p = p.next;
        }
        return min;
    }

    /**
     * Returns the sum of squares of all elements in the list.
     *
     * @return The sum of squares of all elements.
     */
    public int squaredSum() {
        IntList p = this;
        int sum = 0;

        while (p != null) {
            sum = sum + p.item * p.item;
            p = p.next;
        }
        return sum;
    }

    /**
     * Destructively squares each item of the list.
     *
     * @param L list to destructively square.
     */
    public static void dSquareList(IntList L) {
        while (L != null) {
            L.item = L.item * L.item;
            L = L.next;
        }
    }

    /**
     * Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListIterative(IntList L) {
        if (L == null) {
            return null;
        }
        IntList res = new IntList(L.item * L.item, null);
        IntList ptr = res;
        L = L.next;
        while (L != null) {
            ptr.next = new IntList(L.item * L.item, null);
            L = L.next;
            ptr = ptr.next;
        }
        return res;
    }

    /** Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        return new IntList(L.item * L.item, squareListRecursive(L.next));
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
    public static IntList dcatenate(IntList A, IntList B) {
        if (B == null) return A;
        if (A == null) return B;

        IntList p = A;
        while (p.next != null) {
            p = p.next;
        }
        p.next = B;

        return A;
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * non-destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
     public static IntList catenate(IntList A, IntList B) {
         if (A == null) {
             return B;
         }
         return new IntList(A.item, catenate(A.next, B));
     }

    /**
     * Returns a new IntList consisting of A followed by B,
     * non-destructively using iterative solution.
     */
     private static IntList catenateIterative(IntList A, IntList B) {
         if (B == null) return copyList(A);
         if (A == null) return B;
         IntList copy = copyList(A);
         IntList p = copy;
         while (p.next != null) {
             p = p.next;
         }
         p.next = B;
         return copy;
     }

     private static IntList copyList(IntList L) {
         IntList head = new IntList(L.item);
         IntList p = head;
         L = L.next;
         while (L != null) {
             p.next = new IntList(L.item);
             L = L.next;
             p = p.next;
         }
         return head;
     }

     public static IntList reverse(IntList L) {
        IntList prev = null;
        IntList curr = L;
        IntList next = null;

        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
     }
}