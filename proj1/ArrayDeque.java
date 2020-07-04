import java.util.Objects;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int nextFront;
    private int nextBack;
    private T[] array;

    /**
     *Creates an empty array deque, again the starting size should be 8
     */
    public ArrayDeque(){
        this.array = (T[]) new Object[8];
        this.size = 0;
        this.nextFront = 0;
        this.nextBack = 1;
    }

    /**
     * Expands array
     */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(this.array, 0, a, 0, this.size);
        this.array = a;
        this.nextFront = this.array.length - 1;
        this.nextBack = this.size;
    }

    /**
     * Adds an item of type T to the nextFront of the deque.
     */
    public void addFirst(T item) {
        if (this.size == this.array.length) {
            resize(this.array.length * 2);
        }

        if (this.size < this.array.length) {
            this.array[this.nextFront] = item;
            this.size++;
            this.nextFront = (this.nextFront - 1 + this.array.length) % this.array.length;
        }
    }

    /**
     * Adds an item of type T to the nextBack of the deque.
     */
    public void addLast(T item) {
        if (this.size == this.array.length) {
            resize(this.array.length * 2);
        }

        if (this.size < this.array.length) {
            this.array[this.nextBack] = item;
            this.size++;
            this.nextBack = (this.nextBack + 1) % this.array.length;
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
        for (int i = 0; i < this.array.length; i++) {
//            if (this.array[i] != null) {
                System.out.print(this.get(i) + " ");
//            }
        }
        System.out.println(" ");
    }

    /**
     * Shrinks array
     */
    private void shrink() {
        T[] a = (T[]) new Object[this.array.length / 2];
        System.arraycopy(this.array, this.nextFront + 1, a,0, this.size);
        this.array = a;
        this.nextFront = this.array.length - 1;
        this.nextBack = this.size;
        System.out.println(this.nextFront); // run shrinkArrayTest to see result!
        System.out.println(this.nextBack); // run shrinkArrayTest to see result!
    }
    /**
     * Removes and returns the item at the nextFront of the deque.
     * If no such item exists, returns null
     */
    public T removeFirst() {
        T frontValue = null;
        if (this.isEmpty()) {
            return null;
        }
        if (this.array.length >= 16 && this.size <= this.array.length * 0.25) {
            shrink();
        }
        if (this.nextFront == this.array.length - 1) {
            frontValue = this.array[0];
            this.array[0] = null;
            this.nextFront = 0;
        } else {
            frontValue = this.array[this.nextFront + 1];
            this.array[this.nextFront + 1] = null;
            this.nextFront = (this.nextFront + 1) % this.array.length;
        }
        this.size--;
        return frontValue;
    }

    /**
     * Removes and returns the item at the nextBack of the deque.
     * If no such item exists, returns null
     */
    public T removeLast() {
        T backValue = null;
        if (this.isEmpty()) {
            return null;
        }
        if (this.array.length >= 16 && this.size <= this.array.length * 0.25) {
            shrink();
        }
        if (this.nextBack == 0) {
            backValue = this.array[this.array.length - 1];
            this.array[this.array.length - 1] = null;
            this.nextBack = 0;
        } else {
            backValue = this.array[this.nextBack - 1];
            this.array[this.nextBack - 1] = null;
            this.nextBack = (this.nextBack - 1) % this.array.length;
        }
        this.size--;

        return backValue;
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
