import java.util.Objects;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int front;
    private int back;
    private T[] array;

    /**
     *Creates an empty array deque, again the starting size should be 8
     */
    public ArrayDeque(){
        this.array = (T[]) new Object[8];
        this.size = this.array.length;
        this.front = 0;
        this.back = 1;
    }

    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        int currLength = this.array.length;
        if (this.get(0) == null) {
            this.array[0] = item;
        } else if (this.get(0) != null) {
            for (int i = currLength - 1; i >= this.back; i--) {
                if (this.get(i) == null) {
                    this.array[i] = item;
                    this.front = i;
                    break;
                }
            }
            if (this.front - this.back == 1 ||
                    this.back - this.front == currLength - 1) {

                T[] expanded = (T[]) new Object[currLength * 2];

//                 for (int i = 0; i < 8; i++) {
//                     expanded[i] = this.array[this.front];
//                     this.front++;
//
//                 }
                //front -> back do right copying/

            }
        }
    }


    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        if (this.get(1) == null) {
            this.array[1] = item;
        } else if (this.get(0) == null && this.back == this.array.length - 1) {
            this.array[0] = item;
            this.back = 0;
        } else if (this.get(1) != null) {
            for (int i = 2; i < this.array.length; i++) {
                if (this.get(i) == null) {
                    this.array[i] = item;
                    this.back = i;
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
        if (this.array[this.front] == null) {
            return null;
        } else {
            T frontValue = this.array[this.front];
            this.array[this.front] = null;
            if (this.front == this.array.length - 1) {
                this.front = 0;
            } else {
                this.front = this.front + 1;
            }
            return frontValue;
        }
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     */
    public T removeLast() {
        if (this.array[this.back] == null) {
            return null;
        } else {
            T backValue = this.array[this.back];
            this.array[this.back] = null;
            if (this.back == 0) {
                this.back = this.array.length - 1;
            } else {
                this.back = this.back - 1;
            }
            return backValue;
        }
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
