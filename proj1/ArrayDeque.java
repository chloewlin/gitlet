import java.util.Objects;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int nextFront;
    private int nextBack;
    private T[] array;
    private int arrayLength;

    /**
     *Creates an empty array deque, again the starting size should be 8
     */
    public ArrayDeque(){
        this.array = (T[]) new Object[8];
        this.size = 0;
        this.nextFront = 0;
        this.nextBack = 1;
        this.arrayLength = this.array.length;
    }

    /**
     * Adds an item of type T to the nextFront of the deque.
     */
    public void addFirst(T item) {
        if (this.size < this.arrayLength) {
            this.array[this.nextFront] = item;
            this.size++;
            this.nextFront = (this.nextFront - 1 + this.arrayLength) % this.arrayLength;
        }
    }


    /**
     * Adds an item of type T to the nextBack of the deque.
     */
    public void addLast(T item) {
        if (this.get(1) == null) {
            this.array[1] = item;
        } else if (this.get(0) == null && this.nextBack == this.array.length - 1) {
            this.array[0] = item;
            this.nextBack = 0;
        } else if (this.get(1) != null) {
            for (int i = 2; i < this.array.length; i++) {
                if (this.get(i) == null) {
                    this.array[i] = item;
                    this.nextBack = i;
                    break;
                }
            }
        }

    }

    /**
     * Returns true if deque is empty, false otherwise.
     * This method should be deleted and migrated to Deque.java
     */
    public boolean isEmpty() {
        return this.size == 0;
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
        for (int i = 0; i < this.arrayLength; i++) {
            if (this.array[i] != null) {
                System.out.print(this.get(i) + " ");
            }
        }
        System.out.println(" ");
    }

    /**
     * Removes and returns the item at the nextFront of the deque.
     * If no such item exists, returns null
     */
    public T removeFirst() {
        if (this.array[this.nextFront] == null) {
            return null;
        } else {
            T nextFrontValue = this.array[this.nextFront];
            this.array[this.nextFront] = null;
            if (this.nextFront == this.array.length - 1) {
                this.nextFront = 0;
            } else {
                this.nextFront = this.nextFront + 1;
            }
            return nextFrontValue;
        }
    }

    /**
     * Removes and returns the item at the nextBack of the deque.
     * If no such item exists, returns null
     */
    public T removeLast() {
        if (this.array[this.nextBack] == null) {
            return null;
        } else {
            T nextBackValue = this.array[this.nextBack];
            this.array[this.nextBack] = null;
            if (this.nextBack == 0) {
                this.nextBack = this.array.length - 1;
            } else {
                this.nextBack = this.nextBack - 1;
            }
            return nextBackValue;
        }
    }

    /**
     * Gets the item at the given index, where 0 is the nextFront,
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
