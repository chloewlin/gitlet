import java.util.Objects;

public class ArrayDeque<T> implements Deque<T> {
    private T placeholder;
    private int size;
    private int frontIndex;
    private int backIndex;
    private T[] array;

    /**
     *Creates an empty array deque, again the starting size should be 8
     */
    public ArrayDeque(){
        this.array = (T[]) new Object[8];
        this.size = this.array.length;
        this.frontIndex = 0;
        this.backIndex = 1;
    }


    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        if (this.get(0) == null) {
            this.array[0] = item;
        } else if (this.get(0) != null) {
            for (int i = this.array.length - 1; i > this.backIndex; i--) {
                if (this.get(i) == null) {
                    this.array[i] = item;
                    this.frontIndex = i;
                    break;
                }
            }
        }
    }


    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {

    }

    /**
     * Returns true if deque is empty, false otherwise.
     * This method should be deleted and migrated to Deque.java
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * Returns the number of items in the deque.
     */
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        for (int i = 0; i < this.array.length; i++) {
            System.out.print(this.get(i) + " ");
        }
        System.out.println(" ");
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     */
    public T removeFirst() {
        return this.placeholder;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     */
    public T removeLast() {
        return this.placeholder;
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists,
     * returns null. Must not alter the deque
     */
    public T get(int index) {
        if (index > this.array.length - 1 || index < 0) {
            return null;
        }
        return this.array[index];
    }

}
