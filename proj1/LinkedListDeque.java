public class LinkedListDeque<T> implements Deque<T> {

    private static class Node<T> {
        public T item;
        public Node prev;
        public Node next;

        public Node(T item,  Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private T placeholder;
    private Node sentinel;
    private int size;

    /** Constructor which creates an empty linked list deque */
    public LinkedListDeque() {
        this.sentinel = new Node(42, null, null);
        this.sentinel.prev = this.sentinel;
        this.sentinel.next = this.sentinel;
        this.size = 0;
    }

    public LinkedListDeque(T item) {
        this.sentinel = new Node(42, null, null);
        Node newNode = new Node(item, this.sentinel, this.sentinel);
        this.sentinel.next = newNode;
        this.sentinel.prev = newNode;
        this.size++;
    }

    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        Node first = new Node(item, this.sentinel, null);
        if (this.size == 0) {
            first.next = this.sentinel;
            this.sentinel.prev = first;
        } else {
            first.next = this.sentinel.next;
            this.sentinel.next.prev = first;
        }
        this.sentinel.next = first;
        this.size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        Node last = new Node(item, null, null);
        if (this.size == 0) {
            last.prev = this.sentinel;
            last.next = this.sentinel;
            this.sentinel.prev = last;
            this.sentinel.next = last;
        } else {
            last.next = this.sentinel;
            last.prev = this.sentinel.prev;
            this.sentinel.prev.next = last;
            this.sentinel.prev = last;
        }
        this.size++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     * This method should be deleted and migrated to Deque.java
     */
    public boolean isEmpty() {
        return this.size == 0 ? true : false;
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
        Node node = sentinel.next;
        while (!node.equals(sentinel)) {
            System.out.print(node.item + " ");
            node = node.next;
        }
        System.out.println("");
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
        return this.placeholder;
    }


    /**
     * Same as get, but this method should be implemented using recursion
     */
    public T getRecursive(int index) {
        return this.placeholder;
    }
}
